package org.opensourceframework.base.microservice;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统自定义Context
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ServiceContext implements ServiceConstants {
    private static final ThreadLocal<ServiceContext> LOCAL =
            new ThreadLocal<ServiceContext>() {
                @Override
                protected ServiceContext initialValue() {
                    return new ServiceContext();
                }
            };
    private final Map<String, Object> mapValue;
    private MicroServiceContext microServiceContext;

    private ServiceContext() {
        this.microServiceContext = null;
        this.mapValue = new ConcurrentHashMap();
    }

    public static ServiceContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
    }

    private MicroServiceContext getMicroServiceContext() {
        if (this.microServiceContext == null) {
            String className = "org.opensourceframework.base.microservice.SpringCloudMicroServiceContext";

            try {
                this.microServiceContext = (MicroServiceContext) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this.microServiceContext;
    }

    public void removeAttachmentContext() {
        if (this.microServiceContext != null) {
            this.microServiceContext.removeContext();
        }
    }

    public void set(String key, Object value) {
        if (value == null) {
            this.mapValue.remove(key);
        } else {
            this.mapValue.put(key, value);
        }
    }

    public void remove(String key) {
        this.mapValue.remove(key);
    }

    public Object get(String key) {
        return this.mapValue.get(key);
    }

    public Map<String, Object> getKeys() {
        return this.mapValue;
    }

    public void setAttachment(String key, String attachment) {
        this.set(key, attachment);
        this.getMicroServiceContext().setAttachment(key, attachment);
    }

    public void setAttachment(Map<String , String> attachmentMap){
        this.mapValue.putAll(attachmentMap);
    }

    public String getAttachment(String key) {
        String value = (String) this.get(key);
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment(key);
        }

        return value;
    }

    public void removeAttachment(String key) {
        this.remove(key);
        this.getMicroServiceContext().removeAttachment(key);
    }

    public Map<String, String> getAttachments() {
        return this.getMicroServiceContext().getAttachments();
    }

    public String getAppId() {
        String value = (String) this.get(APP_ID);
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment(APP_ID);
        }

        return value;
    }

    public void setAppId(String appId) {
        this.set(APP_ID, appId);
        this.getMicroServiceContext().setAttachment(APP_ID, appId);
    }

    public String getAppSecret() {
        String value = (String) this.get(APP_SECRET);
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment(APP_SECRET);
        }

        return value;
    }

    public void setAppSecret(String appSecret) {
        this.set("opensourceframework.appSecret", appSecret);
        this.getMicroServiceContext().setAttachment(APP_SECRET, appSecret);
    }

    public Long getRequestUserId() {
        String userId = (String) this.get("req.userId");
        if (userId == null) {
            userId = this.getMicroServiceContext().getAttachment("req.userId");
        }

        return userId != null ? Long.valueOf(userId) : null;
    }

    public String getRequestUserCode() {
        String value = (String) this.get("req.userCode");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.userCode");
        }

        return value;
    }

    public Long getRequestApplicationId() {
        String applicationId = (String) this.get("req.applicationId");
        if (applicationId == null) {
            applicationId = this.getMicroServiceContext().getAttachment("req.applicationId");
        }

        return StringUtils.isEmpty(applicationId) ? null : Long.valueOf(applicationId);
    }

    public String getRequestUserIdString() {
        String value = (String) this.get("req.userId");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.userId");
        }

        return value;
    }

    public String getRequestTerminalTypeString() {
        String value = (String) this.get("req.terminal.type");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.terminal.type");
        }

        return value;
    }

    public String getRequestId() {
        String reqId = (String) this.get("req.requestId");
        if (reqId == null) {
            reqId = this.getMicroServiceContext().getAttachment("req.requestId");
        }

        return reqId;
    }

    public String getRequestOpenId() {
        String value = (String) this.get("req.openId");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.openId");
        }

        return value;
    }

    public String getRequestUserType() {
        String value = (String) this.get("req.userType");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.userType");
        }

        return value;
    }

    public String getRemoteIp() {
        String value = (String) this.get("req.remoteIp");
        if (value == null) {
            value = this.getMicroServiceContext().getAttachment("req.remoteIp");
        }
        return value;
    }

    public void setRequestId(String requestId){
        this.set(REQ_REQUESTID, requestId);
        this.getMicroServiceContext().setAttachment(REQ_REQUESTID, requestId);
        MDC.put(REQ_REQUESTID, requestId);
    }
}
