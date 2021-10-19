package org.opensourceframework.component.mongodb.helper;

import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.LogicalCondition;
import org.opensourceframework.base.db.OperationalEnum;
import org.opensourceframework.base.db.QueryCondition;
import com.mongodb.client.model.Filters;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Condition生成Document 或者Bson帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MongoHelper {
	private static final Logger logger = LoggerFactory.getLogger(MongoHelper.class);

	/**
	 * 根据Condition生成Document
	 *
	 * @param condition
	 * @return
	 */
	public static Document buildConditionDocument(Condition condition){
		Document document = null;

		if(condition instanceof QueryCondition){
			document = buildQueryConditionDocument((QueryCondition)condition);
		}

		if(condition instanceof LogicalCondition){
			document = buildLogicConditionDocument((LogicalCondition)condition);
		}

		return document;
	}

	/**
	 * 根据Condition生成Bson
	 *
	 * @param condition
	 * @return
	 */
	public static Bson buildConditionBson(Condition condition){
		Bson bson = null;

		if(condition instanceof QueryCondition){
			bson = buildQueryConditionBson((QueryCondition)condition);
		}

		if(condition instanceof LogicalCondition){
			bson = buildLogicConditionBson((LogicalCondition)condition);
		}

		return bson;
	}


	private static Document buildLogicConditionDocument(LogicalCondition condition) {
		// TODO Auto-generated method stub
		List<? extends Condition> conditions = condition.getConditions();
		Document document = null;
		if (!CollectionUtils.isEmpty(conditions)) {
			// 处理conditions中只有一个QueryCondition的情况 这种是不需要And or连接的
			if (conditions.size() == 1) {
				document = buildConditionDocument(conditions.get(0));
			} else {
				List<Document> filters = new ArrayList<>();
				for (Condition subCondition : conditions) {
					filters.add(buildConditionDocument(subCondition));
				}

				document = new Document("$".concat(condition.getOperator().getKeyword()), filters);
			}
		} else {
			new Document();
		}
		return document;
	}

	private static Bson buildLogicConditionBson(LogicalCondition condition) {
		Bson filter = null;
		List<? extends Condition> conditions = condition.getConditions();
		try {
			if (!CollectionUtils.isEmpty(conditions)) {
				// 处理conditions中只有一个QueryCondition的情况 这种是不需要And or连接的
				if (conditions.size() == 1 && conditions.get(0) instanceof QueryCondition) {
					filter = buildConditionBson(conditions.get(0));
				} else {
					Method method = null;
					List<Bson> filters = new ArrayList<>();
					for (Condition subCondition : conditions) {
						filters.add(buildConditionBson(subCondition));
					}
					method = Filters.class.getMethod(condition.getOperator().getKeyword(), Iterable.class);
					filter = (Bson) method.invoke(null, filters);
				}
			} else {
				filter = new Document();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filter;
	}



	private static Document buildQueryConditionDocument(QueryCondition condition) {
		Document document = null;
		OperationalEnum operator = condition.getOperator();
		String property = condition.getProperty();
		Object value = condition.getValue();

		if (operator == null) {
			return new Document();
		}
		String keyword = operator.getKeyword();
		try {
			switch (keyword) {
				case "eq":
					document = new Document(property, new Document("$eq", value));
					break;
				case "gt":
					document = new Document(property, new Document("$gt", value));
					break;
				case "gte":
					document = new Document(property, new Document("$gte", value));
					break;
				case "lt":
					document = new Document(property, new Document("$lt", value));
					break;
				case "lte":
					document = new Document(property, new Document("$lte", value));
					break;
				case "ne":
					document = new Document(property, new Document("$ne", value));
					break;
				case "in":
					if (value instanceof Collection) {
						document = new Document(property, new Document("$in", value));
					} else {
						logger.error("参数类型错误,使用in查询参数类型为Collection");
						throw new Exception("参数类型错误,使用in查询参数类型为Collection");
					}
					break;
				case "nin":
					if (value instanceof Collection) {
						document = new Document(property, new Document("$nin", value));
					} else {
						logger.error("参数类型错误,使用nin查询参数类型为Collection");
						throw new Exception("参数类型错误,使用nin查询参数类型为Collection");
					}
					break;
				case "exists":
					if (value instanceof Boolean) {
						document = new Document(property, new Document("$exists", value));
					} else {
						logger.error("参数类型错误,使用exists查询参数类型为Boolean");
						throw new Exception("参数类型错误,使用exists查询参数类型为Boolean");
					}
					break;
				case "type":
					document = new Document(property, new Document("$type", value));
					break;
				case "contains":
					document = new Document(property, new Document("$regex", ".*".concat(value.toString()).concat(".*")));
					break;
				case "startsWith":
					document = new Document(property, new Document("$regex", value.toString().concat(".*")));
					break;
				case "endsWith":
					document = new Document(property, new Document("$regex", ".*".concat(value.toString())));
					break;
				case "regex":
					document = new Document(property, new Document("$regex", value));
					break;
				default:
					break;
			}
		} catch (Exception e) {
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		}
		return document;
	}

	private static Bson buildQueryConditionBson(QueryCondition condition) {
		Bson bson = null;
		OperationalEnum operator = condition.getOperator();
		String property = condition.getProperty();
		Object value = condition.getValue();
		if (operator == null) {
			return new Document();
		}
		String keyword = operator.getKeyword();
		try {
			switch (keyword) {
				case "eq":
					bson = Filters.eq(property, value);
					break;
				case "gt":
					bson = Filters.gt(property, value);
					break;
				case "gte":
					bson = Filters.gte(property, value);
					break;
				case "lt":
					bson = Filters.lt(property, value);
					break;
				case "lte":
					bson = Filters.lte(property, value);
					break;
				case "ne":
					bson = Filters.ne(property, value);
					break;
				case "in":
					if (value instanceof Collection) {
						Collection<Object> fieldVals = (Collection<Object>) value;
						bson = Filters.in(property, fieldVals);
					} else {
						logger.error("参数类型错误,使用in查询参数类型为Collection");
						throw new Exception("参数类型错误,使用in查询参数类型为Collection");
					}
					break;
				case "nin":
					if (value instanceof Collection) {
						Collection<Object> fieldVals = (Collection<Object>) value;
						bson = Filters.nin(property, fieldVals);
					} else {
						logger.error("参数类型错误,使用nin查询参数类型为Collection");
						throw new Exception("参数类型错误,使用nin查询参数类型为Collection");
					}
					break;
				case "exists":
					if (value instanceof Boolean) {
						bson = Filters.exists(property, (Boolean) value);
					} else {
						bson = Filters.exists(property);
					}
					break;
				case "type":
					bson = Filters.type(property, value.toString());
					break;
				case "contains":
					bson = Filters.regex(property, ".*".concat(value.toString()).concat(".*"));
					break;
				case "startsWith":
					bson = Filters.regex(property, value.toString().concat(".*"));
					break;
				case "endsWith":
					bson = Filters.regex(property, ".*".concat(value.toString()));
				case "regex":
					bson = Filters.regex(property, ".*".concat(value.toString()).concat(".*"));
					break;
				default:
					break;
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition operator is error!case:{}", e.getMessage());
		}
		return bson;
	}

}
