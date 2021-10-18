package org.opensourceframework.component.dao.base;

import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.SqlConditionHelper;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.base.eo.BaseEoUtil;
import org.opensourceframework.base.eo.CamelToUnderline;
import org.opensourceframework.base.exception.SystemException;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.component.dao.annotation.ShardingTable;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.component.dao.helper.SqlHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.Assert;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成sql
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SqlTemplate<T extends BaseEo , PK extends Serializable> {
	private static final int WITH_NULL = 0;
	private static final int NOT_NULL = 1;

	/**
	 * 保存操作
	 *
	 * @param obj
	 * @return
	 */
	public String insert(T obj) {
		SQL sql = new SQL();
		sql.INSERT_INTO(obj.tableName());
		StringBuilder sb = new StringBuilder();
		String value = replaceInsertCreatePerson(obj.returnInsertColumnsDef(), obj.getCreatePerson());
		value = replaceInsertApplicationId(value, obj.getApplicationId());
		Long id = obj.getId();
		if ((null == obj.getId()) || Long.valueOf(0L).equals(obj.getId())) {
			id = getId(obj);
		}
		sb.append(id).append(",").append(value);
		sql.VALUES(obj.returnInsertColumnsName(), String.valueOf(sb));
		obj.setId(id);
		return sql.toString();
	}

	/**
	 * 生成分布式id
	 *
	 * @return
	 */
	private Long getId(T eo) {
		Long applicationId = null;
		if(eo != null) {
			applicationId = eo.getApplicationId();
		}
		if(applicationId == null) {
			applicationId = ServiceContext.getContext().getRequestApplicationId();
		}
		Long workerId = BaseEoUtil.getWorkerId();
		return Long.valueOf(SnowFlakeId.nextId(workerId, applicationId));
	}

	public String findNotWithShardingKey(Map<String, Object> whereMap){
		SQL sql = new SQL();
		if(MapUtils.isNotEmpty(whereMap)) {
			//获取操作的实体类
			Class<?> clazz  = (Class<?>) whereMap.get(SqlStatementContant.CLASS_PARAM);
			sql.SELECT(selectColumnsAlias(clazz));
			sql.FROM(BaseEoUtil.tableName(clazz));
			String where = buildShardingWhere(clazz , whereMap , 0);
			if(StringUtils.isBlank(where)){
				throw new SystemException("not found where condition.");
			}
			sql.WHERE(where);

			Integer limit = (Integer) whereMap.get(SqlStatementContant.QUERY_DATA_LIMIT_PARAM);
			if(limit == null || limit.intValue() == 0){
				limit = SqlStatementContant.QUERY_DATA_DEFAULT_LIMIT;
			}
			sql.LIMIT(limit);
		}else{
			throw new SystemException("not found where condition.");
		}
		return sql.toString();
	}

	public String deleteNotWithShardingKey(Map<String, Object> whereMap){
		SQL sql = new SQL();
		if(MapUtils.isNotEmpty(whereMap)) {
			//获取操作的实体类
			Class<?> clazz  = (Class<?>) whereMap.get(SqlStatementContant.CLASS_PARAM);
			String tableName = BaseEoUtil.tableName(clazz);
			String where = buildShardingWhere(clazz , whereMap , 0);
			if(StringUtils.isBlank(where)){
				throw new SystemException("not found where condition.");
			}

			Object flagObj = whereMap.get(SqlStatementContant.IS_LOGIC_DEL_KEY);
			Boolean isLogicDel = false;
			if(flagObj != null){
				isLogicDel = Boolean.valueOf(flagObj.toString());
			}

			int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 0;

			if (!isLogicDel.booleanValue()) {
				sql = new SQL();
				sql.DELETE_FROM(tableName);
				sql.WHERE(where);
			}else {
				sql.UPDATE(tableName);
				sql.SET(getUpdateCreatePersonDr(dr, null));
				sql.WHERE(where);
			}
		}else{
			throw new SystemException("not found where condition.");
		}
		return sql.toString();
	}

	/**
	 *
	 * @param clazz
	 * @param whereMap
	 * @param type   0查询   1更新
	 * @return
	 */
	private String buildShardingWhere(Class<?> clazz , Map<String, Object> whereMap , int type){
		ShardingTable shardingTable = clazz.getAnnotation(ShardingTable.class);
		if(shardingTable == null){
			throw new SystemException("{}'s ShardingTable not found.");
		}
		String tableNumParam = shardingTable.tableNumParam();
		StringBuilder sb = new StringBuilder();
		if(MapUtils.isNotEmpty(whereMap)) {
			Condition condition = (Condition) whereMap.get(SqlStatementContant.WHERE_CONDITION_KET);

			if(condition == null) {
				for (Map.Entry<String, Object> entry : whereMap.entrySet()) {
					// 分表逻辑参数 不参与where条件拼凑
					if (tableNumParam.equalsIgnoreCase(entry.getKey())
							|| SqlStatementContant.QUERY_DATA_LIMIT_PARAM.equalsIgnoreCase(entry.getKey())
							|| SqlStatementContant.CLASS_PARAM.equals(entry.getKey())) {
						continue;
					}

					if (sb.length() > 0) {
						sb.append(" and ");
					}
					if (type == 0) {
						sb.append(SqlHelper.propertyToColumn(entry.getKey())).append("=#{").append(entry.getKey()).append('}');
					} else if (type == 1) {
						sb.append(SqlHelper.propertyToColumn(entry.getKey())).append("=#{whereMap.").append(entry.getKey()).append('}');
					}
				}
			}else{
				FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
				sb.append(SqlConditionHelper.buildCondition(condition , fdf , clazz));
			}
		}
		return sb.toString();
	}

	public String updateNotWithShardingKey(Map<String, Object> paramMap){
		SQL sql = new SQL();
		Map<String , Object> whereMap = (Map<String , Object>)paramMap.get(SqlStatementContant.WHERE_MAP_KEY);
		Map<String , Object> updateMap = (Map<String , Object>)paramMap.get(SqlStatementContant.UPDATE_MAP_KET);
		if(MapUtils.isNotEmpty(whereMap) && MapUtils.isNotEmpty(updateMap)) {
			//获取操作的实体类
			Class<?> clazz = (Class<?>) paramMap.get(SqlStatementContant.CLASS_PARAM);
			sql.UPDATE(BaseEoUtil.tableName(clazz));
			BaseEo eo = null;
			try {
				eo = (BaseEo) clazz.getConstructor().newInstance();
				for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
					ReflectHelper.setFieldValue(eo, entry.getKey(), entry.getValue());
				}
				sql.SET(eo.returnShardingUpdateSetNotNull());
			} catch (Exception e) {
				throw new SystemException("class:" + clazz.getName() + ", not constructor method.");
			}

			sql.WHERE(buildShardingWhere(clazz, whereMap , 1));
		}else {
			if(MapUtils.isEmpty(whereMap)) {
				throw new SystemException("whereMap is enmpty!");
			}
			if(MapUtils.isEmpty(updateMap)) {
				throw new SystemException("updateMap is enmpty!");
			}
		}
		return sql.toString();
	}

	public String insertBatch(Map<String, Object> params) {
		List<T> eoList = (List) params.get("eoList");
		String sql = "sql error";
		if (CollectionUtils.isNotEmpty(eoList)) {
			T t = eoList.get(0);
			String tableName = t.tableName();

			StringBuffer sb = new StringBuffer("insert into ").append(tableName).append("(" + t.returnInsertColumnsNameBatch() + ")").append(" values ");

			String insertValueColumns = t.returnInsertColumnsDefBatch();
			MessageFormat mf = new MessageFormat(insertValueColumns);

			for (int i = 0; i < eoList.size(); i++) {
				T eo = eoList.get(i);
				Long pk = eo.getId();
				if (pk == null || Long.valueOf(0L).equals(pk)) {
					pk = getId(eo);
				}
				sb.append("(").append(pk).append(",");
				sb.append(mf.format(new Object[]{String.valueOf(i)}));
				if (i < eoList.size() - 1) {
					sb.append(",");
				}
				eoList.get(i).setId(pk);
			}
			if (t.tableName() != null) {
				sql = sb.toString();
			}
		}
		return sql;
	}

	private String replaceInsertApplicationId(String insertValue, Long applicationId) {
		if (applicationId == null) {
			applicationId = Long.valueOf(1L);
		}
		return insertValue.replaceAll("#applicationId", String.valueOf(applicationId));
	}

	private String replaceInsertCreatePerson(String insertValue, String createPerson) {
		if (StringUtils.isEmpty(createPerson)) {
			createPerson = "";
		}
		return insertValue.replaceAll("#createPerson", createPerson);
	}

	private String replaceInsertCreatePersonId(String insertValue, Long createPersonId) {
		String value = null;
		if(createPersonId != null) {
			value = insertValue.replaceAll("#createPersonId", createPersonId.toString());
		}
		return value;
	}

	public String updateWithNull(T obj) {
		return getUpdateSql(obj, Integer.valueOf(0));
	}

	public String updateNotNull(T obj) {
		return getUpdateSql(obj, Integer.valueOf(1));
	}

	private String getUpdateSql(T obj, Integer flag) {
		String returnUpdateSet = null;
		if (flag.intValue() == 0) {
			returnUpdateSet = obj.returnUpdateSetWithNull();
		} else {
			returnUpdateSet = obj.returnUpdateSetNotNull();
		}
		String idname = obj.idName();
		SQL sql = new SQL();
		sql.UPDATE(obj.tableName());
		sql.SET(returnUpdateSet);
		sql.WHERE(idname + "= #{" + idname + "}");
		return sql.toString();
	}

	public String updateByCondition(T t) {
		SQL sql = new SQL();
		sql.UPDATE(t.tableName());
		sql.SET(t.returnUpdateSetNotNull());
		String where = t.returnUpdateWhereColumnNames();
		if (!"".equals(where)) {
			sql.WHERE(where);
		} else {
			return null;
		}
		return sql.toString();
	}

	public String delete(T whereEo) {
		return deleteLogic(whereEo, Boolean.valueOf(false));
	}

	public String deleteLogic(T whereEo){
		return deleteLogic(whereEo, Boolean.valueOf(true));
	}

	private String deleteLogic(T obj, Boolean isLogicDel) {
		String where = getWhere(obj);
		if ("".equals(where)) {
			return null;
		}
		int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 0;
		String tableName = obj.tableName();
		SQL sql = new SQL();
		if ((isLogicDel != null) && (!isLogicDel.booleanValue())) {
			sql = new SQL();
			sql.DELETE_FROM(tableName);
			sql.WHERE(where);
		}else {
			sql.UPDATE(tableName);
			sql.SET(getUpdateCreatePersonDr(dr, obj.getUpdatePerson()));
			sql.WHERE(where);
		}
		return sql.toString();
	}

	public String deleteLogicById(Class<?> clazz, PK id) {
		Serializable[] ids = {id};
		return deleteLogicBatchIds(clazz, ids, true);
	}

	private String deleteLogicById(Class<?> clazz, Long id, Boolean isLogicDel) {
		Long[] ids = {id};
		return deleteLogicBatchIds(clazz, ids, isLogicDel);
	}

	public String deleteById(Class<?> clazz, PK pkId) {
		Serializable[] ids = {pkId};

		return deleteLogicBatchIds(clazz, ids, Boolean.valueOf(false));
	}

	public String deleteBatch(Class<?> clazz, PK[] ids) {
		return deleteLogicBatchIds(clazz, ids, Boolean.valueOf(false));
	}

	public String deleteLogicBatch(Class<?> clazz, PK[] ids){
		return deleteLogicBatchIds(clazz , ids , true);
	}
	
	public String deleteByCondition(T whereEo){
		StringBuilder sqlStr = new StringBuilder();
		SQL sql = new SQL();
		sql.DELETE_FROM(whereEo.tableName());
		String where = whereEo.returnWhereConditionColumnNames();
		if(StringUtils.isNotBlank(where)) {
			sql.WHERE(where);
			sqlStr.append(sql).append(";");
			return sqlStr.toString();
		}
		return null;
	}
	
	private String deleteLogicBatchIds(Class<?> clazz, Serializable[] ids, Boolean isLogicDel) {
		if ((ids == null) || (ids.length < 1)) {
			return null;
		}
		String idArray = StringUtils.join(ids, ",");
		int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 0;
		String tableName = BaseEoUtil.tableName(clazz);
		StringBuilder sqlStr = new StringBuilder();

		SQL sql = new SQL();
		sql.UPDATE(tableName);
		sql.SET(getUpdateCreatePersonDr(dr, null));
		sql.WHERE("id in (" + idArray + ") and dr = 0");

		if ((isLogicDel != null) && (!isLogicDel.booleanValue())) {
			sql = new SQL();
			sql.DELETE_FROM(tableName);
			sql.WHERE("id in (" + idArray + ")");
		}
		return sql.toString();
	}

	private String getUpdateCreatePersonDr(int dr, String updatePerson) {
		StringBuilder sb = new StringBuilder();
		sb.append("dr=").append(dr).append(",update_time=now()");
		String reqUupdatePerson = ServiceContext.getContext().getRequestUserCode();
		if ((StringUtils.isEmpty(reqUupdatePerson)) && (StringUtils.isNotEmpty(updatePerson))) {
			reqUupdatePerson = updatePerson;
		}
		if (StringUtils.isNotEmpty(reqUupdatePerson)) {
			sb.append(",update_person='").append(reqUupdatePerson).append("'");
		}
		return sb.toString();
	}

	public String findOne(T obj) {
		String sql = findColumn(obj);
		sql = sql.concat( " limit 1");
		return sql;
	}


	public String findColumn(T obj) {
		SQL sql = new SQL();
		String[] tableColumnNames = obj.getQueryTableColumnNames();
		sql.SELECT(getSelectColumnNames(obj, tableColumnNames));
		sql.FROM(obj.tableName());
		sql.WHERE(getWhere(obj));
		return sql.toString();
	}

	public String findIdByCondition(T obj) {
		SQL sql = new SQL();
		sql.SELECT("id");
		sql.FROM(obj.tableName());
		String where = obj.returnWhereConditionColumnNames();
		if (!"".equals(where)) {
			sql.WHERE(where);
			return sql.toString();
		}
		return null;
	}

	public String findByCondition(T obj){
		SQL sql = new SQL();
		sql.SELECT(getSelectColumnNames(obj));
		sql.FROM(obj.tableName());
		String where = obj.returnWhereConditionColumnNames();
		if (!"".equals(where)) {
			sql.WHERE(where);
			return sql.toString();
		}
		return null;
	}

	public String findList(T obj) {
		return findListColumn(obj);
	}

	public String findListColumn(T obj) {
		SQL sql = new SQL();
		String[] tableColumnNames = obj.getQueryTableColumnNames();
		sql.SELECT(getSelectColumnNames(obj, tableColumnNames));
		sql.FROM(obj.tableName());
		sql.WHERE(getWhere(obj));
		String orderBy = obj.resultSort();
		if (!"".equals(orderBy)) {
			sql.ORDER_BY(orderBy);
		}
		return sql.toString();
	}

	public String findPageList(T t, Integer currentPage, Integer pageSize) {
		currentPage = Integer.valueOf(currentPage != null ? currentPage.intValue() : 1);
		pageSize = Integer.valueOf(pageSize != null ? pageSize.intValue() : 10);
		int rowStartIndex = (currentPage.intValue() - 1) * pageSize.intValue();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(" limit ").append(rowStartIndex).append(",").append(pageSize);
		String sql = findList(t);
		return sql + strBuilder;
	}

	public String findPageListByCondition(T t, Integer currentPage, Integer pageSize) {
		currentPage = Integer.valueOf(currentPage != null ? currentPage.intValue() : 1);
		pageSize = Integer.valueOf(pageSize != null ? pageSize.intValue() : 10);
		int rowStartIndex = (currentPage.intValue() - 1) * pageSize.intValue();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(" limit ").append(rowStartIndex).append(",").append(pageSize);
		String sql = findByCondition(t);
		return sql + strBuilder;
	}

	public String findById(Class<?> clazz, PK id) {
		return findColumnById(clazz, id);
	}



	public String findByIds(Class<?> clazz, PK[] ids) {
		return findByIdsDr(clazz, ids, false);
	}

	public String findByIdsDr(Class<?> clazz, PK[] ids, Boolean containsDr) {
		return findColumnByIdsDr(clazz, ids, containsDr);
	}

	public String findColumnByIdsDr(Class<?> clazz, PK[] ids, Boolean containsDr, String... tableColumnName) {
		String selectColumnName = null;
		if ((tableColumnName != null) && (tableColumnName.length > 0)) {
			selectColumnName = StringUtils.join(tableColumnName, ",").toLowerCase();
			if (!SqlHelper.isSpiteParams(selectColumnName)) {
				selectColumnName = selectColumnsAlias(clazz);
			}
		} else {
			selectColumnName = selectColumnsAlias(clazz);
		}
		SQL sql = new SQL();
		sql.SELECT(selectColumnName);
		sql.FROM(BaseEoUtil.tableName(clazz));
		String idname = BaseEoUtil.idName(clazz);
		String where = null;
		if (ids.length == 1) {
			where = idname + " = " + ids[0];
		} else {
			where = idname + " in (" + StringUtils.join(ids, ",") + ")";
		}
		if (!containsDr.booleanValue()) {
			where = where + " and dr=0";
		}
		sql.WHERE(where);

		return sql.toString();
	}

	public String findColumnById(Class<?> clazz, PK id, String... tableColumnNames) {
		String selectColumnName = getSelectColumnNames(clazz , tableColumnNames);
		SQL sql = new SQL();
		sql.SELECT(selectColumnName);
		sql.FROM(BaseEoUtil.tableName(clazz));
		String idname = BaseEoUtil.idName(clazz);
		sql.WHERE(idname + " = " + id + " and dr=0");
		return sql.toString();
	}

	public String findColumnByIds(Class<?> clazz, PK[] ids, String... tableColumnNames) {
		return findColumnByIdsDr(clazz, ids, false, tableColumnNames);
	}


	private String getSelectColumnNames(Class<?> clazz , String... tableColumnNames){
		String selectColumnNames = null;
		if ((tableColumnNames != null) && (tableColumnNames.length > 0)) {
			selectColumnNames = StringUtils.join(tableColumnNames, ",").toLowerCase();
			if (!SqlHelper.isSpiteParams(selectColumnNames)) {
				selectColumnNames = selectColumnsAlias(clazz);
			}
		} else {
			selectColumnNames = selectColumnsAlias(clazz);
		}
		return selectColumnNames;
	}

	public String findAll(Class<?> clazz) {
		SQL sql = new SQL();
		sql.SELECT(selectColumnsAlias(clazz));
		sql.FROM(BaseEoUtil.tableName(clazz));
		sql.WHERE("dr = 0");
		return sql.toString();
	}

	public String countByClazz(Class<?> clazz) {
		SQL sql = new SQL();
		sql.SELECT("count(1)");
		sql.FROM(BaseEoUtil.tableName(clazz));
		sql.WHERE("dr = 0");
		return sql.toString();
	}

	public String count(T obj) {
		SQL sql = new SQL();
		sql.SELECT("count(1)");
		sql.FROM(obj.tableName());
		if (!"".equals(getWhere(obj))) {
			sql.WHERE(getWhere(obj));
		} else {
			sql.WHERE("dr = 0");
		}
		return sql.toString();
	}

	public String countByCondition(T obj) {
		SQL sql = new SQL();
		sql.SELECT("count(1)");
		sql.FROM(obj.tableName());

		String where = obj.returnWhereConditionColumnNames();
		if (!"".equals(where)) {
			sql.WHERE(where);
		}
		return sql.toString();
	}

	public String selectColumnsAlias(Class<?> paramClazz) {
		StringBuffer columnNames = new StringBuffer();
		columnNames.append("id, createTime,createPerson,createPersonId,updateTime, updatePerson, updatePersonId , applicationId, dr");
		Class<?> clazz = paramClazz;
		String autoHumpCol = System.getProperty(CommonCanstant.DB_AUTO_HUMP_MYSQL_COLUMN);
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				if (clazz != BaseEo.class) {
					Field[] declaredFields = clazz.getDeclaredFields();
					for (Field field : declaredFields) {
						if (field.isAnnotationPresent(Column.class)) {
							Column column = field.getAnnotation(Column.class);
							if (!"".equals(columnNames.toString())) {
								columnNames.append(",");
							}
							if (!"".equals(column.name())) {
								columnNames.append(column.name()).append(" as ").append(field.getName());
							} else {
								String fileName = null;
								if(CommonCanstant.YES_STR.equals(autoHumpCol)) {
									fileName = CamelToUnderline.camelToUnderline(field.getName());
								}else {
									fileName = field.getName();
								}

								columnNames.append(fileName).append(" as ").append(field.getName());
							}
						}
					}
				}
			} catch (Exception localException) {
			}
		}
		return columnNames.toString();
	}

	private String getSelectColumnNames(T obj, String... tableColumnName) {
		String selectColumnName = null;
		if ((tableColumnName != null) && (tableColumnName.length > 0)) {
			selectColumnName = StringUtils.join(tableColumnName, ",").toLowerCase();
			if (!SqlHelper.isSpiteParams(selectColumnName)) {
				selectColumnName = obj.returnSelectColumnsName();
			}
		} else {
			selectColumnName = obj.returnSelectColumnsName();
		}
		return selectColumnName;
	}

	private String getWhere(T obj) {
		StringBuilder where = new StringBuilder();
		if (obj.getId() != null) {
			where.append("id = ").append(obj.getId());
		}

		String returnWhere = obj.returnWhereColumnNames();
		if(where.length() > 0){
			where.append(" and ").append(returnWhere);
		}else{
			where.append(returnWhere);
		}

		if (StringUtils.isNotEmpty(obj.getCreatePerson())) {
			where.append(" and createPerson = '").append(obj.getCreatePerson()).append("'");
		}
		if (StringUtils.isNotEmpty(obj.getUpdatePerson())) {
			where.append(" and updatePerson = '").append(obj.getUpdatePerson()).append("'");
		}
		if (obj.getApplicationId() != null) {
			where.append(" and applicationId = ").append(obj.getApplicationId());
		}

		if (where.length() == 0) {
			where.append("dr = 0");
		}
		return where.toString();
	}

	/**
	 * 更新不为空的属性值
	 *
	 * @param obj
	 * @return
	 */
	public String updateNotNullByShardingEo(T obj) {
		return getShardingUpdateSql(obj, NOT_NULL);
	}

	/**
	 * 更新对象全部信息
	 *
	 * @param obj
	 * @return
	 */
	public String updateWithNullByShardingEo(T obj) {
		return getShardingUpdateSql(obj, WITH_NULL);
	}


	public String findListByShardingEo(T obj , String... tableColumnNames){
		Assert.isTrue(checkShardingKey(obj) , obj.getClass() + " 's shardingKey value is null for selecte");
		return this.findColumn(obj);
	}

	public String findListByShardingKey(Object shardingValue , Class<?> clazz , String... tableColumnNames){
		Assert.notNull(shardingValue , " shardingKey value is null for selecte");
		String selectColumnName = getSelectColumnNames(clazz , tableColumnNames);
		SQL sql = new SQL();
		sql.SELECT(selectColumnName);
		sql.FROM(BaseEoUtil.tableName(clazz));

		ShardingTable shardingTable = clazz.getAnnotation(ShardingTable.class);
		String property = shardingTable.property();
		String column = shardingTable.column();
		Assert.hasLength(property , clazz + " 's  property is null for sharding table");

		if(StringUtils.isBlank(column)){
			column = SqlHelper.propertyToColumn(property);
		}

		sql.WHERE(column + " = " + shardingValue + " and dr = 0");
		return sql.toString();
	}

	public String deleteByShardingEo(T obj){
		return deleteLogic(obj , false);
	}

	public String deleteLogicByShardingEo(T obj){
		return deleteLogic(obj , true);
	}

	public String deleteByShardingKey(Object shardingValue , Class clazz){
		Assert.notNull(shardingValue , " shardingKey value is null for delete");
		SQL sql = new SQL();
		sql.DELETE_FROM(BaseEoUtil.tableName(clazz));
		String column = getShardingKeyColumn(clazz);
		sql.WHERE(column + " = " + shardingValue);
		return sql.toString();
	}

	public String deleteLogicByShardingKey(Object shardingValue , Class clazz){
		Assert.notNull(shardingValue , " shardingKey value is null for delete");
		SQL sql = new SQL();
		sql.UPDATE(BaseEoUtil.tableName(clazz));

		int dr = 1;
		sql.SET(getUpdateCreatePersonDr(dr, null));
		String column = getShardingKeyColumn(clazz);
		sql.WHERE(column + " = " + shardingValue);
		return sql.toString();
	}

	private String deleteShardingLogic(T obj, Boolean isLogicDel) {
		String where = this.getShardingWhere(obj);
		if ("".equals(where)) {
			return null;
		} else {
			int dr = isLogicDel != null && !isLogicDel ? 2 : 1;
			String tableName = obj.tableName();
			SQL sql = new SQL();
			if (isLogicDel != null && !isLogicDel) {
				sql.DELETE_FROM(tableName);
				sql.WHERE(where);
			}else{
				sql.UPDATE(tableName);
				sql.SET(getUpdateCreatePersonDr(dr, obj.getUpdatePerson()));
				sql.WHERE(where);
			}
			return sql.toString();
		}
	}

	/**
	 * 获取分表对应实体类的列名
	 *
	 * @param clazz
	 * @return
	 */
	private String getShardingKeyColumn(Class<?> clazz){
		ShardingTable shardingTable = clazz.getAnnotation(ShardingTable.class);
		String property = shardingTable.property();
		String column = shardingTable.column();
		Assert.hasLength(property , clazz + " 's property is null for sharding table");

		if(StringUtils.isBlank(column)){
			column = SqlHelper.propertyToColumn(property);
		}
		return column;

	}

	private String getShardingWhere(T obj){
		StringBuilder where = new StringBuilder();
		where.append(obj.returnWhereColumnNames());
		ShardingTable shardingTable = obj.getClass().getAnnotation(ShardingTable.class);
		String propertyName = shardingTable.property();
		Assert.notNull(propertyName , obj.getClass() + " 's  column is null for drds ");
		Object value = ReflectHelper.getFieldValue(obj, propertyName);
		Assert.notNull(value , obj.getClass() + " 's shardingKey value is null for drds query");


		if (org.apache.commons.lang3.StringUtils.isNotEmpty(obj.getCreatePerson())) {
			where.append(" and createPerson = '").append(obj.getCreatePerson()).append("'");
		}

		if (org.apache.commons.lang3.StringUtils.isNotEmpty(obj.getUpdatePerson())) {
			where.append(" and updatePerson = '").append(obj.getUpdatePerson()).append("'");
		}

		if (obj.getApplicationId() != null) {
			where.append(" and applicationId = ").append(obj.getApplicationId());
		}

		return where.toString();
	}


	private String getSelectDrdsColumnNames(T obj, String... tableColumnName) {
		String selectColumnName = null;
		if (tableColumnName != null && tableColumnName.length > 0) {
			selectColumnName = org.apache.commons.lang3.StringUtils.join(tableColumnName, ",").toLowerCase();
			if (!SqlHelper.isSpiteParams(selectColumnName)) {
				selectColumnName = obj.returnSelectColumnsName();
			}
		} else {
			selectColumnName = obj.returnSelectColumnsName();
		}

		return selectColumnName;
	}

	private boolean checkShardingKey(T obj) {
		boolean isVlid = true;
		if(obj.getClass().isAnnotationPresent(ShardingTable.class)){
			ShardingTable shardingTable = obj.getClass().getAnnotation(ShardingTable.class);
			String property = shardingTable.property();
			Object value = ReflectHelper.getFieldValue(obj, property);
			if(value == null && StringUtils.isBlank(value.toString())){
				isVlid = false;
			}
		}
		return true;
	}


	private String getShardingUpdateSql(T obj, Integer flag) {
		String returnUpdateSet = null;
		if (flag == WITH_NULL) {
			returnUpdateSet = obj.returnUpdateSetWithNull();
		} else {
			returnUpdateSet = obj.returnUpdateSetNotNull();
		}
		String property = null;
		String column = null;
		if(obj.getClass().isAnnotationPresent(ShardingTable.class)){
			ShardingTable shardingTable = obj.getClass().getAnnotation(ShardingTable.class);
			property = shardingTable.property();
			column = shardingTable.column();
			Assert.hasLength(property , obj.getClass() + " 's  property is null for sharding update");

			if(StringUtils.isBlank(column)){
				column = SqlHelper.propertyToColumn(property);
			}
		}

		returnUpdateSet = hanlderReturnUpdateSet(returnUpdateSet , property);
		Object value = ReflectHelper.getFieldValue(obj, property);
		Assert.notNull(value , obj.getClass() + " 's shardingKey value is null for drds update");

		SQL sql = new SQL();
		sql.UPDATE(obj.tableName());
		sql.SET(returnUpdateSet);
		sql.WHERE(getShardingWhere(obj));
		return sql.toString();
	}

	private String hanlderReturnUpdateSet(String returnUpdateSet , String property){
		String[] updateSetArray =  returnUpdateSet.split(",");
		List<String> newUpdateSet = new ArrayList<>();
		for(String updateSet : updateSetArray){
			if(updateSet.indexOf(property) < 0){
				newUpdateSet.add(updateSet);
			}
		}
		return String.join("," , newUpdateSet);
	}
}
