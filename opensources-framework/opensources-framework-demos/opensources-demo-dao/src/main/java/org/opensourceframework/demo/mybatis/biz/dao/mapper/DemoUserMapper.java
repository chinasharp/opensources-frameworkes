package org.opensourceframework.demo.mybatis.biz.dao.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoUserEo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DemoUser Mapper
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface DemoUserMapper extends BaseMapper<DemoUserEo, Long> {
	@Select({"<script>" ,
			" select u.id , u.account , u.password , u.phone , u.memberCardNo " ,
			"     from demo_user u  ",
			" where u.name like CONCAT(#{queryEo.name},'%') or u.account like CONCAT(#{queryEo.account},'%') ",
			" order by memberCardNo desc",
			"</script>"})
	List<DemoUserEo> findPage(@Param("queryEo") DemoUserEo queryEo);
}
