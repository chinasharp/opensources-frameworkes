package org.opensourceframework.demo.mq.transaction.service.listenter;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.demo.mq.transaction.dao.DemoDao;
import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
import org.opensourceframework.component.mq.helper.SerializeHelper;
import org.opensourceframework.component.mq.vo.TransMessageVo;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义TransactionListenter
 *
 */
@Service
public class DemoSaveTransactionListener implements TransactionListener {
    @Autowired
    private DemoDao demoDao;

    @Override
    @Transactional
    public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
        // 从消息体中解析出数据
        TransMessageVo messageVo = (TransMessageVo) SerializeHelper.deSerialize(message.getBody());
        DemoEo eo = JSON.parseObject(messageVo.getMsgContent() , DemoEo.class);
        try {
            demoDao.insert(eo);
        } catch (Throwable e) {
            e.printStackTrace();
            // 表示系统异常，向客户端返回失败，可通过明确的 ROLLBACK 指令进行判断
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        // 返回 UNKNOW 因为此时事务还没有提交
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt message) {
        TransMessageVo messageVo = (TransMessageVo) SerializeHelper.deSerialize(message.getBody());
        DemoEo eo = JSON.parseObject(messageVo.getMsgContent() , DemoEo.class);
        // 如果存在，说明本地事务执行成功，可以提交
        if(demoDao.findById(eo.getId()) != null ) {
            return LocalTransactionState.COMMIT_MESSAGE;
        } else {
            return LocalTransactionState.UNKNOW;
        }
    }
}
