package org.opensourceframework.center.demo.biz.dao.mapper;

import org.opensourceframework.center.demo.biz.dao.eo.DemoUserEo;
import org.opensourceframework.component.dao.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Mybatis Mapper示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface DemoMapper extends BaseMapper<DemoUserEo, Long> {
	@Select({"<script>" ,
			" select u.id , u.account , u.password , u.phone , u.member_card_no " ,
			"     from user u  ",
			" where u.name like CONCAT(#{queryEo.name},'%') or u.account like CONCAT(#{queryEo.account},'%') ",
			" order by member_card_no desc",
			"</script>"})
	List<DemoUserEo> findPage(@Param("queryEo") DemoUserEo queryEo);

	/**
	 * 通过xml映射文件调用
	 *
	 * @return
	 */
	DemoUserEo findByMapperXml(@Param("id") Long id);

	/**
	 * 通过xml映射文件调用
	 *
	 * @return
	 */
	void saveByMyBatisXml(DemoUserEo queryEo);
}
