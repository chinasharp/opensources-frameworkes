package org.opensourceframework.component.redis.cache.service;

import com.alibaba.fastjson.TypeReference;
import org.opensourceframework.base.cache.ICacheService;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.component.redis.cache.queue.IRedisSubProcessor;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  redis操作接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/3/12 4:54 PM
 */
public interface IRedisCacheService extends ICacheService {
    /**
     * 原生mset方法不可设置过期时间 故使用循环写入  多数据(大于5个)写入禁止使用此方法(String结构)
     *
     * @param dataMap  cache key:map key , cache value : map value
     * @param expireTime 过期时间,永不过期传0
     */
    void mset(Map<String, String> dataMap, Integer expireTime);

    /**
     * 移除 key 的过期时间,持久保持(String结构)
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param value 缓存的对象
     * @return true/false
     */
    Boolean setPersistCache(String group, String key, Object value);

    /**
     * 批量查询缓存(String结构)
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param keyList key列表
     * @param clazz   缓存转为对象的类型
     * @param <T>
     * @return 对象列表
     */
    <T> List<T> mget(String group , Collection<String> keyList, Class<T> clazz);

    /**
     * 批量查询缓存(String结构)
     *
     * @param keyList key列表
     * @param clazz 缓存转为对象的类型
     * @param <T>
     * @return 对象列表
     */
    <T> List<T> mget(Collection<String> keyList, Class<T> clazz);

    /**
     * 自增(String结构)
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param expireTime  0不过期 单位秒
     * @return 自增后的值
     */
    Long incr(String group , String key, Integer expireTime);

    /**
     * 自增(String结构)
     *
     * @param key 缓存key
     * @param expireTime 0不过期 单位秒
     * @return 自增后的值
     */
    Long incr(String key, Integer expireTime);

    /**
     * 自增(String结构)
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param incrNum 自增数
     * @param expireTime  expireTime  0不过期 单位秒
     * @return 自增后的结果
     */
    Long incrBy(String group , String key, Long incrNum, Integer expireTime);

    /**
     * 自增(String结构)
     *
     * @param  key 缓存key
     * @param  incrNum 自增数
     * @param  expireTime  expireTime  0不过期 单位秒
     * @return 自增后的结果
     */
    Long incrBy(String key, Long incrNum, Integer expireTime);

    /**
     * 获取缓存并反序列化为指定类的对象集合(hash结构)
     *
     * @param group  group 指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param field  hash结构中的field
     * @param clazz  value的对象类型
     * @param <T>
     * @return 数据列表
     */
    <T> List<T> hgetList(String group, String key, String field, Class<T> clazz);

    /**
     * 获取缓存并反序列化为指定类的对象集合(hash结构)
     *
     * @param key    缓存key
     * @param field  hash结构
     * @param clazz  value的对象类型
     * @param <T>
     * @return 数据列表
     * @return
     */
    <T> List<T> hgetList(String key, String field, Class<T> clazz);

    /**
     * 获取缓存并反序列化为指定Map结构(hash结构)
     *
     * @param group  group 指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param field  hash结构中的field
     * @param typeReference value转换为Map类型后map的key和value的类型
     * @param <K>
     * @param <V>
     * @return Map对象集合
     */
    <K, V> Map<K, V> hgetMap(String group, String key, String field, TypeReference<Map<K, V>> typeReference);

    /**
     * 获取缓存并反序列化为指定Map结构(hash结构)
     *
     * @param key    缓存key
     * @param field  hash结构中的field
     * @param typeReference value转换为Map类型后map的key和value的类型
     * @param <K>
     * @param <V>
     * @return Map对象集合
     */
    <K, V> Map<K, V> hgetMap(String key, String field, TypeReference<Map<K, V>> typeReference);

    /**
     * 批量缓存hash结构对象(hash结构)
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param mapData  map中的key和value对应 field和value
     * @return true/false
     */
    Boolean hmset(String group , String key, Map<String, String> mapData);

    /**
     * 批量缓存hash结构对象(hash结构)
     *
     * @param key      缓存key
     * @param mapData  map中的key和value对应 field和value
     * @return true/false
     */
    Boolean hmset(String key, Map<String, String> mapData);

    /**
     * 删除field对应的缓存(hash结构）
     *
     * @param group  指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param fields
     * @return
     */
    Boolean hdel(String group , String key, String[] fields);

    /**
     * 删除field对应的缓存(hash结构）
     *
     * @param key 缓存key
     * @param fields
     * @return
     */
    Boolean hdel(String key, String[] fields);

    /**
     * 检查某业务数据是否已缓存(String结构)
     *
     * @param initFlagKey  某业务数据判断是否已缓存的key
     * @param expireSec    key失效时间 秒
     * @return true/false
     */
    Boolean checkHadInit(String initFlagKey, int expireSec);

    /**
     * 批量插入缓存(hash结构）
     *
     * @param key     缓存key
     * @param eoList  存入缓存后,集合中eo的id为field,eo的json格式为value
     */
    void hsetForList(String key, Collection<? extends BaseEo> eoList);

    /**
     * 批量插入缓存(hash结构）
     *
     * @param group  指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param eoList 存入缓存后,集合中eo的id为field,eo的json格式为value
     */
    void hsetForList(String group , String key, Collection<? extends BaseEo> eoList);


    /**
     * 获取hash结构的所有值 value为单个对象(数量超过100禁止使用,会阻塞)(hash结构）
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param clazz value转换的类型
     * @return 列表数据
     */
    <T> List<T> hgetAll(String group ,String key, Class<T> clazz);

    /**
     * 获取hash结构的所有值 value为单个对象(数量超过100禁止使用,会阻塞)(hash结构）
     *
     * @param key    缓存key
     * @param clazz  value转换的类型
     * @param <T>
     * @return 列表数据
     */
    <T> List<T> hgetAll(String key, Class<T> clazz);

    /**
     * 获取hash结构fields对象的值 value为单个对象 当field对应的值不存在时,返回Map中对应的值为null(hash结构）
     *
     * @param key     缓存key
     * @param clazz   value转换的类型
     * @return map结构 key为field value为clazz类型的对象
     */
    <T> Map<String ,T> hgetMapData(String key, List<String> fields, Class<T> clazz);

    /**
     * 根据key获取多个field对应值的缓存列表,每个field对应的value为单个对象,当field对应的值不存在时,返回Map中对应的值为null(hash结构）
     *
     * @param group  指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param fields hash结构中field的集合
     * @param clazz  value转换的类型
     * @param <T>
     * @return map结构 key为field value为clazz类型的对象
     */
    <T> Map<String ,T> hgetMapData(String group , String key, List<String> fields, Class<T> clazz);

    /**
     * 根据key获取多个field对应值的缓存列表,每个field对应的value为List对象 当field对应的值不存在时,返回Map中对应的值为null(hash结构）
     *
     * @param key    缓存key
     * @param clazz  value转换的类型
     * @param fields hash结构中field的集合
     * @return map结构 key为field value为clazz类型的对象列表
     */
    <T> Map<String ,List<T>> hgetListData(String key, List<String> fields, Class<T> clazz);

    /**
     * 获取hash结构fields对象的值 value为List对象 当field对应的值不存在时,返回Map中对应的值为null(hash结构）
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param clazz value转换的类型
     * @param fields hash结构中field的集合
     * @param <T>
     * @return map结构 key为field value为clazz类型的对象列表
     */
    <T> Map<String ,List<T>> hgetListData(String group , String key, List<String> fields, Class<T> clazz);

    /**
     * 获取hash结构fields对象的值 value为List对象(hash结构）
     *
     * @param key   缓存key
     * @param clazz value转换的类型
     * @param fields hash结构中field的集合
     * @return 对象列表
     */
    <T> List<T> hgetListValues(String key, String[] fields, Class<T> clazz);

    /**
     * 获取hash结构fields对象的值 value为List对象(hash结构）
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param clazz value转换的类型
     * @param fields hash结构中field的集合
     * @param <T>
     * @return 对象列表
     */
    <T> List<T> hgetListValues(String group , String key, String[] fields, Class<T> clazz);

    /**
     * 获取hash结构field对象的值 value为List对象(hash结构）
     *
     * @param key     缓存key
     * @param field   hash结构中field
     * @param clazz   value转换的类型
     * @return 对象列表
     */
    <T> List<T> hgetListValues(String key, String field, Class<T> clazz);

    /**
     * 获取hash结构field对象的值 value为List对象(hash结构）
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key   缓存key,存入redis后的真实key为group-key的组合字符串
     * @param field   hash结构中field
     * @param clazz  value的对象类型
     * @return 对象列表
     */
    <T> List<T> hgetListValues(String group , String key, String field, Class<T> clazz);

    /**
     * hash结构自增长(hash结构）
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param field   hash结构中field
     * @param count   传负数则是减
     * @return  自增后的值
     */
    Long hincrBy(String group, String key, String field, Long count);

    /**
     * hash结构自增长(hash结构）
     *
     * @param key     缓存key
     * @param field   hash结构中field
     * @param count   传负数则是减
     * @return  自增后的值
     */
    Long hincrBy(String key, String field, Long count);

    /**
     * nx与ex的组合
     *
     * @param key      缓存key
     * @param value    缓存的值对象
     * @param seconds  过期时间
     * @return true/false
     */
    Boolean add(String key, Object value, Integer seconds);

    /**
     * nx与ex的组合
     *
     * @param group    指定组,可传${spring.application.name}值
     * @param key      缓存key,存入redis后的真实key为group-key的组合字符串
     * @param value    缓存的值对象
     * @param seconds  过期时间
     * @return true/false
     */
    Boolean add(String group , String key, Object value, Integer seconds);

    /**
     * key键不存在,该命令会创建该键及与其关联的List,之后在将参数中的value从左到右依次插入(队列结构)
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param value   写入缓存的list
     * @param seconds 过期时间
     * @param <T>
     */
    <T> void lpush(String group, String key, List<T> value, Integer seconds);

    /**
     * key键不存在,该命令会创建该键及与其关联的List,之后在将参数中的value从左到右依次插入(队列结构)
     *
     * @param key     缓存key
     * @param value   写入缓存的list
     * @param seconds 过期时间
     * @param <T>
     */
    <T> void lpush(String key, List<T> value, Integer seconds);

    /**
     * 从链表的尾部插入参数中给出的value,插入顺序是从左到右依次插入,先进先出(队列结构)
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param value   写入缓存的list
     * @param seconds 过期时间
     * @param <T>
     */
    <T> void rpush(String group, String key, List<T> value, Integer seconds);

    /**
     * 从链表的尾部插入参数中给出的value,插入顺序是从左到右依次插入,先进先出(队列结构)
     *
     * @param key     缓存key
     * @param value   写入缓存的list
     * @param seconds 过期时间
     * @param <T>
     */
    <T> void rpush(String key, List<T> value, Integer seconds);

    /**
     * 返回指定范围内元素的列表(队列结构)
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param from    从form开始 0表示从头开始
     * @param to      到end结束,-1表示最后元素
     * @param clazz   缓存值转换为对象的类型
     * @param <T>
     * @return
     */
    <T> List<T> lget(String group, String key, Integer from, Integer to, Class<T> clazz);

    /**
     * 返回指定范围内元素的列表(队列结构)
     *
     * @param key     缓存key
     * @param from    从form开始 0表示从头开始
     * @param to      到end结束,-1表示最后元素
     * @param clazz   缓存值转换为对象的类型
     * @param <T>
     * @return
     */
    <T> List<T> lget(String key, Integer from, Integer to, Class<T> clazz);

    /**
     * 设置过期
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param seconds 过期时间
     * @return  true/false
     */
    Boolean expire(String group, String key, int seconds);

    /**
     * 设置过期
     *
     * @param key      缓存key
     * @param seconds  过期时间
     * @return true/false
     */
    Boolean expire(String key, int seconds);

    /**
     * 向集合中增加一条记录,如果这个值已存在,这个值对应的权重将被置为新的权重
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param scoreMembers  map中key,value 分别表示要加入的值和权重
     * @return true/false
     */
    Long zadd(String group, String key, Map<String, Long> scoreMembers);

    /**
     * 向集合中增加一条记录,如果这个值已存在,这个值对应的权重将被置为新的权重
     *
     * @param key           缓存的key
     * @param scoreMembers  map中key,value 分别表示要加入的值和权重
     * @return
     */
    Long zadd(String key, Map<String, Long> scoreMembers);

    /**
     * 向集合中增加一条记录,如果这个值已存在,这个值对应的权重将被置为新的权重
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param scoreMembers  map中key,value 分别表示要加入的值和权重
     * @param seconds  过期时间
     * @return
     */
    Long zadd(String group, String key, Map<String, Long> scoreMembers, int seconds);

    /**
     * 向集合中增加一条记录,如果这个值已存在,这个值对应的权重将被置为新的权重
     *
     * @param key           缓存的key
     * @param scoreMembers  map中key,value 分别表示要加入的值和权重
     * @param seconds       过期时间
     * @return
     */
    Long zadd(String key, Map<String, Long> scoreMembers, int seconds);

    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param start   从start开始  0为第一个元素
     * @param end     到end结束    -1为最后一个元素
     * @return
     */
    Set<String> zrange(String group, String key, Long start, Long end);

    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     *
     * @param key     缓存的key
     * @param start   从start开始  0为第一个元素
     * @param end     到end结束    -1为最后一个元素
     * @return
     */
    Set<String> zrange(String key, Long start, Long end);

    /**
     * 获取给定区间的元素,原始按照权重由高到低排序
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param start   从start开始  0为第一个元素
     * @param end     到end结束    -1为最后一个元素
     * @return
     */
    Set<String> zrevrange(String group, String key, Long start, Long end);

    /**
     * 获取给定区间的元素,原始按照权重由高到低排序
     *
     * @param key     缓存的key
     * @param start   从start开始  0为第一个元素
     * @param end     到end结束    -1为最后一个元素
     * @return
     */
    Set<String> zrevrange(String key, Long start, Long end);

    /**
     * 从集合中删除成员
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param members 删除的成员
     * @return
     */
    Long zrem(String group , String key, List<String> members);

    /**
     * 从集合中删除成员
     *
     * @param key     缓存key
     * @param members 删除的成员
     * @return
     */
    Long zrem(String key, List<String> members);

    /**
     * 获取集合中元素的数量
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @return
     */
    Long zcard(String group, String key);

    /**
     * 获取集合中元素的数量
     *
     * @param key     缓存key
     * @return
     */
    Long zcard(String key);

    /**
     * 获取给定值在集合中的权重
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param members 值
     * @return
     */
    Long zscore(String group, String key, String members);

    /**
     * 获取给定值在集合中的权重
     *
     * @param key
     * @param members
     * @return
     */
    Long zscore(String key, String members);

    /**
     * 获取指定权重区间内集合的数量
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param min     最小排序位置
     * @param max     最大排序位置
     * @return
     */
    Long zcount(String group, String key, Long min, Long max);

    /**
     * 获取指定权重区间内集合的数量
     *
     * @param key
     * @param min     最小排序位置
     * @param max     最大排序位置
     * @return
     */
    Long zcount(String key, Long min, Long max);

    /**
     * 游标查找
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param cursor
     * @return
     */
    @Deprecated
    ScanResult<Tuple> zscan(String group, String key, String cursor);

    /**
     * 游标查找
     * @param key
     * @param cursor
     * @return
     */
    @Deprecated
    ScanResult<Tuple> zscan(String key, String cursor);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<Long, String> zrangeWithScores(String group, String key, Long start, Long end);

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<Long, String> zrangeWithScores(String key, Long start, Long end);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<Long, String> zrevrangeWithScores(String group, String key, Long start, Long end);

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<Long, String> zrevrangeWithScores(String key, Long start, Long end);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<String, Long> zrevrangeByScores(String group, String key, Long start, Long end);

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    Map<String, Long> zrevrangeByScores(String key, Long start, Long end);



    @Deprecated
    Set<String> getKeys(String group, String pattern);
    @Deprecated
    Set<String> getKeys(String pattern);
    @Deprecated
    Set<String> hkeys(String group ,String key);
    @Deprecated
    Set<String> hkeys(String key);

    /**
     * 设置占位值
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param value
     * @return true/false
     */
    Boolean setnx(String group, String key, String value);

    /**
     * 设置占位值
     *
     * @param key
     * @param value
     * @return  true/false
     */
    Boolean setnx(String key, String value);

    /**
     * 批量删除缓存不添加group前缀 百万级key时禁止使用,推荐使用delKeysNotWithGroupByScan
     * 
     * @param pattern
     * @return  删除个数
     */
    @Deprecated
    Long delKeys(String pattern);

    /**
     * 使用游标删除key
     *
     * @param pattern
     * @return
     */
    Long delKeysByScan(String pattern);

    /**
     * 批量执行setnx
     *
     * @param dataMap
     * @return
     */
    Long msetnx(Map<String, String> dataMap);

    /**
     * 批量删除
     *
     * @param keyList
     */
    void mdel(List<String> keyList);

    /**
     * Test for existence of a specified field in a hash. <b>Time complexity:</b> O(1)
     * @param key key
     * @param field field
     * @return Return 1 if the hash stored at key contains the specified field. Return 0 if the key is
     *         not found or the field is not present.
     */
    Boolean hexists(final String key, final String field);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param field field
     * @return Return 1 if the hash stored at key contains the specified field. Return 0 if the key is
     *         not found or the field is not present.
     */
    Boolean hexists(String group, String key, String field);

    /**
     * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
     * returned. Note that even keys set with an empty string as value will return "1". Time
     * complexity: O(1)
     * @param key key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    Boolean exists(final String key);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @return
     */
    Boolean exists(String group, String key);

    /**
     * 获取列表缓存的长度(队列结构)
     *
     * @param   key
     * @return
     */
    Long llen(String key);

    /**
     * 向队尾中插入一个字符value(队列结构)
     *
     * @param key 缓存key
     * @param value
     */
    void rpush(String key, String value);

    /**
     * 向队头中插入一个字符value(队列结构)
     *
     * @param key 缓存key
     * @param value
     */
    void lpush(String key, String value);

    /**
     * 根据count移除队列中匹配value的值(队列结构)
     *
     * @param key 缓存key
     * @param count
     * @param value
     * @return
     */
    long lrem(String key, long count, String value);


    /**
     * 获取有序集合里member对应的排名
     *
     * @param key 缓存key
     * @param member
     * @return
     */
    long zRank(String key, String member);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @param elements
     * @return
     */
    Boolean pfadd(String group, String key, String[] elements);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key     缓存key,存入redis后的真实key为group-key的组合字符串
     * @return
     */
    Long pfcount(String group, String key);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param keys    缓存keys,存入redis后的每个真实key为group-key的组合字符串
     * @return
     */
    Long pfcount(String group, String[] keys);

    /**
     * 批量写缓存(String 结构)
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param dataMap map中的key,value 存入缓存后为group-key ,value
     * @return true/false
     */
    Boolean mset(String group, Map<String, String> dataMap);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param destKey
     * @param sourceKeys
     * @return
     */
    Boolean pfmerge(String group, String destKey, String[] sourceKeys);

    /**
     *
     * @param group   指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @return
     */
    Long getToLiveTime(String group, String key);

    /**
     *
     * @param key
     * @return
     */
    Long getToLiveTime(String key);

    /**
     * 订阅消息
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param channel
     * @param processor
     */
    @Deprecated
    void subscribe(String group, String channel, IRedisSubProcessor<String> processor);

    /**
     *
     * @param channel
     * @param processor
     */
    @Deprecated
    void subscribe(String channel, IRedisSubProcessor<String> processor);

    /**
     * 发布消息
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param channel
     * @param message
     */
    @Deprecated
    void publish(String group, String channel, String message);

    /**
     * 发布消息
     * @param channel
     * @param message
     */
    @Deprecated
    void publish(String channel, String message);

    /**
     * 获取指定值在集合中的位置,集合排序从高到低
     *
     * @param key     缓存key
     * @param member  缓存value
     * @return
     */
    Long zrevRank(String key, String member);

    /**
     * 写入缓存
     *
     * @param group 指定组,可传${spring.application.name}值
     * @param key    缓存key,存入redis后的真实key为group-key的组合字符串
     * @param obj
     * @param expireTime 0 为不过期 单位秒
     * @return
     */
    @Override
    Boolean set(String group, String key, Object obj, Integer expireTime);

    /**
     * 执行脚本
     *
     * @param script    脚本
     * @param keys      传入的参数key列表
     * @param args      传入的参数args列表
     * @param clazz     返回值得类型
     * @param <T>
     * @return
     */
    <T> T eval(String script, List<String> keys, List<String> args , Class<T> clazz);

    /**
     * 根据脚本码执行脚本
     *
     * @param shaCode   redis生成的脚本码
     * @param keys      传入的参数key列表
     * @param args      传入的参数args列表
     * @param clazz     返回值得类型
     * @param <T>
     * @return
     */
    <T> T evalSha(String shaCode, List<String> keys, List<String> args , Class<T> clazz);
}
