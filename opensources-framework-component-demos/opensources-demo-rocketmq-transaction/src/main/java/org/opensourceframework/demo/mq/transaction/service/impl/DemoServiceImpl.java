package org.opensourceframework.demo.mq.transaction.service.impl;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.demo.mq.transaction.service.IDemoService;
import org.opensourceframework.demo.mq.transaction.dao.DemoDao;
import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
import org.opensourceframework.demo.mq.transaction.mq.sender.DemoSender;
import org.opensourceframework.demo.mq.transaction.service.listenter.DemoSaveTransactionListener;
import org.opensourceframework.component.mq.vo.TransListenterInfo;
import org.opensourceframework.component.mq.vo.TransMessageVo;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service实现示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service("demoService")
public class DemoServiceImpl extends BizBaseServiceImpl<DemoEo, Long> implements IDemoService {
	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	@Autowired
	private DemoDao demoDao;

	@Autowired
	private DemoSender demoSender;

	@Autowired
	private DemoSaveTransactionListener demoSaveTransactionListener;

	/**
	 * 获取操作的Dao Bean
	 */
	@Override
	public BizBaseDao<DemoEo, Long> getBizDao() {
		return demoDao;
	}

	/**
	 * MQ 消息事务保存 使用系统默认实现的TransactionListenter
	 *
	 * @param eo
	 */
	@Override
	public DemoEo saveByDefMQTransaction(DemoEo eo) {
		if(eo.getId() == null){
			eo.setId(SnowFlakeId.nextId(0L ,0L));
		}
		TransListenterInfo listenterInfo = new TransListenterInfo();

		// 本地事务的执行bean
		listenterInfo.setListenerInvokeBeanId("demoService");

		// 本地事务执行的方法 此例为执行DemoServiceImpl.saveOrUpdate
		listenterInfo.setExecInvokeMethod("save");
		listenterInfo.setExecParameterTypes(new Class[]{DemoEo.class});
		listenterInfo.setExecArgs(new Object[]{eo});

		// 严重执行成功的方法 此例通过查询数据库记录严重是否事务执行成功 DemoServiceImpl.checkSaveSuccess
		listenterInfo.setCheckInvokeMethod("checkSaveSuccess");
		listenterInfo.setCheckParameterTypes(new Class[]{Long.class});
		listenterInfo.setCheckArgs(new Object[]{eo.getId()});
		TransMessageVo transMessageVo = new TransMessageVo(listenterInfo);

		MessageResponse response = demoSender.sendTransactionMessage(transMessageVo);
		logger.info("saveByDefMQTransaction resp:{}" , JSON.toJSONString(response));

		return eo;
	}

	/**
	 * MQ 消息事务保存 使用自定义实现TransactionListener:demoSaveTransactionListener
	 *
	 * @param eo
	 */
	@Override
	public DemoEo saveByCustomMQTransaction(DemoEo eo) {
		TransMessageVo transMessageVo = new TransMessageVo();
		if(eo.getId() == null){
			eo.setId(SnowFlakeId.nextId(0L ,0L));
		}
		transMessageVo.setMsgContent(JSON.toJSONString(eo));
		MessageResponse response = demoSender.sendTransactionMessage(transMessageVo , demoSaveTransactionListener);
		logger.info("saveByCustomMQTransaction resp:{}" , JSON.toJSONString(response));
		return eo;
	}

	/**
	 * 检查是否存成功
	 *
	 * @param id
	 * @return
	 */
	public Boolean checkSaveSuccess(Long id){
		Boolean flag = false;
		DemoEo eo = super.findById(id);
		if(eo != null){
			flag = true;
		}
		return flag;
	}


	/**
	 * 保存或更新
	 *
	 * @param eo
	 */
	@Override
	public DemoEo save(DemoEo eo) {
		demoDao.insert(eo);
		return eo;
	}
}
