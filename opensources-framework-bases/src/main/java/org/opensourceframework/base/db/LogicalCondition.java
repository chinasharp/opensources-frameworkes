package org.opensourceframework.base.db;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 条件连接对象 例如db:and or  mongodb:$and $or $not $nor  es:
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/9/24
 */
public class LogicalCondition implements Condition {
	private static final Logger logger = LoggerFactory.getLogger(LogicalCondition.class);

	private List<? extends Condition> conditions;

	private OperationalEnum operator;

	public LogicalCondition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LogicalCondition(List<? extends Condition> conditions, OperationalEnum operator) {
		super();
		this.conditions = conditions;
		this.operator = operator;
	}

	public List<? extends Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<? extends Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public OperationalEnum getOperator() {
		return operator;
	}

	@Override
	public Map<String, Object> getPropertyMap() {
		Map<String, Object> propertyMap = Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(conditions)){
			for(Condition condition : conditions){
				propertyMap.putAll(condition.getPropertyMap());
			}
		}
		return propertyMap;
	}

	public void setOperator(OperationalEnum operator) {
		this.operator = operator;
	}

	@Override
	public Condition add(Condition cond) {
		// TODO Auto-generated method stub
		Condition confition = null;
		if (getOperator() != null) {
			confition = Restrictions.and(this, cond);
		} else {
			confition = cond;
		}
		return confition;
	}

	@Override
	public Condition or(Condition cond) {
		// TODO Auto-generated method stub
		Condition confition = null;
		if (getOperator() != null) {
			confition = Restrictions.or(this, cond);
		} else {
			confition = cond;
		}
		return confition;
	}

	@Override
	public Condition createCondition() {
		// TODO Auto-generated method stub
		return new LogicalCondition();
	}
}
