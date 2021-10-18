package org.opensourceframework.component.seclog.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 审计日志Vo
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Getter
@Setter
public class SecLogVo {

    /**
     * 系统模块
     */
    private String appName;

    /**
     * 业务模块
     */
    private String functionName;

    /**
     * 业务属性
     */
    private String operateName;

    /**
     * 业务参数
     */
    private String params;

    /**
     * 页面路径
     */
    private String path;

    /**
     * 员工编号
     */
    private String operator;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作日期
     */
    private Date operateDate;
}
