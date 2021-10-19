#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.apiimpl;

import ${package}.api.IDemoApi;
import ${package}.api.dto.request.DemoReqDto;
import ${package}.api.dto.response.DemoRespDto;
import ${groupId}.common.genrator.IdGenerator;
import ${groupId}.common.rest.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * Api实现以及RPC服务/RestFull服务注册示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@DubboService(group = "${symbol_dollar}{dubbo.service.group}", version = "${symbol_dollar}{dubbo.service.version}", protocol = "dubbo")
@RestController
@RequestMapping("/v1/op")
public class DemoApiImpl implements IDemoApi {
    /**
     * 保存或者更新
     *
     * @param demoReqDto
     * @return
     */
    @Override
    @PostMapping(value={"/saveOrUpdate"}, produces={"application/json"})
    public RestResponse<DemoRespDto> saveOrUpdate(DemoReqDto demoReqDto) {
        DemoRespDto dto = new DemoRespDto();
        dto.setId(IdGenerator.nextId(null, null));
        dto.setAccount("dubbo_test");
        dto.setAddress("dubbo_test");
        dto.setMemberCardNo("dubbo_test");
        dto.setName("dubbo_test");
        dto.setPhone("dubbo_test");
        dto.setCreatePerson("dubbo_test");
        return new RestResponse<>(dto);
    }

    /**
     * 批量保存
     *
     * @param reqDtoList
     * @return
     */
    @Override
    @PostMapping(value={"/batchSave"}, produces={"application/json"})
    public RestResponse<List<DemoRespDto>> batchSave(List<DemoReqDto> reqDtoList) {
        return null;
    }

    /**
     * 更新不为空的属性
     *
     * @param reqDto
     * @return
     */
    @Override
    @PostMapping(value={"/updateWithNull"}, produces={"application/json"})
    public RestResponse<Void> updateWithNull(DemoReqDto reqDto) {
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
    public RestResponse<Void> updateNotNull(DemoReqDto reqDto) {
        return null;
    }
}
