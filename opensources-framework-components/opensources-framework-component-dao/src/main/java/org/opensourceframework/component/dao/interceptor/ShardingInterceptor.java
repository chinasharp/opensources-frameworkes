package org.opensourceframework.component.dao.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.component.dao.annotation.ShardingTable;
import org.opensourceframework.component.dao.algorithm.HashModTable;
import org.opensourceframework.component.dao.algorithm.HashingTable;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.*;

/**
 * Mybatis拦截器 根据分表注解替换主表名为分表名
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/2/22 10:19 AM
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class ShardingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(ShardingInterceptor.class);
    private static final int HASH_MOD = 1;
    private static final int HASH_HASHING = 2;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // TODO Auto-generated method stub
        BoundSql boundSql = null;
        MetaObject metaObject = null;
        MappedStatement mappedStatement = null;
        Object target = invocation.getTarget();
        if (target instanceof RoutingStatementHandler) {
            RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) target;
            metaObject = SystemMetaObject.forObject(routingStatementHandler);
            StatementHandler statementHandler = (StatementHandler) metaObject.getValue("delegate");
            metaObject = SystemMetaObject.forObject(statementHandler);

            mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
            boundSql = statementHandler.getBoundSql();
            //获取对应的Mapper类
            Class<?> mapperClass = Class.forName(mappedStatement.getId().substring(0,mappedStatement.getId().lastIndexOf(".")));
            //获取对应EO
            Class<?> eoClass = getEoClass(mapperClass);
            if(eoClass.isAnnotationPresent(ShardingTable.class) && eoClass.isAnnotationPresent(Table.class)){
                String maintable = eoClass.getAnnotation(Table.class).name();
                ShardingTable rdsSharding = eoClass.getAnnotation(ShardingTable.class);
                int algorithm = rdsSharding.algorithm();
                int tableTotal = rdsSharding.tableCount();
                String shardingProperty = rdsSharding.property();
                String tableNumParam = rdsSharding.tableNumParam();
                if(org.apache.commons.lang3.StringUtils.isBlank(shardingProperty)){
                    logger.error("{} not find hash key!" , maintable);
                }else {
                    String subTableName = null;

                    Object shardingValue = getShardingValue(boundSql , shardingProperty);
                    if (shardingValue != null) {
                        //有查分键的,使用hash算法得出子表名
                        if(HASH_MOD == algorithm) {
                            //hash mod算法
                            subTableName = HashModTable.getSplitTableName(maintable, shardingValue, tableTotal);
                        }else{
                            //hashing算法
                            subTableName = HashingTable.getSplitTableName(maintable, shardingValue, tableTotal);
                        }
                    } else {
                        Object tableNumber = getTableNumber(boundSql , tableNumParam);
                        //没有分表拆分键 根据分表的序号确定表名
                        if(tableNumber != null){
                            subTableName = maintable + "_" + tableNumber;
                        }
                    }

                    if(org.apache.commons.lang3.StringUtils.isNotBlank(subTableName)){
                        String sql = boundSql.getSql();
                        //将表名替换为子表名
                        sql = sql.replaceAll(maintable, subTableName);

                        metaObject = SystemMetaObject.forObject(boundSql);
                        metaObject.setValue("sql", sql);
                    }else{
                        logger.error("Unable to obtain subTableName , exec canceled. caseby: {} splitKey's value is null or tableNumParam's value {} is null.", maintable, tableNumParam);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private Object getShardingValue(BoundSql boundSql , String shardingProperty){
        Object parameterObj = boundSql.getParameterObject();
        //将参数转换为Map结构
        Map<String , String> parameterMap = json2Map(JSON.toJSONString(parameterObj));
        Object shardingValue = parameterMap.get(shardingProperty);
        if(shardingValue == null && parameterObj instanceof BaseEo){
            Condition condition = ((BaseEo)parameterObj).getCondition();
            if(condition != null && MapUtils.isNotEmpty(condition.getPropertyMap())){
                shardingValue = condition.getPropertyMap().get(shardingProperty);
            }
        }
        return shardingValue;
    }

    private Object getTableNumber(BoundSql boundSql , String tableNumParam ){
        Object parameterObj = boundSql.getParameterObject();
        //将参数转换为Map结构
        Map<String , String> parameterMap = json2Map(JSON.toJSONString(parameterObj));
        return parameterMap.get(tableNumParam);
    }

    @Override
    public Object plugin(Object target) {
        // TODO Auto-generated method stub
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target,this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取Eo class
     *
     * @param eoMapper
     * @return
     */
    private Class<?> getEoClass(Class<?> eoMapper) {
        Class entityClass = getGenericClass(eoMapper);
        if (entityClass != null) {
            String eoName = entityClass.getPackage().getName() + "." + StringUtils.delete(entityClass.getSimpleName(), "Eo") + "ExtEo";

            try {
                Class extClass = Class.forName(eoName);
                entityClass = extClass;
            } catch (ClassNotFoundException exception) {
            }
        }

        return entityClass;
    }

    /**
     * 获取接口的泛型类型，如果不存在则返回null
     *
     * @param clazz
     * @return
     */
    private Class<?> getGenericClass(Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();
        if(t == null){
            t = clazz.getGenericInterfaces()[0];
        }
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            return ((Class<?>) p[0]);
        }
        return null;
    }


    /**
     * 复杂json字符串转换为Map,包含数组时value为List
     */
    private static Map<String, String> json2Map(String json) {
        LinkedMap map = new LinkedMap();
        JSONObject jsonObject = JSONObject.parseObject(json);
        return populate(jsonObject, map);
    }

    /**
     * 将嵌套json结构转换成单层结构的Map,层之间重复的属性覆盖
     */
    private static Map<String, String> populate(JSONObject jsonObject, Map<String, String> map) {
        for (Iterator iterator = jsonObject.entrySet().iterator(); iterator.hasNext(); ) {
            String entryStr = String.valueOf(iterator.next());
            String key = entryStr.substring(0, entryStr.indexOf("="));

            Class clazz = jsonObject.get(key).getClass();
            if (clazz.equals(JSONObject.class)) {
                HashMap _map = new HashMap();
                map.putAll(populate(jsonObject.getJSONObject(key), _map));
            } else if (clazz.equals(JSONArray.class)) {
                ArrayList list = new ArrayList();
                map.putAll(populateArray(jsonObject.getJSONArray(key), list));
            } else {
                map.put(key, jsonObject.get(key).toString());
            }
        }

        return map;
    }

    /**
     * 如果是键对应数组,则返回一个list到上级的map里
     */
    private static Map populateArray(JSONArray jsonArray, List list) {
        HashMap map = new HashMap();
        for (int i = 0; i < jsonArray.size(); i++) {
            Class clazz = jsonArray.get(i).getClass();
            if (clazz.equals(JSONArray.class)) {
                ArrayList _list = new ArrayList();
                list.add(_list);
                populateArray(jsonArray.getJSONArray(i), _list);
            } else if (clazz.equals(JSONObject.class)) {
                HashMap _map = new HashMap();
                list.add(_map);
                map.putAll(populate(jsonArray.getJSONObject(i), _map));
            } else {
                list.add(jsonArray.get(i));
            }
        }

        return map;
    }

}
