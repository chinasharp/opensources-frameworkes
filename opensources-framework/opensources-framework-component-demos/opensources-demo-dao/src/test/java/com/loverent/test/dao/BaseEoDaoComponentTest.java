package com.loverent.test.dao;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.Restrictions;
import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.demo.mybatis.MybatisDemoBoot;
import org.opensourceframework.demo.mybatis.biz.dao.DemoUserDao;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoUserEo;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseEoDaoComponentTest {
	private static Logger logger = LoggerFactory.getLogger(BaseEoDaoComponentTest.class);
	@Autowired
	private DemoUserDao demoUserDao;

	@Autowired
	private BizBaseDao bizBaseDao;

	/**
	 * 分页排序
	 */
	@Test
	public void pageSort(){
		DemoUserEo queryEo = new DemoUserEo();
		//queryEo.desc("createTime");
		queryEo.asc(queryEo.getColumnName(DemoUserEo::getAccount));
		PageInfo<DemoUserEo> eoPageInfo =  demoUserDao.findPage(queryEo , 1 , 20 );
		for(DemoUserEo eo : eoPageInfo.getList()) {
			logger.info(eo.getAccount());
		}
	}

	/**
	 * 物理删除
	 */
	@Test
	public void delete(){
		DemoUserEo conditionEo = new DemoUserEo();
		conditionEo.setId(1037648928618980352L);
		logger.info("query data:{}" , JSON.toJSONString(demoUserDao.findOne(conditionEo)));

		demoUserDao.delete(conditionEo);
		logger.info("delete after. query data:{}" , JSON.toJSONString(demoUserDao.findOne(conditionEo)));
	}

	/**
	 * 逻辑删除
	 */
	@Test
	public void logicDelete(){
		DemoUserEo conditionEo = new DemoUserEo();
		conditionEo.setId(1054252946810086400L);
		DemoUserEo demoUserEo = demoUserDao.findOne(conditionEo);
		if(demoUserEo != null) {
			logger.info("query data property dr:{}", demoUserDao.findOne(conditionEo).getDr());
			demoUserDao.deleteLogic(conditionEo);
		}
	}

	/**
	 * 根据主键删除
	 */
	@Test
	public void deleteById(){
		demoUserDao.deleteById(1054252946810086400L);

		DemoUserEo conditionEo = new DemoUserEo();
		conditionEo.setId(1054252946810086400L);
		logger.info("exec deleteById done. query data:{}" , demoUserDao.findOne(conditionEo));
	}

	/**
	 * 根据主键逻辑删除
	 */
	@Test
	public void logicDeleteById(){
		DemoUserEo conditionEo = new DemoUserEo();
		conditionEo.setId(1054252946776531968L);
		logger.info("exec logicDeleteById before. query data:{}" , demoUserDao.findOne(conditionEo));

		demoUserDao.deleteLogicById(1054252946776531968L);
		logger.info("exec logicDeleteById done. query data:{}" , demoUserDao.findOne(conditionEo));
	}

	/**
	 * 保存
	 */
	@Test
	public void insert() {
		DemoUserEo eo = new DemoUserEo();
		eo.setName("name_insert");
		eo.setAccount("account_insert_20200106");
		eo.setAddress("address_insert");
		eo.setMemberCardNo("card_no_insert");
		eo.setPhone("phone_insert");
		eo.setCreatePerson("System");
		eo.setUpdatePerson("System");
		eo.setTestDate(new Date());

		demoUserDao.insert(eo);

		logger.info("eo pkid:{}", eo.getId());
	}

	/**
	 * 批量保存
	 */
	@Test
	public void batchInsert(){
		List<DemoUserEo> eoList = Lists.newArrayList();
		for(int i = 0 ; i < 50; i++){
			DemoUserEo eo = new DemoUserEo();
			eo.setName("name_insert_" + i);
			eo.setAccount("account_insert_" + i);
			eo.setAddress("address_insert_" + i);
			eo.setMemberCardNo("card_no_insert_" + i);
			eo.setPhone("phone_insert_" + i);
			eo.setApplicationId(111L);
			eo.setCreatePerson("System_" + i);
			eo.setUpdatePerson("System_" + i);
			eoList.add(eo);
		}
		demoUserDao.insertBatch(eoList);
	}

	/**
	 * 根据条件更新
	 */
	@Test
	public void updateByCondition(){
		DemoUserEo updateEo = new DemoUserEo();
		updateEo.setTestDate(new Date());
		updateEo.setCondition(Restrictions.eq(updateEo.getColumnName(DemoUserEo::getId) , 1074612757468685312L));
		demoUserDao.updateByCondition(updateEo);

		updateEo = new DemoUserEo();
		updateEo.setTestDate(new Date());
		try {
			updateEo.setCondition(Restrictions.eq(updateEo.getColumnName(DemoUserEo::getUpdateTime)  ,
					FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2019-12-31 15:16:17")));
			updateEo.setCondition(
					Restrictions.gte(updateEo.getColumnName(DemoUserEo::getUpdateTime)  ,
						FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2019-1-31 15:16:17"))
					.add(Restrictions.lte(updateEo.getColumnName(DemoUserEo::getUpdateTime)  ,
							FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2019-12-31 15:16:17"))));


			demoUserDao.updateByCondition(updateEo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 实体对象为查询条件
	 */
	@Test
	public void queryUser() {
		DemoUserEo eo = new DemoUserEo();
		eo.setApplicationId(111L);

		List<DemoUserEo> eoList = demoUserDao.findList(eo , new String[]{"name"});
		logger.info("query res:{}", JSON.toJSONString(eoList));
	}

	/**
	 * 根据主键ids查询
	 */
	@Test
	public void findByIds(){
		Long[] pkIds = new Long[]{1037648928618980352L , 1037648928618980312L};
		demoUserDao.findByIds(pkIds);
	}

	/**
	 * 根据id查询 可指定返回的字段
	 */
	@Test
	public void findColumnById(){
		demoUserDao.findColumnById(DemoUserEo.class ,1037648928618980352L , new String[]{"name"});
	}

	/**
	 * 根据ids查询 可指定返回的字段
	 */
	@Test
	public void findColumnByIds(){
		Long[] pkIds = new Long[]{1037648928618980352L , 1037648928618980312L};
		demoUserDao.findColumnByIds(DemoUserEo.class ,pkIds , new String[]{"name"});
	}

	/**
	 * 根据实体对象中部位空的属性值为条件查询
	 */
	@Test
	public void findOne(){
		DemoUserEo eo = new DemoUserEo();
		eo.setAccount("sharding_" + 100);

		demoUserDao.findOne(eo);
		demoUserDao.findOne(eo , new String[]{"name"});
	}

	/**
	 * 根据实体对象中部位空的属性值为条件查询
	 */
	@Test
	public void findList(){
		DemoUserEo eo = new DemoUserEo();
		eo.setAccount("sharding_" + 100);

		demoUserDao.findList(eo);
		demoUserDao.findList(eo , new String[]{"name"});
	}

	@Test
	public void conditionTest(){
		Condition condition = Restrictions.eq("name" , "123")
				.add(Restrictions.or(Restrictions.eq("account" , "abc") , Restrictions.eq("phone" , "1212")));
		DemoUserEo eo = new DemoUserEo();
		eo.setCondition(condition);
		demoUserDao.findByCondition(eo);
	}

	/**
	 * 时间条件查询
	 */
	@Test
	public void conditionDateTest(){
		DemoUserEo eo = new DemoUserEo();
		// 大于等于
		Condition condition = Restrictions.gte(eo.getColumnName(DemoUserEo::getUpdateTime) , DateHelper.parseDate("2021-09-01 00:00:00" , DateHelper.YMD24H_DATA));
		condition = condition.add(Restrictions.lte(eo.getColumnName(DemoUserEo::getUpdateTime) , DateHelper.parseDate("2021-09-20 00:00:00" , DateHelper.YMD24H_DATA)));
		eo.setCondition(condition);
		demoUserDao.findByCondition(eo);
	}
}
