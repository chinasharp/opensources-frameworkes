package org.opensourceframework.component.mq.vo;

import java.io.Serializable;

/**
 * MQ Transaction Message Listener 调用信息
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class TransListenterInfo implements Serializable {
    /**
     * 事务消息Listener 中 checkLocalTransaction调用的方法
     */
    protected String checkInvokeMethod;
    /**
     * check方法的参数类型
     */
    protected Class<?>[] checkParameterTypes;
    /**
     * check方法的参数对象
     */
    protected Object[] checkArgs;

    /**
     * 事务消息Listener 中 executeLocalTransaction调用的方法
     */
    protected String execInvokeMethod;
    /**
     * exec方法的参数类型
     */
    protected Class<?>[] execParameterTypes;
    /**
     * exec方法的参数对象
     */
    protected Object[] execArgs;

    /**
     * 事务消息Listener 中 executeLocalTransaction/checkLocalTransaction调用方法的Bean
     */
    protected String listenerInvokeBeanId;

    public TransListenterInfo() {
    }

    public TransListenterInfo(String checkInvokeMethod, String execInvokeMethod, String listenerInvokeBeanId) {
        this.checkInvokeMethod = checkInvokeMethod;
        this.execInvokeMethod = execInvokeMethod;
        this.listenerInvokeBeanId = listenerInvokeBeanId;
    }

    public String getCheckInvokeMethod() {
        return checkInvokeMethod;
    }

    public void setCheckInvokeMethod(String checkInvokeMethod) {
        this.checkInvokeMethod = checkInvokeMethod;
    }

    public String getExecInvokeMethod() {
        return execInvokeMethod;
    }

    public void setExecInvokeMethod(String execInvokeMethod) {
        this.execInvokeMethod = execInvokeMethod;
    }

    public String getListenerInvokeBeanId() {
        return listenerInvokeBeanId;
    }

    public void setListenerInvokeBeanId(String listenerInvokeBeanId) {
        this.listenerInvokeBeanId = listenerInvokeBeanId;
    }

    public Class<?>[] getCheckParameterTypes() {
        return checkParameterTypes;
    }

    public void setCheckParameterTypes(Class<?>[] checkParameterTypes) {
        this.checkParameterTypes = checkParameterTypes;
    }

    public Object[] getCheckArgs() {
        return checkArgs;
    }

    public void setCheckArgs(Object[] checkArgs) {
        this.checkArgs = checkArgs;
    }

    public Class<?>[] getExecParameterTypes() {
        return execParameterTypes;
    }

    public void setExecParameterTypes(Class<?>[] execParameterTypes) {
        this.execParameterTypes = execParameterTypes;
    }

    public Object[] getExecArgs() {
        return execArgs;
    }

    public void setExecArgs(Object[] execArgs) {
        this.execArgs = execArgs;
    }
}
