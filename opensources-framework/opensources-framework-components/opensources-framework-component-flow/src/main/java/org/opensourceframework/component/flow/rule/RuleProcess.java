package org.opensourceframework.component.flow.rule;

import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.component.flow.op.OperatorNotIn;
import org.opensourceframework.component.flow.config.FlowConfig;
import org.opensourceframework.component.flow.slots.Entry;
import org.opensourceframework.component.flow.slots.EntryResp;
import org.opensourceframework.component.flow.slots.Module;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * * @author 
 * @version 1.0
 * 
 */
@Component
public class RuleProcess implements Rule {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义规则执行
     *
     * @Param requestMap 规则入参
     * en 规则
     * runner 规则执行器
     */
    @Override
    public EntryResp runRule(Entry en, Map<String, Object> requestMap, ExpressRunner runner, DefaultContext context) {
        EntryResp resp = new EntryResp();
        //执行单个规则
        if (StringUtils.isEmpty(en.getRule())) {
            resp.setResult(requestMap);
            resp.setModResult(0);
            return resp;
        }
        String ruleString = assembleRule(en.getRule(), requestMap);
        Object execute = ruleExecute(ruleString,context,runner);
        requestMap.put(en.getRuleKey(), execute);
        requestMap.put(FlowConfig.ruleResult, execute);
        resp.setModResult(execute);
        logger.info("requestMap:{}", JSONObject.toJSONString(requestMap));
        return resp;
    }

    /**
     * 定义规则执行
     *
     * @Param requestMap 规则入参
     * en 规则
     * runner 规则执行器
     */
    @Override
    public boolean runRule(Module en, Map<String, Object> requestMap, ExpressRunner runner, DefaultContext context) {
        boolean result = true;
        if(!StringUtils.isEmpty(en.getModuleRule())){
            String ruleString = assembleRule(en.getModuleRule(), requestMap);
            Object execute = ruleExecute(ruleString,context,runner);
            if (execute instanceof Boolean) {
                result = (boolean) execute;
            }

        }
        return result;
    }

    /**
     * 执行组件内规则是否需要继续执行
     *
     * @Param ruleResult 当前规则执行结果
     */
    public boolean checkRule(String ruleResult) {
        boolean result = true;
        return result;
    }

    /**
     * 执行规则时进行变量替换
     *
     * @Param requestMap 进入变量
     * rule 规则
     */
    private String assembleRule(String rule, Map<String, Object> requestMap) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(requestMap);
        String ruleString = strSubstitutor.replace(rule);
        return ruleString;
    }

    /**
     * 规则执行层代码
     * @param rule 规则参数
     * @param context 上下文
     * @param runner 执行器
     * @return Object执行结果
     */
    private Object ruleExecute(String rule, DefaultContext context, ExpressRunner runner) {
        Object execute = null;
        logger.info("【ruleExecute执行参数】:{}",rule);
        try {
            execute = runner.execute(rule, context, null, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("【ruleExecute执行结果】:{}",execute);
        return execute;
    }

    /**
     * 获取执行器对象
     */
    public ExpressRunner getExpressRunner(){
        ExpressRunner runner = new ExpressRunner();
        try {
            //增加扩展组件
            runner.addOperator("notIn",new OperatorNotIn("notIn"));
        } catch (Exception e) {
            logger.error("【getExpressRunner add notIn error】：{}",e);
            e.printStackTrace();
        }
        return  runner;
    }

}
