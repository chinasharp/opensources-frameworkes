package org.opensourceframework.demo.idempotent.biz.service;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoUserService extends IBizBaseService<DemoUserEo, Long> {
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
	 * @return list
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
	 * 分页查询 通过Mapper.xml文件demo
	 *
	 * @param demoUserEo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo<DemoUserEo> findPageByMapper(DemoUserEo demoUserEo, Integer currentPage, Integer pageSize);
}
