package org.opensourceframework.component.flow.slots;

import lombok.Data;

import java.util.LinkedList;

/**
 * * @author 
 * @version 1.0
 * 
 * 组件实体
 */
@Data
public class Module {
    /**
     * 组件key
     */
    private String moduleKey;
    /**
     * 组件规则
     */
    private String moduleRule;
    /**
     * 组件类型 0:规则判断组件 1:执行组件 2触达组件
     */
    private Integer moduleType;
    /**
     * 执行组件类型 :  1:条件符合执行 2:条件不符合执行
     */
    private Integer runType;
    /**
     * 规则实例
     */
    LinkedList<Entry> entry;
}
