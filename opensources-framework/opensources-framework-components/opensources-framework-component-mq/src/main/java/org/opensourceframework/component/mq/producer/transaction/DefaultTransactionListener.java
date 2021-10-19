package org.opensourceframework.component.mq.producer.transaction;

import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.helper.SerializeHelper;
import org.opensourceframework.component.mq.helper.SpringHelper;
import org.opensourceframework.component.mq.vo.TransListenterInfo;
import org.opensourceframework.component.mq.vo.TransMessageVo;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 默认的Transaction Message 本地事务监听类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Service("defaultTransactionListener")
public class DefaultTransactionListener implements TransactionListener {
    @Override
    @Transactional
    public LocalTransactionState executeLocalTransaction(Message message, Object listenerArg) {
        // 从消息体中解析出数据
        TransMessageVo messageVo = (TransMessageVo) SerializeHelper.deSerialize(message.getBody());
        TransListenterInfo listenterInfo = messageVo.getTransListenterInfo();
        validate(listenterInfo);

        String invokeBeanId = listenterInfo.getListenerInvokeBeanId();
        Object invokeBean = SpringHelper.getBean(invokeBeanId);

        String execInvokeMethod = listenterInfo.getExecInvokeMethod();
        Class<?>[] execParameterTypes = listenterInfo.getExecParameterTypes();
        Object[] execArgs = listenterInfo.getExecArgs();

        try {
            ReflectHelper.invokeMethod(invokeBean ,execInvokeMethod , execParameterTypes , execArgs);
        } catch (Throwable e) {
            e.printStackTrace();
            // 表示系统异常，向客户端返回失败，可通过明确的 ROLLBACK 指令进行判断
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        // 返回 UNKNOW 因为此时事务还没有提交
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        // 从消息体中解析出数据
        TransMessageVo messageVo = (TransMessageVo) SerializeHelper.deSerialize(messageExt.getBody());
        TransListenterInfo listenterInfo = messageVo.getTransListenterInfo();
        validate(listenterInfo);

        String invokeBeanId = listenterInfo.getListenerInvokeBeanId();
        Object invokeBean = SpringHelper.getBean(invokeBeanId);

        String checkInvokeMethod = listenterInfo.getCheckInvokeMethod();
        Class<?>[] checkParameterTypes = listenterInfo.getCheckParameterTypes();
        Object[] checkArgs = listenterInfo.getCheckArgs();

        // 根据返回的bean 类型
        Object invokeRes = ReflectHelper.invokeMethod(invokeBean ,checkInvokeMethod , checkParameterTypes , checkArgs);
        if(!(invokeRes instanceof Boolean)){
            throw new MQException("Transaction Message CheckInvokeMethod's return Type must is Boolean");
        }
        Boolean resFlag = true;
        if(invokeRes == null){
            resFlag = false;
        }else {
            resFlag = (Boolean) invokeRes;
        }
        if(resFlag) {
            return LocalTransactionState.COMMIT_MESSAGE;
        } else {
            return LocalTransactionState.UNKNOW;
        }
    }

    private void validate(TransListenterInfo listenterInfo){
        Assert.notNull(listenterInfo , "Not Found TransListenterInfo Config!");
        Assert.hasLength(listenterInfo.getListenerInvokeBeanId() , "Not Found TransListenterInfo's property listenerInvokeBean");
        Assert.hasLength(listenterInfo.getExecInvokeMethod() , "Not Found TransListenterInfo's property execInvokeMethod");
        Assert.hasLength(listenterInfo.getCheckInvokeMethod() , "Not Found TransListenterInfo's property checkInvokeMethod");
    }
}
