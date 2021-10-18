package org.opensourceframework.demo.mybatis.biz.dao.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface DemoShardingUserMapper extends BaseMapper<DemoShardingUserEo, Long> {
	@Select({"<script>" ,
			" select u.id , u.account , u.password , u.phone , u.memberCardNo " ,
			"     from demo_user u  ",
			" where u.name like CONCAT(#{queryEo.name},'%') or u.account like CONCAT(#{queryEo.account},'%') ",
			" order by memberCardNo desc",
			"</script>"})
	List<DemoShardingUserEo> findPage(@Param("queryEo") DemoShardingUserEo queryEo);
}
