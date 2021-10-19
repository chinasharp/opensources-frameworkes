package org.opensourceframework.component.flow.slots;

import lombok.Data;

import java.util.Map;

/**
 * * @author 
 * @version 1.0
 * 
 */
@Data
public class EntryResp {

    /**
     * 规则执行结果
     */
    private Map<String,Object> result;
    /**
     * 是否继续中断执行
     */
    private Object modResult;
}
