#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.dao.mapper;

import ${package}.biz.dao.eo.DemoEo;
import ${groupId}.component.dao.base.BaseMapper;
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
			" where u.name like CONCAT(${symbol_pound}{queryEo.name},'%') or u.account like CONCAT(${symbol_pound}{queryEo.account},'%') ",
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
