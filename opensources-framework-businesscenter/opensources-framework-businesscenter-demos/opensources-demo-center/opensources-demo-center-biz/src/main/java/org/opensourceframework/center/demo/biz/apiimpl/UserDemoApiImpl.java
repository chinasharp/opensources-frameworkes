package org.opensourceframework.center.demo.biz.apiimpl;

import org.opensourceframework.center.demo.api.op.user.IUserDemoApi;
import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.user.DemoUserRespDto;
import org.opensourceframework.center.demo.biz.dao.eo.DemoUserEo;
import org.opensourceframework.center.demo.biz.service.IDemoService;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.rest.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Api实现以及RPC服务/RestFull服务注册示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@DubboService(group = "${dubbo.service.group}", version = "${dubbo.service.version}", protocol = "dubbo")
@RestController
@RequestMapping("/v1/op")
public class UserDemoApiImpl implements IUserDemoApi {
    @Resource
    private IDemoService demoService;

    /**
     * 保存或者更新
     *
     * @param demoUserReqDto
     * @return
     */
    @Override
    @PostMapping(value={"/saveOrUpdate"}, produces={"application/json"})
    public RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto) {
        DemoUserRespDto dto = new DemoUserRespDto();
        dto.setAccount("dubbo_test_" + System.currentTimeMillis());
        dto.setAddress("dubbo_test_" + System.currentTimeMillis());
        dto.setMemberCardNo("dubbo_test_" + System.currentTimeMillis());
        dto.setName("dubbo_test_" + System.currentTimeMillis());
        dto.setPhone("dubbo_test_" + System.currentTimeMillis());
        dto.setCreatePerson("dubbo_test_" + System.currentTimeMillis());

        DemoUserEo demoUserEo = new DemoUserEo();
        BeanHelper.copyProperties(demoUserEo, dto);
        demoService.saveOrUpdate(demoUserEo);

        DemoUserRespDto demoUserRespDto = new DemoUserRespDto();
        BeanHelper.copyProperties(demoUserRespDto, demoUserEo);

        return RestResponse.buildSuccessResponse(demoUserRespDto);
    }

    /**
     * 批量保存
     *
     * @param reqDtoList
     * @return
     */
    @Override
    @PostMapping(value={"/batchSave"}, produces={"application/json"})
    public RestResponse<List<DemoUserRespDto>> batchSave(List<DemoUserReqDto> reqDtoList) {
        reqDtoList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            DemoUserReqDto dto = new DemoUserReqDto();
            dto.setAccount("dubbo_test_" + i + System.currentTimeMillis());
            dto.setAddress("dubbo_test_"  + i + System.currentTimeMillis());
            dto.setMemberCardNo("dubbo_test_" + i + System.currentTimeMillis());
            dto.setName("dubbo_test_" + i + System.currentTimeMillis());
            dto.setPhone("dubbo_test_" + i + System.currentTimeMillis());
            dto.setCreatePerson("dubbo_test_" + i + System.currentTimeMillis());
            reqDtoList.add(dto);
        }

        List<DemoUserEo> eoList = new ArrayList<>();
        BeanHelper.copyCollection(eoList , reqDtoList , DemoUserEo.class);
        demoService.batchSave(eoList);

        List<DemoUserRespDto> respDtoList = new ArrayList<>();
        BeanHelper.copyCollection(respDtoList , eoList , DemoUserRespDto.class);
        return new RestResponse<>(respDtoList);
    }

    /**
     * 更新不为空的属性
     *
     * @param reqDto
     * @return
     */
    @Override
    @PostMapping(value={"/updateWithNull"}, produces={"application/json"})
    public RestResponse<Void> updateWithNull(DemoUserReqDto reqDto) {
        return null;
    }

    /**
     * 更新所有属性
     *
     * @param reqDto
     * @return
     */
    @Override
    @PostMapping(value={"/updateNotNull"}, produces={"application/json"})
    public RestResponse<Void> updateNotNull(DemoUserReqDto reqDto) {
        return null;
    }
}
