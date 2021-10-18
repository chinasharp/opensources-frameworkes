package org.opensourceframework.component.seclog.event;

import org.opensourceframework.component.seclog.config.SecLogTopicConfig;
import org.opensourceframework.component.seclog.helper.ConvertHelper;
import org.opensourceframework.component.seclog.vo.SecLogConvertVo;
import org.opensourceframework.component.seclog.vo.SecLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.api.IMessageSender;

import lombok.extern.slf4j.Slf4j;

/**
 * 审计日志异步监听
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Slf4j
public class SecLogListener {

    @Autowired
    private IMessageSender sender;

    @Autowired
    private SecLogTopicConfig secLogTopicConfig;


    public SecLogListener() {

    }

    /**
     * 异步发送mq消息
     * @param event
     */
    @Async("taskExecutor")
    @Order
    @EventListener(SecLogEvent.class)
    public void sendSecLog(SecLogEvent event) {
        // 获取参数
        SecLogConvertVo convertVo = (SecLogConvertVo) event.getSource();
        // 审计日志组合转换
        SecLogVo secLogVo = ConvertHelper.convertSecLogVo(convertVo);
        // 自定义日志处理
        // String logContext = ContextHelper.handleContext(joinPoint, context, args);

        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent(JSON.toJSONString(secLogVo));
        // 发送mq消息
        sender.sendMessage(secLogTopicConfig.getTopic(), secLogTopicConfig.getTag(), messageVo);
        log.info("[审计日志]发送成功：" + JSON.toJSONString(secLogVo));
    }
}
