import org.opensourceframework.component.flow.rule.RuleProcess;
import org.opensourceframework.component.flow.slots.Entry;
import org.opensourceframework.component.flow.slots.EntryResp;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * * @author 
 * @version 1.0
 * 
 */
public class testRule {


    @Test
    public void runRuleTest() {
        ExpressRunner runner = new ExpressRunner();
        Map<String,Object> requestMap = new HashMap<>();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Entry e = new Entry();
        String exp = "if  1  notIn [1,2] then 1 else 0";
        e.setRule(exp);
        e.setRuleKey("1");
        context.put("key1",2);
        RuleProcess pro = new RuleProcess();
        EntryResp resp = pro.runRule(e,requestMap,runner,context);
        System.out.println("requestMap:{}"+ requestMap);
    }


}
