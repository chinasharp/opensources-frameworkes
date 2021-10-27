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
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.mybatis.biz.service.IDemoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags = "Mybatis Demo")
@RestController
@RequestMapping("/com/opensourceframework/demo/mybatis")
public class MybatisDemoController {
	private static final Logger logger = LoggerFactory.getLogger(MybatisDemoController.class);
	@Autowired
	private IDemoUserService demoUserService;

	@PostMapping(value = "/saveOrUpdate" , produces = "application/json")
	@ApiOperation(value = "保存/更新例子",notes = "保存/更新例子")
	public RestResponse<DemoUserRespDto> saveOrUpdate(@RequestBody DemoUserReqDto reqDto){
		DemoUserEo demoUserEo = new DemoUserEo();
		BeanHelper.copyProperties(demoUserEo , reqDto);
		demoUserEo = demoUserService.saveOrUpdate(demoUserEo);

		DemoUserRespDto dto = new DemoUserRespDto();
		BeanHelper.copyProperties(dto , demoUserEo);
		return RestResponse.success(dto);
	}

	@PostMapping(value = "/batchSave" , produces = "application/json")
	@ApiOperation(value = "批量保存例子",notes = "批量保存")
	public RestResponse<List<DemoUserRespDto>> batchSave(@RequestBody List<DemoUserReqDto> reqDtos){
		List<DemoUserEo> eoList = new ArrayList<>();
		BeanHelper.copyCollection(eoList , reqDtos , DemoUserEo.class);
		eoList = demoUserService.batchSave(eoList);
		List<DemoUserRespDto> respDtos = new ArrayList<>();
		BeanHelper.copyCollection(reqDtos , eoList , DemoUserRespDto.class);
		return RestResponse.success(respDtos);
	}

	@GetMapping(value = "/findById" , produces = "application/json")
	@ApiOperation(value = "根据id查询",notes = "根据id查询")
	public RestResponse<DemoUserRespDto> findById(@RequestParam(name = "id" , required = true) Long id){
		DemoUserRespDto respDto = null;
		DemoUserEo demoUserEo = demoUserService.findById(id);
		if(demoUserEo != null) {
			respDto = new DemoUserRespDto();
			BeanHelper.copyProperties(respDto, demoUserEo);
		}
		return RestResponse.success(respDto);
	}

	@PostMapping(value = "/findByIdList" , produces = "application/json")
	@ApiOperation(value = "根据id列表查询数据列表",notes = "根据id列表查询数据列表")
	public RestResponse<List<DemoUserRespDto>> findByIdList(@RequestBody List<Long> idList){
		List<DemoUserEo> eoList = demoUserService.findByIds(idList);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findByIds" , produces = "application/json")
	@ApiOperation(value = "根据id字符串查询数据列表",notes = "根据id字符串查询数据列表")
	public RestResponse<List<DemoUserRespDto>> findByIds(@RequestParam(name = "ids" , required = true) String ids){
		Long[] idArray = StringHelper.strToLongArray(ids , ",");
		List<DemoUserEo> eoList = demoUserService.findByIds(idArray);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findPage" , produces = "application/json")
	@ApiOperation(value = "根据demoUserReqDto中不为空的属性为条件 分页查询",notes = "根据demoUserReqDto中不为空的属性为条件 分页查询")
	public RestResponse<PageInfo<DemoUserRespDto>> findPage(@RequestBody DemoUserReqDto demoUserReqDto ,
			@RequestParam(name="currentPage" , defaultValue = "1") Integer currentPage,
			@RequestParam(name="pageSize" , defaultValue = "10") Integer pageSize){
		DemoUserEo queryEo = new DemoUserEo();
		queryEo.setName(demoUserReqDto.getName());

		String account = demoUserReqDto.getAccount();
		if(StringUtils.isNotBlank(account)) {
			Condition condition = Restrictions.eq("name", demoUserReqDto.getName());
			queryEo.setCondition(condition);
		}

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		PageInfo<DemoUserEo> pageInfoEo =  demoUserService.findPage(queryEo , currentPage , pageSize);
		PageInfo<DemoUserRespDto> pageInfoDto = new PageInfo<>();
		BeanHelper.copyPageInfo(pageInfoDto , pageInfoEo , DemoUserRespDto.class);
		return RestResponse.success(pageInfoDto);
	}

	@PostMapping(value = "/findPageByMapper" , produces = "application/json")
	@ApiOperation(value = "根据id字符串查询数据列表,使用自实现mapper实现",notes = "根据id字符串查询数据列表,使用自实现mapper实现")
	public RestResponse<PageInfo<DemoUserRespDto>> findPageByMapper(@RequestBody DemoUserReqDto demoUserReqDto ,
			@RequestParam(name="currentPage" , defaultValue = "1") Integer currentPage,
			@RequestParam(name="pageSize" , defaultValue = "10") Integer pageSize){
		DemoUserEo queryEo = new DemoUserEo();
		queryEo.setName(demoUserReqDto.getName());

		Condition condition = Restrictions.like("account" , demoUserReqDto.getAccount().concat("%"));
		queryEo.setCondition(condition);

		PageInfo<DemoUserEo> pageInfoEo =  demoUserService.findPageByMapper(queryEo , currentPage ,pageSize);
		PageInfo<DemoUserRespDto> pageInfoDto = new PageInfo<>();
		BeanHelper.copyPageInfo(pageInfoDto , pageInfoEo , DemoUserRespDto.class);
		return RestResponse.success(pageInfoDto);
	}


	@PostMapping(value = "/findList" , produces = "application/json")
	@ApiOperation(value = "根据对象不为空的属性值查询",notes = "根据对象不为空的属性值查询")
	public RestResponse<List<DemoUserRespDto>> findList(@RequestBody DemoUserReqDto queryDto){
		DemoUserEo queryEo = new DemoUserEo();
		BeanHelper.copyProperties(queryEo , queryDto);

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		List<DemoUserEo> eoList = demoUserService.findList(queryEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/findByCondition" , produces = "application/json")
	@ApiOperation(value = "根据Condition查询",notes = "根据Condition查询")
	public RestResponse<List<DemoUserRespDto>> findByCondition(@RequestBody DemoUserReqDto queryDto){
		DemoUserEo queryEo = new DemoUserEo();

		Condition condition = Restrictions.startsWith("name" , queryDto.getName()).add(Restrictions.lte("createTimeStamp" , System.currentTimeMillis()));
		queryEo.setCondition(condition);

		//name正序 然后创建时间倒序
		queryEo.asc("name");
		queryEo.desc("createTimeStamp");

		List<DemoUserEo> eoList = demoUserService.findByCondition(queryEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/updateNotNull" , produces = "application/json")
	@ApiOperation(value = "根据id属性为条件,更新其它有值得属性",notes = "根据id属性为条件 更新其它有值得属性")
	public RestResponse<DemoUserRespDto> updateNotNull(@RequestBody DemoUserReqDto reqDto){
		DemoUserEo eo = new DemoUserEo();
		BeanHelper.copyProperties(eo , reqDto);
		Integer updateRows = demoUserService.updateNotNull(eo);

		DemoUserEo demoUserEo = demoUserService.findById(reqDto.getId());
		DemoUserRespDto demoUserDto = new DemoUserRespDto();
		BeanHelper.copyProperties(demoUserDto , demoUserEo);
		return RestResponse.success(demoUserDto);
	}

	@PostMapping(value = "/updateWithNull" , produces = "application/json")
	@ApiOperation(value = "根据id属性为条件,更新其它属性的值,包括为null的属性",notes = "根据id属性为条件 更新其它属性的值,包括为null的属性")
	public RestResponse<DemoUserRespDto> updateWithNull(@RequestBody DemoUserReqDto reqDto){
		DemoUserEo eo = new DemoUserEo();
		BeanHelper.copyProperties(eo , reqDto);
		Integer updateRows = demoUserService.updateWithNull(eo);

		DemoUserEo demoUserEo = demoUserService.findById(reqDto.getId());
		DemoUserRespDto demoUserDto = new DemoUserRespDto();
		BeanHelper.copyProperties(demoUserDto , demoUserEo);
		return RestResponse.success(demoUserDto);
	}

	@PostMapping(value = "/updateByCondition" , produces = "application/json")
	@ApiOperation(value = "根据Condition条件 更新其它有值得属性",notes = "根据Condition条件 更新其它有值得属性")
	public RestResponse<List<DemoUserRespDto>> updateByCondition(@RequestBody DemoUserReqDto reqDto){
		DemoUserEo updateEo = new DemoUserEo();

		Condition condition = Restrictions.lte("createTimeStamp" , System.currentTimeMillis());
		updateEo.setCondition(condition);
		updateEo.setName("condition");
		demoUserService.updateByCondition(updateEo);

		List<DemoUserEo> eoList = demoUserService.findList(updateEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList , eoList , DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}

	@PostMapping(value = "/upLoad")
	@ApiOperation(value = "文件上传",notes = "文件上传")
	public RestResponse<String> uploadImage(MultipartFile file)  {
		String fileName = file.getOriginalFilename();
		logger.info("获取到文件，文件名：{}, fileType:{}, logo:{}", fileName);
		return RestResponse.success(fileName);
	}

	@PostMapping(value = "/findByMultipleCondition" , produces = "application/json")
	@ApiOperation(value = "根据多个Condition条件 查询",notes = "Condition复杂使用")
	public RestResponse<List<DemoUserRespDto>> findByMultipleCondition(@RequestBody DemoUserReqDto reqDto) {
		DemoUserEo conditionEo = new DemoUserEo();

		/**
		 * sql 语句
		 * where (name like 'dubbo_test%' and account = 'test' and applictionId = 0) or (account = 'user' and name like 'test%')
		 *
		 */
		Condition condition = Restrictions.startsWith("name", "dubbo_test");
		//多个and 条件
		condition = condition.add(Restrictions.eq("account", "test"));
		condition = condition.add(Restrictions.eq("applicationId", 0));

		//与前面的条件or连接
		condition = condition.or(Restrictions.eq("account", "user").add(Restrictions.startsWith("name", "test")));

		conditionEo.setCondition(condition);
		List<DemoUserEo> eoList = demoUserService.findByCondition(conditionEo);
		List<DemoUserRespDto> dtoList = Lists.newArrayList();
		BeanHelper.copyCollection(dtoList, eoList, DemoUserRespDto.class);
		return RestResponse.success(dtoList);
	}
}
