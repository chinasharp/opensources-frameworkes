package org.opensourceframework.component.es.helper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.LogicalCondition;
import org.opensourceframework.base.db.OperationalEnum;
import org.opensourceframework.base.db.QueryCondition;
import org.opensourceframework.base.eo.CamelToUnderline;
import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.base.helper.ReflectHelper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Condition转化为ES语句
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ESConditionHelper {
	private static final Logger logger = LoggerFactory.getLogger(ESConditionHelper.class);
	private static final String SQL_PATTERN_CHAR = "%";

	public static String buildCondition(Condition condition , FastDateFormat dateFormat , Class<?> clazz){
		String sql = null;
		if(condition instanceof QueryCondition){
			sql = buildQueryCondition((QueryCondition)condition , dateFormat , clazz);
		}
		if(condition instanceof LogicalCondition){
			sql = buildLogicCondition((LogicalCondition)condition , dateFormat , clazz);
		}
		return sql;
	}

	/**
	 * 根据查询操作条件生成sql
	 *
	 * @param condition
	 * @param dateFormat
	 * @param clazz
	 * @return
	 */
	private static String buildQueryCondition(QueryCondition condition , FastDateFormat dateFormat , Class<?> clazz){
		StringBuilder sqlWhere = new StringBuilder();
		OperationalEnum operator = condition.getOperator();
		String fieldName = condition.getProperty();
		Object fieldVal = condition.getValue();

		if (operator != null  && StringUtils.isNotBlank(fieldName)) {
			String autoHumpCol = System.getProperty(CommonCanstant.DB_AUTO_HUMP_ES_COLUMN);
			if(CommonCanstant.YES_STR.equals(autoHumpCol)) {
				sqlWhere.append(CamelToUnderline.camelToUnderline(condition.getProperty()));
			}else {
				sqlWhere.append(condition.getProperty());
			}

			if(fieldVal != null) {
				switch (operator) {
					case EQ:
					case NE:
					case GT:
					case LT:
					case GTE:
					case LTE:
						sqlWhere.append(buildComparableOp(condition, dateFormat));
						break;
					case LIKE:
						fieldVal = likeValue(fieldVal.toString());
						sqlWhere.append(" like ").append("'").append(fieldVal).append("'");
						break;
					case IN:
					case CONTAINS:
						fieldVal = processOperatorInValue(condition, clazz);
						if (fieldVal != null && StringUtils.isNotBlank(fieldVal.toString())) {
							sqlWhere.append(" in (").append(fieldVal).append(")");
						}
						break;
                    case NIN:
                        fieldVal = processOperatorInValue(condition, clazz);
                        if (fieldVal != null && StringUtils.isNotBlank(fieldVal.toString())) {
                            sqlWhere.append(" not in (").append(fieldVal).append(")");
                        }
                        break;
					case ENDS_WITH:
						fieldVal = likeValue(fieldVal.toString());
						sqlWhere.append(" like ").append("'%").append(fieldVal).append("'");
						break;
					case STARTS_WITH:
						fieldVal = likeValue(fieldVal.toString());
						sqlWhere.append(" like ").append("'%").append(fieldVal).append("%'");
						break;
					default:
						logger.error("not found operator:{} for sql" , operator);
						break;
				}
			}else {
				switch (operator) {
					case IS_NULL:
						sqlWhere.append(" is null");
						break;
					case IS_NOT_NULL:
						sqlWhere.append(" is not null");
						break;
					default:
						logger.error("not found operator:{} for sql" , operator);
						break;
				}
			}
		}
		return sqlWhere.toString();
	}

	private static String buildLogicCondition(LogicalCondition logicalCondition, FastDateFormat dateFormat, Class<?> clazz) {
		// TODO Auto-generated method stub
		List<? extends Condition> conditions = logicalCondition.getConditions();
		StringBuilder filter = new StringBuilder();
		if (!CollectionUtils.isEmpty(conditions)) {
			Condition condition = conditions.get(0);
			// 处理conditions中只有一个QueryCondition的情况 这种是不需要And or连接的
			if (conditions.size() == 1 && condition instanceof QueryCondition) {
				filter.append(buildQueryCondition((QueryCondition) condition, dateFormat, clazz));
			} else {
				for (Condition subCondition : conditions) {

					if (filter.length() > 0) {
						if (OperationalEnum.AND == logicalCondition.getOperator()) {
							filter.append(" and ");
						}
						if (OperationalEnum.OR == logicalCondition.getOperator()) {
							filter.append(" or ");
						}
					}

					filter.append("(").append(buildCondition(subCondition, dateFormat, clazz)).append(")");
				}
			}
		}
		return filter.toString();
	}

	private static String buildComparableOp(QueryCondition condition ,FastDateFormat dateFormat){
		StringBuilder sqlWhere = new StringBuilder();
		if(OperationalEnum.EQ.equals(condition.getOperator())){
			sqlWhere.append(" = ");
		}
		if(OperationalEnum.NE.equals(condition.getOperator())){
			sqlWhere.append(" <> ");
		}
		if(OperationalEnum.LT.equals(condition.getOperator())){
			sqlWhere.append(" < ");
		}

		if(OperationalEnum.GT.equals(condition.getOperator())){
			sqlWhere.append(" > ");
		}

		if(OperationalEnum.GTE.equals(condition.getOperator())){
			sqlWhere.append(" >= ");
		}

		if(OperationalEnum.LTE.equals(condition.getOperator())){
			sqlWhere.append(" <= ");
		}

		Object fieldVal = condition.getValue();
		if (condition.getValue() instanceof Date) {
			Date val = (Date)fieldVal;
			sqlWhere.append("STR_TO_DATE('").append(DateHelper.YYYYMMDDHHMMSS(val)).append("' ,'%Y-%m-%d %H:%i:%s')");
		} else if (fieldVal instanceof String) {
			sqlWhere.append("'").append(fieldVal).append("'");
		} else {
			sqlWhere.append(fieldVal);
		}

		return sqlWhere.toString();
	}


		private static String likeValue(String value) {
			if (StringUtils.isEmpty(value)) {
				return value;
			} else {
				int start = 0;
				int end = value.length();
				String startValue = "";
				String endValue = "";
				if (SQL_PATTERN_CHAR.equals(value.substring(0, 1))) {
					start = 1;
					startValue = SQL_PATTERN_CHAR;
				}

				if (SQL_PATTERN_CHAR.equals(value.substring(value.length() - 1))) {
					end = value.length() - 1;
					endValue = SQL_PATTERN_CHAR;
				}

				String v = value.substring(start, end);
				if (v.indexOf(SQL_PATTERN_CHAR) != -1) {
					value = startValue + v.replaceAll(SQL_PATTERN_CHAR, "[%]") + endValue;
				}

				return value;
			}
		}

	protected static String processOperatorInValue(QueryCondition filter , Class<?> clazz) {
		if (filter != null && filter.getValue() != null) {
			ArrayList processedValues = Lists.newArrayList();

			try {
				Field field = ReflectHelper.getField(clazz, CamelToUnderline.underlineToCamel(filter.getProperty()));
				Class<?> fieldClass = field.getType();
				if (!String.class.equals(fieldClass)) {
					return filter.getValue() instanceof List ? Joiner.on(",").skipNulls().join((List)filter.getValue()) : filter.getValue().toString();
				} else {
					Object filterValue = filter.getValue();
					if (filterValue instanceof String) {
						String[] splitValues = ((String)filterValue).split(",");
						int length = splitValues.length;

						for(int index = 0; index < length; ++index) {
							String splitValue = splitValues[index];
							if (!splitValue.startsWith("'")) {
								splitValue = "'" + splitValue;
							}

							if (!splitValue.endsWith("'")) {
								splitValue = splitValue + "'";
							}

							processedValues.add(splitValue);
						}
					} else if (filterValue instanceof List) {
						List<?> listFilterValues = (List)filterValue;
						Iterator iterator = listFilterValues.iterator();

						while(iterator.hasNext()) {
							Object listValue = iterator.next();
							if (listValue != null) {
								if (listValue instanceof String) {
									String processValue = (String)listValue;
									if (!processValue.startsWith("'")) {
										processValue = "'" + processValue;
									}

									if (!processValue.endsWith("'")) {
										processValue = processValue + "'";
									}

									processedValues.add(processValue);
								} else {
									processedValues.add("'" + listValue + "'");
								}
							}
						}
					} else {
						processedValues.add("'" + filterValue + "'");
					}

					return Joiner.on(",").skipNulls().join(processedValues);
				}
			} catch (Exception e) {
				logger.error("sqlfiter传入的property有误", e);
				return null;
			}
		} else {
			return null;
		}
	}


	public static BoolQueryBuilder buildBoolQueryBuilder(Map<String , Object> queryParams){
		if(MapUtils.isNotEmpty(queryParams)) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			queryParams.forEach((fieldName, fieldVal) -> {
				if (fieldVal == null) {
					queryBuilder.mustNot(QueryBuilders.existsQuery(fieldName));
				} else {
					queryBuilder.must(QueryBuilders.termQuery(fieldName, fieldVal));
				}
			});
			return queryBuilder;
		}else {
			return null;
		}

	}
}
