package org.opensourceframework.base.db;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 条件对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public class QueryCondition implements Condition {
	private static final Logger logger = LoggerFactory.getLogger(QueryCondition.class);
	/**
	 */
	protected String property;

	/**
	 */
	protected Object value;

	/**
	 * Query Operators
	 * 
	 */
	protected OperationalEnum operator;

	public QueryCondition() {
		// TODO Auto-generated constructor stub
		super();
	}

	public QueryCondition(String property, Object value, OperationalEnum operator) {
		super();
		this.property = property;
		this.value = value;
		this.operator = operator;
	}

	public QueryCondition(String property, Object value) {
		super();
		this.property = property;
		this.value = value;
		this.operator = OperationalEnum.EQ;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public OperationalEnum getOperator() {
		return operator;
	}

	@Override
	public Map<String, Object> getPropertyMap() {
		Map<String , Object> propertyMap = Maps.newHashMap();
		propertyMap.put(property , value);
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
	public Condition createCondition(){
		return new QueryCondition();
	}
}
