package org.opensourceframework.component.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.component.mq.config.MqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 生产者与消费者基础接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
    protected ScheduledExecutorService executor;
    protected MqConfig mqConfig;
    protected boolean durable = true;
    protected int prefetchCount = 1;
    protected boolean autoAck = false;

    public BaseService() {
    }

    protected void start(MqConfig mqConfig) {
    }

    protected void asyncWaitAndReconnect(final MqConfig mqConfig) {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.schedule(new Runnable() {
            @Override
            public void run() {
                BaseService.this.start(mqConfig);
            }
        }, 30L, TimeUnit.SECONDS);
    }

    public void setMqConfigVo(MqConfig mqConfig) {
        this.mqConfig = mqConfig;
    }

    protected int getWaiTime(int retryCount) {
        int waitTime = 1;
        switch(retryCount) {
            case 1:
                waitTime = retryCount * 30;
                break;
            case 2:
                waitTime = retryCount * 60;
                break;
            case 3:
                waitTime = retryCount * 100;
                break;
            case 4:
                waitTime = retryCount * 150;
                break;
            case 5:
                waitTime = retryCount * 240;
                break;
            case 6:
                waitTime = retryCount * 400;
                break;
            case 7:
                waitTime = retryCount * 500;
                break;
            case 8:
                waitTime = retryCount * 600;
                break;
            case 9:
                waitTime = retryCount * 700;
        }

        return waitTime;
    }

    protected String getMessageId(Object message) {
        String messageId = null;
        try{
            JSONObject jsonObject = JSON.parseObject(message.toString());
            Object obj = jsonObject.getString("messageId");
            if(obj != null){
                messageId = obj.toString();
            }
        }catch (Exception e){
        }

        if(messageId == null){
            messageId = SnowFlakeId.nextId(null ,null).toString();
        }
        return messageId;
    }

    protected String getSubExpression(Object[] tags) {
        StringBuilder sb = new StringBuilder();
        if (tags != null) {
            for(int i = 0; i < tags.length; ++i) {
                if (i == tags.length - 1) {
                    sb.append(tags[i]);
                } else {
                    sb.append(tags[i]).append("||");
                }
            }
        }

        return sb.toString();
    }
}
