package org.opensourceframework.center.demo.biz.service;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.center.demo.biz.dao.eo.DemoUserEo;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

import java.util.List;

/**
 * Service接口示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoService extends IBizBaseService<DemoUserEo, Long> {
	/**
	 * 保存/更新
	 *
	 * @param eo
	 * @return
	 */
	DemoUserEo saveOrUpdate(DemoUserEo eo);

	/**
	 * 批量保存
	 *
	 * @param eoList
	 */
	List<DemoUserEo> batchSave(List<DemoUserEo> eoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param eo
	 * @return
	 */
	@Override
	Integer updateWithNull(DemoUserEo eo);

	/**
	 * 更新所有属性
	 *
	 * @param eo
	 * @return
	 */
	@Override
	Integer updateNotNull(DemoUserEo eo);

	/**
	 * 自定义操作 不使用基类方法
	 *
	 * @param eo
	 * @return
	 */
	List<DemoUserEo> customMethod(DemoUserEo eo);

	/**
	 * 使用Mybatis的注解@Select @Update @Insert实现分页查询
	 *
	 * @param demoUserEo
	 * @return
	 */
	PageInfo<DemoUserEo> findPageByMapper(DemoUserEo demoUserEo, Integer currentPage, Integer pageSize);

	/**
	 * 通过mybatis的映射文件调用
	 *
	 * @param id
	 * @return
	 */
	DemoUserEo findByMapperXml(Long id);

	/**
	 * 通过mybatis的映射文件调用
	 *
	 * @param eo
	 */
	DemoUserEo saveByMybatisXml(DemoUserEo eo);
}
