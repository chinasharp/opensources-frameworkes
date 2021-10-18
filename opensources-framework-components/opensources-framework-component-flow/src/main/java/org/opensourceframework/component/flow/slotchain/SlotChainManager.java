package org.opensourceframework.component.flow.slotchain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * * @author 
 * @version 1.0
 * 
 */
public class SlotChainManager{

    /**
     * 定义流程map
     */
    private static volatile Map<String, Object> flowRules = new HashMap<>();

    /**
     * 获取规则
     * @param activityCode
     * @return
     */
    public static Object getSlotChain(String activityCode) {
        return flowRules.get(activityCode);
    }

    /**
     * 获取所有的链
     * @param
     * @return
     */
    public static Map<String, Object> getAllSlotChain() {
        return flowRules;
    }

    /**
     * 添加或者修改链
     * @param activityCode
     * @param flow
     */
    public static void putSlotChain(String activityCode, Object flow) {
        flowRules.put(activityCode,flow);
    }

    /**
     * 初始化链
     * @param rules
     */
    public static void initSlotChain(Map<String, Object> rules) {
        flowRules = rules;
    }

}
