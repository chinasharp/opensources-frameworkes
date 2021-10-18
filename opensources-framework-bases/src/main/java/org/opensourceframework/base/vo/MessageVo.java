package org.opensourceframework.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * MQ消息vo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ProtobufClass
@ApiModel(value = "MessageVo", description = "消息基类")
public class MessageVo implements Serializable {
    /**
     * 业务Id 业务的唯一标识
     */
    @ApiModelProperty(name = "bizId", value = "业务Id")
    @NotNull(message = "bizId不能为空")
    protected String bizId;

    /**
     * 业务编码
     */
    @ApiModelProperty(name = "bizCode", value = "业务编码 以及在使用有序消息时分配队列的散列键")
    protected String bizCode;

    /**
     * 业务名称
     */
    @ApiModelProperty(name = "bizName", value = "业务名称")
    protected String bizName;

    /**
     * 业务时间
     */
    @ApiModelProperty(name = "bizDate", value = "业务时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date bizDate;

    @ApiModelProperty(name = "retryCount", value = "重试次数", notes = "用于业务自己记录该消息的处理次数")
    protected Integer retryCount;

    /**
     * 发送的消息内容
     */
    @ApiModelProperty(name = "msgContent", value = "发送文本数据的消息(包括base64的字符串的文件数据),json格式字符串")
    @NotNull(message = "msgContent不能为空")
    protected String msgContent;

    /**
     * 消息唯一标识
     */
    protected String messageId;

    /**
     * 附加属性
     */
    @Protobuf(fieldType = FieldType.MAP)
    protected Map<String, String> extendMap = Maps.newHashMap();

    public MessageVo() {
    }

    public MessageVo(String msgContent) {
        this.msgContent = msgContent;
    }

    public MessageVo(String bizName, @NotNull(message = "msgContent不能为空") String msgContent) {
        this.bizName = bizName;
        this.msgContent = msgContent;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public MessageVo(@NotNull(message = "bizId不能为空") String bizId, String bizCode, String bizName,
                     @NotNull(message = "msgContent不能为空") String msgContent) {
        this.bizId = bizId;
        this.bizCode = bizCode;
        this.bizName = bizName;
        this.msgContent = msgContent;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getExtendMap() {
        return extendMap;
    }

    public void setExtendMap(Map<String, String> extendMap) {
        this.extendMap = extendMap;
    }

    public Date getBizDate() {
        return bizDate;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
    }
}
