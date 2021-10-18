package org.opensourceframework.component.mq.vo;

import org.opensourceframework.base.vo.MessageVo;

/**
 * MQ Tranaction Message 传值对象
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class TransMessageVo extends MessageVo {
    protected TransListenterInfo transListenterInfo;

    public TransMessageVo(){
        super();
    }
    public TransMessageVo(TransListenterInfo transListenterInfo) {
        this.transListenterInfo = transListenterInfo;
    }

    public TransMessageVo(String bizName, String msgContent, TransListenterInfo transListenterInfo) {
        super(bizName, msgContent);
        this.transListenterInfo = transListenterInfo;
    }

    public TransMessageVo(String bizId, String bizCode, String bizName, String msgContent, TransListenterInfo transListenterInfo) {
        super(bizId, bizCode, bizName, msgContent);
        this.transListenterInfo = transListenterInfo;
    }

    public TransListenterInfo getTransListenterInfo() {
        return transListenterInfo;
    }

    public void setTransListenterInfo(TransListenterInfo transListenterInfo) {
        this.transListenterInfo = transListenterInfo;
    }
}
