package com.loverent.test.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.Restrictions;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.demo.mybatis.MybatisDemoBoot;
import org.opensourceframework.demo.mybatis.biz.dao.DemoShardingUserDao;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShardingDaoComponentTest {
    private static final Logger logger = LoggerFactory.getLogger(ShardingDaoComponentTest.class);

    @Autowired
    private DemoShardingUserDao demoShardingUserDao;

    /**
     * 分表批量保存  拆分键：account
     */
    @Test
    public void batchShardingInsert() {
        List<DemoShardingUserEo> eoList = Lists.newArrayList();

        DemoShardingUserEo eo;
        for (int i = 0; i < 1000; i++) {
            eo = new DemoShardingUserEo();
            eo.setName("name_" + i);
            eo.setAccount("sharding_" + i);
            eo.setAddress("address" + i);
            eo.setMemberCardNo("card_no_" + i);
            eo.setPhone("phone_" + i);
            eo.setCreatePerson("System");
            eo.setUpdatePerson("System");
            eoList.add(eo);
        }

        eoList = demoShardingUserDao.insertBatch(eoList);

        logger.info("insert res:{}", JSON.toJSONString(eoList));
    }

    /**
     * 根据拆分键查询  拆分键：account
     */
    @Test
    public void findBySharding(){
        List<DemoShardingUserEo> eoList = demoShardingUserDao.findListByShardingKey("sharding_2");
        logger.info("findByShardingKey query data:{}" , JSON.toJSONString(eoList));

        eoList = demoShardingUserDao.findListByShardingKey("sharding_2" , new String[]{"name"});
        logger.info("findByShardingKey tableColumnNames  query data:{}" , JSON.toJSONString(eoList));

        DemoShardingUserEo queryEo = new DemoShardingUserEo();
        queryEo.setAccount("sharding_2");
        eoList = demoShardingUserDao.findListByShardingEo(queryEo);
        logger.info("findListByShardingEo query data:{}" , JSON.toJSONString(eoList));

        eoList = demoShardingUserDao.findListByShardingEo(queryEo , new String[]{"name"});
        logger.info("findListByShardingEo tableColumnNames query data:{}" , JSON.toJSONString(eoList));
    }

    /**
     * 根据拆分键删除  拆分键：account
     */
    @Test
    public void deleteSharding(){
        DemoShardingUserEo queryEo = new DemoShardingUserEo();
        queryEo.setAccount("sharding_2");
        demoShardingUserDao.deleteByShardingEo(queryEo);

        demoShardingUserDao.deleteByShardingKey("sharding_2");

        demoShardingUserDao.deleteLogicByShardingEo(queryEo);

        demoShardingUserDao.deleteLogicByShardingKey("sharding_2");
    }

    /**
     * 根据拆分键查询  拆分键：account
     */
    @Test
    public void queryShardingUser() {
        DemoShardingUserEo eo = new DemoShardingUserEo();
        eo.setAccount("sharding_" + 100);

        List<DemoShardingUserEo> eoList = demoShardingUserDao.findList(eo , new String[0]);
        logger.info("query res:{}", JSON.toJSONString(eoList));
    }

    /**
     * 根据拆分键更新  拆分键：account
     */
    @Test
    public void updateShardingUser() {
        DemoShardingUserEo eo = new DemoShardingUserEo();
        eo.setName("update_name");
        Condition condition = Restrictions.eq("account", "sharding_100");
        eo.setCondition(condition);
        demoShardingUserDao.updateByCondition(eo);

        eo = new DemoShardingUserEo();
        eo.setAccount("sharding_100");
        logger.info("update result:{}", JSON.toJSONString(demoShardingUserDao.findList(eo)));

    }

    /**
     * 不带拆分键的分表查询
     */
    @Test
    public void findShardingDataNoShardingKey(){
        Map<String , Object> whereMap = new HashMap<>();
        //key 为查询条件  value为条件值
        whereMap.put("name" , "name_30");
        whereMap.put("phone" , "phone_30");

        //不设置此参数 系统默认为1000
        whereMap.put(SqlStatementContant.QUERY_DATA_LIMIT_PARAM, 500);
        List<DemoShardingUserEo> eoList = demoShardingUserDao.findShardingDataNoShardingKey(whereMap);
        logger.info("findShardingDataNoShardingKey by map query data:{}" , JSON.toJSONString(eoList));
    }

    /**
     * 不带拆分键的分表查询
     */
    @Test
    public void findShardingDataNoShardingKeyWithCondition(){
        //Condition condition = Restrictions.eq("name" , "name_30").add(Restrictions.eq("phone" , "phone_30"));
        Condition condition = Restrictions.eq("name" , "123")
                .add(Restrictions.or(Restrictions.eq("account" , "abc") , Restrictions.eq("phone" , "1212")));

        List<DemoShardingUserEo> eoList = demoShardingUserDao.findShardingDataNoShardingKey(condition , 500);
        logger.info("findShardingDataNoShardingKey by condition query data:{}" , JSON.toJSONString(eoList));

    }

    /**
     * 不带拆分键的分表更新
     */
    @Test
    public void updateShardingDataNoShardingKey(){
        Map<String , Object> whereMap = new HashMap<>();
        //key 为查询条件  value为条件值
        whereMap.put("name" , "name_57");
        whereMap.put("phone" , "phone_57");
        //logger.info("findShardingDataNoShardingKey query data:{}" ,  demoShardingUserDao.findShardingDataNoShardingKey(whereMap));
        Map<String , Object> updateMap = new HashMap<>();
        updateMap.put("memberCardNo" , "updateShardingDataTest");
        demoShardingUserDao.updateShardingDataNoShardingKey(whereMap , updateMap);
    }

}
