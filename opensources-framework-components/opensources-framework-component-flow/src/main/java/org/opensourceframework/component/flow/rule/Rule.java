package org.opensourceframework.component.flow.rule;

import org.opensourceframework.component.flow.slots.Entry;
import org.opensourceframework.component.flow.slots.EntryResp;
import org.opensourceframework.component.flow.slots.Module;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

import java.util.Map;

/**
 * * @author 
 * @version 1.0
 * 
 */
public interface Rule {

    /**
     * 执行规则返回map参数
     * @param en
     * @param requestMap
     * @param runner
     * @param context
     * @return map
     */
    EntryResp runRule(Entry en, Map<String, Object> requestMap, ExpressRunner runner, DefaultContext context);

    /**
     * 执行规则返回 boolean
     * @param mod
     * @param requestMap
     * @param runner
     * @param context
     * @return boolean
     */
    boolean runRule(Module mod, Map<String, Object> requestMap, ExpressRunner runner, DefaultContext context);
    
}
