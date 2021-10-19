package org.opensourceframework.component.flow.slots;

import lombok.Data;

/**
 * * @author 
 * @version 1.0
 * 
 * 规则实体
 */
@Data
public class Entry {

    /**
     * 参数
     */
    private String ruleKey;

    /**
     * 规则参数
     */
    private String rule;
}
