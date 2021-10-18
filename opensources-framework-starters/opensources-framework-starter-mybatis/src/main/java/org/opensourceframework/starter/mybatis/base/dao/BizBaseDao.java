package org.opensourceframework.starter.mybatis.base.dao;

import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.component.dao.base.BaseDao;
import org.opensourceframework.component.dao.base.BaseMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * 基础业务Dao
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository("bizBaseDao")
public class BizBaseDao<T extends BaseEo, PK extends Serializable> extends BaseDao<T , PK> {
	/**
	 * 实体版本号,当改变DTO的时候通过版免序列化异常问题
	 */
	protected final static String POJO_VERSION = ":2.0:";

	/**
	 * Spring注解的特殊用法 会将所有类型为BaseMapper的bean注入到Map中
	 */
	@Resource
	public Map<String, BaseMapper> mappers;

	@Override
	public Map<String, BaseMapper> getMappers() {
		return mappers;
	}
}
