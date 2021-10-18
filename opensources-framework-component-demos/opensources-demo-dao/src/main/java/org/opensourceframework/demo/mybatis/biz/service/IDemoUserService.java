package org.opensourceframework.demo.mybatis.biz.service;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoUserEo;
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
	 * @param demoUserEo
	 * @return
	 */
	DemoUserEo saveOrUpdate(DemoUserEo demoUserEo);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	List<DemoUserEo> batchSave(List<DemoUserEo> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param demoUserEo
	 * @return
	 */
	@Override
	Integer updateWithNull(DemoUserEo demoUserEo);

	/**
	 * 更新所有属性
	 *
	 * @param demoUserEo
	 * @return
	 */
	@Override
	Integer updateNotNull(DemoUserEo demoUserEo);

	/**
	 * 自定义操作 不使用基类方法
	 *
	 * @param userReqDto
	 * @return
	 */
	List<DemoUserEo> customMethod(DemoUserEo userReqDto);

	/**
	 * 分页查询
	 *
	 * @param demoUserEo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageInfo<DemoUserEo> findPageByMapper(DemoUserEo demoUserEo , Integer currentPage,Integer pageSize);
}
