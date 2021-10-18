package org.opensourceframework.component.flow.op;

import com.alibaba.fastjson.JSONObject;
import com.ql.util.express.Operator;
import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.exception.QLException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * * @author 
 * @version 1.0
 * 
 * 扩展notIn 组件
 */
public class OperatorNotIn extends Operator {
	public OperatorNotIn(String aName) {
		this.name = aName;
	}

	public OperatorNotIn(String aAliasName, String aName, String aErrorInfo) {
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	public Object executeInner(Object[] list) throws Exception {
		Object obj = list[0];
		List<Object> arrStr =new ArrayList<>();
		if (obj == null) {
		    if(QLExpressRunStrategy.isAvoidNullPointer()){
                //避免空指针策略异常则返回false
		        return false;
            }
			// 对象为空，不能执行方法
			String msg = "对象为空，不能执行方法:";
			throw new QLException(msg + this.name);
		} else if (((obj instanceof Number) || (obj instanceof String)) == false) {
			String msg = "对象类型不匹配，只有数字和字符串类型才才能执行 in 操作,当前数据类型是:";
			throw new QLException(msg + obj.getClass().getName());
		} else if(list.length == 2 && (list[1].getClass().isArray() || list[1] instanceof List)){
			int len = Array.getLength(list[1]);
			if(obj.equals(list[1]) == true) {
				return false;
			}
			else if(list[1].getClass().isArray()) {
				for(int i=0;i<len;i++){
					arrStr.add(Array.get(list[1],i));
				}
				System.out.println("arr=="+ JSONObject.toJSONString(arrStr));
                return !arrStr.contains(obj);
			}
			return false;

		} else{
			String msg = "对象类型不匹配，只有数字和字符串类型才才能执行 in 操作,当前数据类型是:";
			throw new QLException(msg + obj.getClass().getName());
		}
	}


}
