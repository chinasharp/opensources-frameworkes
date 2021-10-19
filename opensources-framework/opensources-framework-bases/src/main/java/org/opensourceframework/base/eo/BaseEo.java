package org.opensourceframework.base.eo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.Sort;
import org.opensourceframework.base.db.SortEnum;
import org.opensourceframework.base.db.SqlConditionHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库实体类基类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseEo implements Serializable {
    private static final long serialVersionUID = 304762148012121215L;

    private static final String GET_METHOD_NAME_PREFIX = "get";
    /**
     * Serializable 内置标记接口的2方法之一 writeReplace和readResolve
     */
    private static final String SERIALIZED_MARKER_METHOD_NAME = "writeReplace";
    /**
     * SerializableLambda 实现类名的连接符号
     */
    private static final String SERIALIZED_LAMBDA_IMPL_CLASS_SPLIT_CHAR = "/";
    private static final String CLASS_NAME_SPLIT_CHAR = ".";

    private static final transient Logger logger = LoggerFactory.getLogger(BaseEo.class);
    private static final transient Map<Class<? extends BaseEo>, List<EoField>> mapColumns = new ConcurrentHashMap<>();
    private static final transient Map<Class<? extends BaseEo>, String> mapId = new ConcurrentHashMap<>();

    @Id
    protected Long id;
    /**
     * 应用实例ID
     */
    @Column(name = "application_id")
    protected Long applicationId;

    @Column(name = "create_person")
    protected String createPerson;
    @Column(name = "create_person_id")
    protected Long createPersonId;

    @Column(name = "create_time")
    protected Date createTime;

    @JsonIgnore
    @Column(name = "create_time_stamp")
    @JSONField(serialize = false)
    protected Long createTimeStamp;

    @Column(name = "update_person")
    protected String updatePerson;

    @Column(name = "update_person_id")
    protected Long updatePersonId;

    @Column(name = "update_time")
    protected Date updateTime;

    @JsonIgnore
    @JSONField(serialize = false)
    @Column(name = "update_time_stamp")
    protected Long updateTimeStamp;

    /**
     * 0 正常数据
     * 1 已删除数据
     */
    @Column(name = "dr")
    protected Integer dr = 0;

    @JSONField(serialize = false)
    @Transient
    protected Condition condition;

    @JSONField(serialize = false)
    @Transient
    protected List<Sort> sortList;

    /**
     * 查询的列名
     */
    @JSONField(serialize = false)
    @Transient
    private String[] queryTableColumnNames;

    /**
     * 异步请求时记录的请求id
     */
    @JSONField(serialize = false)
    @Transient
    protected String asyncRequestId;

    /**
     * 数据库查询为NULL的标识位  用于缓存操作  避免缓存穿透
     */
    protected Boolean dbNullFlag = false;

    public BaseEo() {
        asyncRequestId = ServiceContext.getContext().getRequestId();
    }

    public void desc(String... properties) {
        if (properties != null && properties.length > 0) {
            if (sortList == null) {
                sortList = Lists.newArrayList();
            }

            int index = sortList.size();
            for (String property : properties) {
                Sort sort = new Sort(property, SortEnum.DESC, index);
                index++;
                sortList.add(sort);
            }
        }
    }

    public void asc(String... properties) {
        if (properties != null && properties.length > 0) {
            if (sortList == null) {
                sortList = Lists.newArrayList();
            }

            int index = sortList.size();
            for (String property : properties) {
                Sort sort = new Sort(property, SortEnum.ASC, index);
                index++;
                sortList.add(sort);
            }
        }
    }

    public static BaseEo newInstance(Class<? extends BaseEo> clazz) {
        try {
            return build(clazz);
        } catch (Exception exception) {
            throw new RuntimeException("INIT instance ERROR_CODE");
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCreatePerson() {
        return this.createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if (createTime != null) {
            setCreateTimeStamp(createTime.getTime());
        }
    }

    public String getUpdatePerson() {
        return this.updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Long getCreateTimeStamp() {
        if (createTimeStamp == null && createTime != null) {
            createTimeStamp = createTime.getTime();
        }
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Long getUpdateTimeStamp() {
        if (updateTimeStamp == null && updateTime != null) {
            updateTimeStamp = updateTime.getTime();
        }
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        if (updateTime != null) {
            setUpdateTimeStamp(updateTime.getTime());
        }
    }

    public Integer getDr() {
        return this.dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public String tableName() {
        return BaseEoUtil.tableName(this.getClass());
    }

    public String idName() {
        String idFieldName = mapId.get(this.getClass());
        if (idFieldName == null) {
            idFieldName = BaseEoUtil.idName(this.getClass());
            mapId.put(this.getClass(), idFieldName);
        }

        return idFieldName;
    }

    private boolean isWhereNull(String fieldName) {
        try {
            return fieldValueWhere(getClass(), fieldName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return true;
        }
    }

    private boolean fieldValueWhere(Class<?> clazz, String fieldName) throws Exception {
        while (clazz != BaseEo.class && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(this) == null || "".equals(field.get(this));
            } catch (NoSuchFieldException exception) {
                clazz = clazz.getSuperclass();
            }
        }
        return true;
    }

    private boolean isNull(String fieldName) {
        try {
            return this.fieldValueBoolean(this.getClass(), fieldName);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return true;
        }
    }

    private boolean fieldValueBoolean(Class<?> clazz, String fieldName) throws Exception {
        while (clazz != BaseEo.class && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(this) == null;
            } catch (NoSuchFieldException exception) {
                clazz = clazz.getSuperclass();
            }
        }
        return false;
    }

    /**
     * 初始化BaseEo自身的EoFied信息
     */
    private void initBaseEoColumnList(){
        if(!mapColumns.containsKey(BaseEo.class)){
            List<EoField> baseEoFieldList = new ArrayList();
            Field[] fields = BaseEo.class.getDeclaredFields();
            for (int index = 0; index < fields.length; ++index) {
                Field field = fields[index];
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String fieldName = field.getName();
                    String tableColumn = column.name();
                    EoField eoField = new EoField(fieldName, tableColumn);
                    baseEoFieldList.add(eoField);
                }
            }
            if (CollectionUtils.isNotEmpty(baseEoFieldList)) {
                mapColumns.put(BaseEo.class, baseEoFieldList);
            }
        }
    }

    /**
     * 实体对象属性和表字段的关系列表
     */
    private void calcColumnList() {
        initBaseEoColumnList();

        if (!mapColumns.containsKey(this.getClass())) {
            List<EoField> eoFieldList = new ArrayList();
            Set<String> attributeSet = new HashSet();
            EoField eoField = null;
            Class<?> clazz = this.getClass();

            for (; clazz != BaseEo.class; clazz = clazz.getSuperclass()) {
                try {
                    Field[] fields = clazz.getDeclaredFields();
                    int length = fields.length;
                    for (int index = 0; index < length; ++index) {
                        Field field = fields[index];
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            String eoAttribute = field.getName();
                            if (!attributeSet.contains(eoAttribute)) {
                                String tableColumn = column.name();
                                if (StringUtils.isBlank(tableColumn)) {
                                    String autoHumpCol = System.getProperty(CommonCanstant.DB_AUTO_HUMP_MYSQL_COLUMN);
                                    if (CommonCanstant.YES_STR.equals(autoHumpCol)) {
                                        tableColumn = CamelToUnderline.camelToUnderline(eoAttribute);
                                    } else {
                                        tableColumn = eoAttribute;
                                    }
                                }

                                eoField = new EoField(eoAttribute, tableColumn);
                                eoFieldList.add(eoField);

                                attributeSet.add(eoAttribute);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            mapColumns.put(this.getClass(), eoFieldList);
        }
    }

    /**
     * 生产select 语句的 需要查询的字段语句
     *
     * @return
     */
    public String returnSelectColumnsName() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            if (sb.length() > 0) {
                sb.append(',');
            } else {
                sb.append(BaseEoUtil.BASE_SELECT_SQL).append(",");
            }

            sb.append(eoField.getTableColumn());
            sb.append(" as ");
            sb.append(eoField.getEoAttribute());
        }
        return sb.toString();
    }


    /**
     * 插入语句 要插入数据的字段
     *
     * @return
     */
    public String returnInsertColumnsName() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder("id,");
        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            if (!this.isNull(eoField.getEoAttribute())) {
                sb.append(eoField.getTableColumn()).append(",");
            }
        }

        sb.append(this.insertColumn());
        return sb.toString();
    }

    /**
     * 生成插入语句的insert into
     *
     * @return
     */
    public String returnInsertColumnsDef() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            String eoAttribute = eoField.getEoAttribute();
            if (!this.isNull(eoAttribute)) {
                sb.append("#{").append(eoAttribute).append("},");
            }
        }

        sb.append(this.insertValue(false));
        return sb.toString();
    }

    /**
     * 生成批量保存的insert into语句
     *
     * @return
     */
    public String returnInsertColumnsNameBatch() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder("id,");
        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            sb.append(eoField.getTableColumn()).append(",");
        }

        sb.append(this.insertColumn());
        return sb.toString();
    }

    /**
     * 生成批量保存的values语句
     *
     * @return
     */
    public String returnInsertColumnsDefBatch() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            sb.append("#'{'eoList[{0}].").append(eoField.getEoAttribute()).append("'}',");
        }

        sb.append(this.insertValue(true)).append(")");
        return sb.toString();
    }


    private String insertColumn() {
        StringBuilder sb = new StringBuilder();
        sb.append(BaseEoUtil.BASE_INSERT_SQL);
        return sb.toString();
    }

    private String insertValue(Boolean isBatch) {
        StringBuilder sb = new StringBuilder();
        Long nowMillis = System.currentTimeMillis();
        sb.append("now(),").append(nowMillis).append(",");
        sb.append("now(),").append(nowMillis).append(",");

        String person = ServiceContext.getContext().getRequestUserCode();
        Long personId = ServiceContext.getContext().getRequestUserId();
        if (StringUtils.isBlank(person)) {
            person = this.getCreatePerson();
        }
        if (personId == null) {
            personId = this.getCreatePersonId();
        }
        person = (null == person) ? "" : person;

        Long applicationId = ServiceContext.getContext().getRequestApplicationId();
        if (applicationId == null) {
            applicationId = getApplicationId();
        }
        applicationId = (null == applicationId) ? 0L : applicationId;

        if (isBatch) {
            sb.append("''").append(person).append("'',").append(personId).append(",").append(applicationId).append(",''");
            sb.append(person).append("'',").append(personId).append(",").append("#'{'eoList[{0}].dr'}'");
        } else {
            sb.append("'").append(person).append("'").append(",").append(personId).append(",").append(applicationId);
            sb.append(",'").append(person).append("'").append(",").append(personId).append(",").append("#{dr}");
        }

        return sb.toString();
    }

    /**
     * 生成更新的操作的set 语句
     *
     * @return
     */
    private String updateColumn() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dr").append("=#{dr},");
        stringBuilder.append(getColumnName(BaseEo::getCreateTime)).append("=now()");
        String updatePerson = ServiceContext.getContext().getRequestUserCode();
        Long updatePersonId = ServiceContext.getContext().getRequestUserId();
        if (null != this.getUpdatePerson() && !"admin".equals(this.getUpdatePerson())) {
            updatePerson = this.getUpdatePerson();
        }
        if (null != this.getUpdatePersonId() && !Long.valueOf(0).equals(this.getUpdatePerson())) {
            updatePersonId = this.getUpdatePersonId();
        }

        if (StringUtils.isNotEmpty(updatePerson)) {
            stringBuilder.append(",").append(getColumnName(BaseEo::getUpdatePerson)).append("='").append(updatePerson).append("'");
        }
        if (updatePersonId != null) {
            stringBuilder.append(",").append(getColumnName(BaseEo::getUpdatePersonId)).append("=").append(updatePersonId);
        }

        return stringBuilder.toString();
    }

    /**
     * 生成更新的操作的set 语句
     *
     * @return
     */
    private String updateShardingColumn() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getColumnName(BaseEo::getUpdateTime)).append("=now()");
        String updatePerson = ServiceContext.getContext().getRequestUserCode();
        if (null != this.getUpdatePerson() && !"0".equals(this.getUpdatePerson())) {
            updatePerson = this.getUpdatePerson();
        }

        if (StringUtils.isNotEmpty(updatePerson)) {
            stringBuilder.append(",").append(getColumnName(BaseEo::getUpdatePersonId)).append("='").append(updatePerson).append("'");
        }

        return stringBuilder.toString();
    }

    /**
     * 包含所有属性的更新语句
     *
     * @return
     */
    public String returnUpdateSetWithNull() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        if (this.getColumnList() != null && this.getColumnList().size() > 0) {
            sb.append(this.updateColumn());
        }

        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            EoField eoField = iterator.next();
            sb.append(eoField.getTableColumn()).append("=#{").append(eoField.getEoAttribute()).append('}');
        }

        return sb.toString();
    }

    /**
     * 只包含有值属性的更新语句
     *
     * @return
     */
    public String returnUpdateSetNotNull() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        if (this.getColumnList() != null && this.getColumnList().size() > 0) {
            sb.append(this.updateColumn());
        }

        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();

            // 判断属性的值是否为null
            boolean isNullValue = this.isNull(eoField.getEoAttribute());
            if (!isNullValue) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(eoField.getTableColumn()).append("=#{").append(eoField.getEoAttribute()).append('}');
            }
        }

        return sb.toString();
    }

    /**
     * 只包含有值属性的更新语句
     *
     * @return
     */
    public String returnShardingUpdateSetNotNull() {
        this.calcColumnList();
        StringBuilder sb = new StringBuilder();
        if (this.getColumnList() != null && this.getColumnList().size() > 0) {
            sb.append(this.updateShardingColumn());
        }

        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();

            // 判断属性的值是否为null
            boolean isNullValue = this.isNull(eoField.getEoAttribute());
            if (!isNullValue) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(eoField.getTableColumn()).append("=#{updateMap.").append(eoField.getEoAttribute()).append('}');
            }
        }

        return sb.toString();
    }

    /**
     * 构建where条件语句
     *
     * @return
     */
    public String returnWhereColumnNames() {
        this.calcColumnList();
        Set<String> columnsList = new HashSet();
        StringBuilder sb = new StringBuilder();

        if (this.condition != null) {
            sb.append(getConditionWhere(this.condition, FastDateFormat.getInstance("yyyy-MM-dd"), getClass()));
        }

        if (!columnsList.contains("dr")) {
            if (!"".equals(sb.toString())) {
                sb.append(" and ");
            }

            if (this.getDr() == 1) {
                sb.append("dr").append("=1");
            } else {
                sb.append("dr").append("=0");
            }
        }

        Iterator<EoField> iterator = this.getColumnList().iterator();

        while (iterator.hasNext()) {
            EoField eoField = iterator.next();
            String attribute = eoField.getEoAttribute();
            if (!this.isWhereNull(attribute) && !columnsList.contains(attribute)) {
                if (!"".equals(sb.toString())) {
                    sb.append(" and ");
                }

                sb.append(eoField.getTableColumn()).append("=#{").append(eoField.getEoAttribute()).append("}");
            }
        }

        String sql = sb.toString();
        return sql;
    }

    /**
     * 生成Condition条件类的sql wehere语句
     *
     * @param condition
     * @param df
     * @param aClass
     * @return
     */
    private String getConditionWhere(Condition condition, FastDateFormat df, Class<? extends BaseEo> aClass) {
        return SqlConditionHelper.buildCondition(condition, df, getClass());
    }

    /**
     * 生成更新操作的where语句
     *
     * @return
     */
    public String returnUpdateWhereColumnNames() {
        return returnWhereConditionColumnNames();
    }

    /**
     * @return
     */
    public String returnWhereConditionColumnNames() {
        FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return getConditionWhere(this.condition, fdf, this.getClass());
    }

    /**
     * 生成排序的sql语句
     *
     * @return
     */
    public String resultSort() {
        StringBuilder sb = new StringBuilder();
        String autoHumpCol = System.getProperty(CommonCanstant.DB_AUTO_HUMP_MYSQL_COLUMN);
        if (CollectionUtils.isNotEmpty(this.sortList)) {
            Collections.sort(sortList);
            String fileName = null;
            for (Sort sort : sortList) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                if (CommonCanstant.YES_STR.equals(autoHumpCol)) {
                    fileName = CamelToUnderline.camelToUnderline(sort.getProperty());
                } else {
                    fileName = sort.getProperty();
                }

                sb.append(fileName).append(" ").append(sort.getOrder().getCode());
            }
        }

        if ("".equals(sb.toString())) {
            sb.append("id").append(" desc");
        }

        return sb.toString();
    }

    private List<EoField> getColumnList() {
        return mapColumns.get(this.getClass());
    }


    public static BaseEo build(Class<? extends BaseEo> clazz) throws Exception {
    	return clazz.newInstance();
    }

    @Override
    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int length = fields.length;

        for (int index = 0; index < length; ++index) {
            Field f = fields[index];
            if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
                Object value = null;

                try {
                    f.setAccessible(true);
                    value = f.get(this);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (value != null) {
                    sb.append(f.getName()).append('=').append(value).append(',');
                }
            }
        }

        sb.append(']');
        return sb.toString();
    }

    public String getAsyncRequestId() {
        return asyncRequestId;
    }

    public void setAsyncRequestId(String asyncRequestId) {
        this.asyncRequestId = asyncRequestId;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Boolean getDbNullFlag() {
        return dbNullFlag;
    }

    public void setDbNullFlag(Boolean dbNullFlag) {
        this.dbNullFlag = dbNullFlag;
    }

    public String[] getQueryTableColumnNames() {
        return queryTableColumnNames;
    }

    public void setQueryTableColumnNames(String[] queryTableColumnNames) {
        this.queryTableColumnNames = queryTableColumnNames;
    }

    public Long getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(Long createPersonId) {
        this.createPersonId = createPersonId;
    }

    public Long getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Long updatePersonId) {
        this.updatePersonId = updatePersonId;
    }


    /**
     * 获取getXxxx()方法对应table column名
     *
     * @param methodFunc 类名::getXxx  注意不是getXxx()
     * @param <T>
     * @param <R>
     * @return
     */
    public <T, R> String getColumnName(BaseFunction<T, R> methodFunc) {
        String column = null;
        try {
            // Serializable 有两个标记接口方法writeReplace和readResolve
            Method writeReplace = methodFunc.getClass().getDeclaredMethod(SERIALIZED_MARKER_METHOD_NAME);
            writeReplace.setAccessible(true);
            java.lang.invoke.SerializedLambda serializedLambda = (java.lang.invoke.SerializedLambda) writeReplace.invoke(methodFunc);
            String implMethodName = serializedLambda.getImplMethodName();
            String implClassName = serializedLambda.getImplClass();
            String propertyName = implMethodName.replace(GET_METHOD_NAME_PREFIX, "");
            Class<?> clazz = getClass().getClassLoader().loadClass(implClassName.replace(SERIALIZED_LAMBDA_IMPL_CLASS_SPLIT_CHAR,
					CLASS_NAME_SPLIT_CHAR));

            calcColumnList();

            List<EoField> eoFieldList = mapColumns.get(clazz);
            if (CollectionUtils.isNotEmpty(eoFieldList)) {
                for (EoField eoField : eoFieldList) {
                    if (eoField.getEoAttribute().equalsIgnoreCase(propertyName)) {
                        column = eoField.getTableColumn();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            new BizException("get column name happend Exception.Reason:" + e.getMessage());
        }

        return column;
    }
}
