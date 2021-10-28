#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.service;

import com.github.pagehelper.PageInfo;
import ${package}.biz.dao.eo.DemoEo;
import ${groupId}.starter.mybatis.base.service.IBizBaseService;

import java.util.List;

/**
 * Service接口示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoService extends IBizBaseService<DemoEo, Long> {
	/**
	 * 保存/更新
	 *
	 * @param eo
	 * @return
	 */
	DemoEo saveOrUpdate(DemoEo eo);

	/**
	 * 批量保存
	 *
	 * @param eoList
	 */
	List<DemoEo> batchSave(List<DemoEo> eoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param eo
	 * @return
	 */
	@Override
	Integer updateWithNull(DemoEo eo);

	/**
	 * 更新所有属性
	 *
	 * @param eo
	 * @return
	 */
	@Override
	Integer updateNotNull(DemoEo eo);

	/**
	 * 自定义操作 不使用基类方法
	 *
	 * @param eo
	 * @return
	 */
	List<DemoEo> customMethod(DemoEo eo);

	/**
	 * 使用Mybatis的注解@Select @Update @Insert实现分页查询
	 *
	 * @param demoUserEo
	 * @return
	 */
	PageInfo<DemoEo> findPageByMapper(DemoEo demoUserEo, Integer currentPage, Integer pageSize);

	/**
	 * 通过mybatis的映射文件调用
	 *
	 * @param id
	 * @return
	 */
	DemoEo findByMapperXml(Long id);

	/**
	 * 通过mybatis的映射文件调用
	 *
	 * @param eo
	 */
	DemoEo saveByMybatisXml(DemoEo eo);
}
