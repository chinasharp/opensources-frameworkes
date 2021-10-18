package org.opensourceframework.demo.mq.transaction.dao.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
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
public interface DemoMapper extends BaseMapper<DemoEo, Long> {
	@Select({"<script>" ,
			" select u.id , u.account , u.password , u.phone , u.member_card_no " ,
			"     from user u  ",
			" where u.name like CONCAT(#{queryEo.name},'%') or u.account like CONCAT(#{queryEo.account},'%') ",
			" order by member_card_no desc",
			"</script>"})
	List<DemoEo> findPage(@Param("queryEo") DemoEo queryEo);

	/**
	 * 通过xml映射文件调用
	 *
	 * @return
	 */
	DemoEo findByMapperXml(@Param("id") Long id);

	/**
	 * 通过xml映射文件调用
	 *
	 * @return
	 */
	void saveByMyBatisXml(DemoEo queryEo);
}
