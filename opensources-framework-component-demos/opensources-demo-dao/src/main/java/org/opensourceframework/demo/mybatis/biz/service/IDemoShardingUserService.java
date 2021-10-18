package org.opensourceframework.demo.mybatis.biz.service;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.demo.mybatis.api.request.DemoUserReqDto;
import org.opensourceframework.demo.mybatis.api.response.DemoUserRespDto;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoShardingUserService extends IBizBaseService<DemoShardingUserEo, Long> {
	/**
	 * 保存/更新
	 *
	 * @param reqDto
	 * @return
	 */
	DemoUserRespDto saveOrUpdate(DemoUserReqDto reqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 */
	List<DemoUserRespDto> batchSave(List<DemoUserReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	Integer updateWithNull(DemoUserReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	Integer updateNotNull(DemoUserReqDto reqDto);

	/**
	 * 自定义操作 不使用基类方法
	 *
	 * @param userReqDto
	 * @return
	 */
	List<DemoUserRespDto> customMethod(DemoUserReqDto userReqDto);

	/**
	 * 使用Mybatis注解 编写Mapper文件
	 *
	 * @param demoUserEo
	 * @return
	 */
	PageInfo<DemoShardingUserEo> findPageByMapper(DemoShardingUserEo demoUserEo, Integer currentPage, Integer pageSize);
}
