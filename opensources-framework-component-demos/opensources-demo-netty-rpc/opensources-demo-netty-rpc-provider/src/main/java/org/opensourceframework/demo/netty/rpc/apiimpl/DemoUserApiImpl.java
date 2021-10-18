package org.opensourceframework.demo.netty.rpc.apiimpl;


import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.netty.rpc.api.IDemoUserApi;
import org.opensourceframework.demo.netty.rpc.api.dto.request.DemoUserReqDto;
import org.opensourceframework.demo.netty.rpc.api.dto.response.DemoUserRespDto;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.opensourceframework.netty.rpc.core.annotation.RpcProvider;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Demo 操作Api实现
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RpcProvider
public class DemoUserApiImpl implements IDemoUserApi {
    /**
     * 保存或者更新
     *
     * @param demoUserReqDto
     * @return
     */
    @Override
    @PostMapping(value = {"/saveOrUpdate"}, produces = {"application/json"})
    public RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto) {

        DemoUserRespDto dto = new DemoUserRespDto();
        dto.setId(SnowFlakeId.nextId(null, null));
        dto.setAccount("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        dto.setAddress("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        dto.setMemberCardNo("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        dto.setName("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        dto.setPhone("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        dto.setCreatePerson("dubbo_test_" + OpensourceFrameworkSystem.getIp());
        return new RestResponse<>(dto);
    }

    /**
     * 批量保存
     *
     * @param reqDtoList
     * @return
     */
    @Override
    public RestResponse<List<DemoUserRespDto>> batchSave(List<DemoUserReqDto> reqDtoList) {
        return null;
    }

    /**
     * 更新不为空的属性
     *
     * @param reqDto
     * @return
     */
    @Override
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
    public RestResponse<Void> updateNotNull(DemoUserReqDto reqDto) {
        return null;
    }
}
