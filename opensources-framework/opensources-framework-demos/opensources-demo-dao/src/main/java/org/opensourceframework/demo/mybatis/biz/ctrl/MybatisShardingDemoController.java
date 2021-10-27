package org.opensourceframework.demo.mybatis.biz.ctrl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.Restrictions;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.helper.StringHelper;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.mybatis.api.request.DemoUserReqDto;
import org.opensourceframework.demo.mybatis.api.response.DemoUserRespDto;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.opensourceframework.demo.mybatis.biz.service.IDemoShardingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags = "Mybatis Sharding Demo")
@RestController
@RequestMapping("/com/opensourceframework/shardingdemo/mybatis")
public class MybatisShardingDemoController {
	@Autowired
	private IDemoShardingUserService demoUserService;

	@PostMapping(value = "/saveOrUpdate" , produces = "application/json")
	@ApiOperation(value = "保存/更新例子",notes = "保存/更新例子")
	public RestResponse<DemoUserRespDto> saveOrUpdate(@RequestBody DemoUserReqDto reqDto){
		DemoUserRespDto demoUserRespDto = demoUserService.saveOrUpdate(reqDto);
		return RestResponse.success(demoUserRespDto);
	}

	@PostMapping(value = "/batchSave" , produces = "application/json")
	@ApiOperation(value = "批量保存例子",notes = "批量保存")
	public RestResponse<List<DemoUserRespDto>> batchSave(@RequestBody List<DemoUserReqDto> reqDtos){
		List<DemoUserRespDto> respDtos = demoUserService.batchSave(reqDtos);
		return RestResponse.success(respDtos);
	}

	@GetMapping(value = "/findById" , produces = "application/json")
	@ApiOperation(value = "根据id查询",notes = "根据id查询")
	public RestResponse<DemoUserRespDto> findById(@RequestParam(name = "id" , required = true) Long id){
		DemoUserRespDto respDto = null;
		DemoShardingUserEo demoShardingUserEo = demoUserService.findById(id);
		if(demoShardingUserEo != null) {
			respDto = new DemoUserRespDto();
			BeanHelper.copyProperties(respDto, demoShardingUserEo);
		}
		return RestResponse.success(respDto);
	}

	@PostMapping(value = "/findByIdList" , produces = "application/json")
	@ApiOperation(value = "根据id列表查询数据列表",notes = "根据id列表查询数据列表")
	public RestResponse<List<DemoUserRespDto>> findByIdList(@RequestBody List<Long> idList){
		List<DemoShardingUserEo> eoList = demoUserService.findByIds(idList);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findByIds" , produces = "application/json")
	@ApiOperation(value = "根据id字符串查询数据列表",notes = "根据id字符串查询数据列表")
	public RestResponse<List<DemoUserRespDto>> findByIds(@RequestParam(name = "ids" , required = true) String ids){
		Long[] idArray = StringHelper.strToLongArray(ids , ",");
		List<DemoShardingUserEo> eoList = demoUserService.findByIds(idArray);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findPage" , produces = "application/json")
	@ApiOperation(value = "根据demoUserReqDto中不为空的属性为条件 分页查询",notes = "根据demoUserReqDto中不为空的属性为条件 分页查询")
	public RestResponse<PageInfo<DemoUserRespDto>> findPage(@RequestBody DemoUserReqDto demoUserReqDto ,
			@RequestParam(name="currentPage" , defaultValue = "1") Integer currentPage,
			@RequestParam(name="pageSize" , defaultValue = "10") Integer pageSize){
		DemoShardingUserEo queryEo = new DemoShardingUserEo();
		queryEo.setName(demoUserReqDto.getName());

		String account = demoUserReqDto.getAccount();
		if(StringUtils.isNotBlank(account)) {
			Condition condition = Restrictions.eq("name", demoUserReqDto.getName());
			queryEo.setCondition(condition);
		}

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		PageInfo<DemoShardingUserEo> pageInfoEo =  demoUserService.findPage(queryEo , currentPage , pageSize);
		PageInfo<DemoUserRespDto> pageInfoDto = new PageInfo<>();
		BeanHelper.copyPageInfo(pageInfoDto , pageInfoEo , DemoUserRespDto.class);
		return RestResponse.success(pageInfoDto);
	}

	@PostMapping(value = "/findPageByMapper" , produces = "application/json")
	@ApiOperation(value = "根据id字符串查询数据列表,使用自实现mapper实现",notes = "根据id字符串查询数据列表,使用自实现mapper实现")
	public RestResponse<PageInfo<DemoUserRespDto>> findPageByMapper(@RequestBody DemoUserReqDto demoUserReqDto ,
			@RequestParam(name="currentPage" , defaultValue = "1") Integer currentPage,
			@RequestParam(name="pageSize" , defaultValue = "10") Integer pageSize){
		DemoShardingUserEo queryEo = new DemoShardingUserEo();
		queryEo.setName(demoUserReqDto.getName());

		Condition condition = Restrictions.like("account" , demoUserReqDto.getAccount().concat("%"));
		queryEo.setCondition(condition);

		PageInfo<DemoShardingUserEo> pageInfoEo =  demoUserService.findPageByMapper(queryEo , currentPage ,pageSize);
		PageInfo<DemoUserRespDto> pageInfoDto = new PageInfo<>();
		BeanHelper.copyPageInfo(pageInfoDto , pageInfoEo , DemoUserRespDto.class);
		return RestResponse.success(pageInfoDto);
	}


	@PostMapping(value = "/findList" , produces = "application/json")
	@ApiOperation(value = "根据对象不为空的属性值查询",notes = "根据对象不为空的属性值查询")
	public RestResponse<List<DemoUserRespDto>> findList(@RequestBody DemoUserReqDto queryDto){
		DemoShardingUserEo queryEo = new DemoShardingUserEo();
		BeanHelper.copyProperties(queryEo , queryDto);

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		List<DemoShardingUserEo> eoList = demoUserService.findList(queryEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findByCondition" , produces = "application/json")
	@ApiOperation(value = "根据Condition查询",notes = "根据Condition查询")
	public RestResponse<List<DemoUserRespDto>> findByCondition(@RequestBody DemoUserReqDto queryDto){
		DemoShardingUserEo queryEo = new DemoShardingUserEo();

		Condition condition = Restrictions.startsWith("name" , queryDto.getName()).add(Restrictions.lte("createTimeStamp" , System.currentTimeMillis()));
		queryEo.setCondition(condition);

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		List<DemoShardingUserEo> eoList = demoUserService.findByCondition(queryEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/updateNotNull" , produces = "application/json")
	@ApiOperation(value = "根据id属性为条件,更新其它有值得属性",notes = "根据id属性为条件 更新其它有值得属性")
	public RestResponse<DemoUserRespDto> updateNotNull(@RequestBody DemoUserReqDto reqDto){
		Integer updateRows = demoUserService.updateNotNull(reqDto);

		DemoShardingUserEo demoShardingUserEo = demoUserService.findById(reqDto.getId());
		DemoUserRespDto demoUserDto = new DemoUserRespDto();
		BeanHelper.copyProperties(demoUserDto , demoShardingUserEo);
		return RestResponse.success(demoUserDto);
	}

	@PostMapping(value = "/updateWithNull" , produces = "application/json")
	@ApiOperation(value = "根据id属性为条件,更新其它属性的值,包括为null的属性",notes = "根据id属性为条件 更新其它属性的值,包括为null的属性")
	public RestResponse<DemoUserRespDto> updateWithNull(@RequestBody DemoUserReqDto reqDto){
		Integer updateRows = demoUserService.updateWithNull(reqDto);

		DemoShardingUserEo demoShardingUserEo = demoUserService.findById(reqDto.getId());
		DemoUserRespDto demoUserDto = new DemoUserRespDto();
		BeanHelper.copyProperties(demoUserDto , demoShardingUserEo);
		return RestResponse.success(demoUserDto);
	}

	@PostMapping(value = "/updateByCondition" , produces = "application/json")
	@ApiOperation(value = "根据Condition条件 更新其它有值得属性",notes = "根据Condition条件 更新其它有值得属性")
	public RestResponse<List<DemoUserRespDto>> updateByCondition(@RequestBody DemoUserReqDto reqDto){
		DemoShardingUserEo updateEo = new DemoShardingUserEo();

		Condition condition = Restrictions.lte("createTimeStamp" , System.currentTimeMillis());
		updateEo.setCondition(condition);
		updateEo.setName("condition");
		demoUserService.updateByCondition(updateEo);

		List<DemoShardingUserEo> eoList = demoUserService.findList(updateEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}
}
