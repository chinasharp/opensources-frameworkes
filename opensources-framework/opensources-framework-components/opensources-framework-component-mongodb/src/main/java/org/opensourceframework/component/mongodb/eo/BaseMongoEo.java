package org.opensourceframework.component.mongodb.eo;

import com.alibaba.fastjson.annotation.JSONField;
import org.opensourceframework.base.eo.BaseEo;

/**
 * MongoDB基类Eo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public class BaseMongoEo extends BaseEo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1772706032833294100L;

	@Override
	@JSONField(name = "_id")
	public final Long getId() {
		return id;
	}

	@Override
	@JSONField(name = "_id")
	public final void setId(Long id) {
		this.id = id;
	}
}
