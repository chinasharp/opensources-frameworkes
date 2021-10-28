#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.feignclients.callback;

import com.alibaba.fastjson.JSON;
import feign.hystrix.FallbackFactory;
import ${package}.biz.feignclients.feignservice.DemoFeignService;
import org.opensourceframework.base.rest.RestResponse;
import ${groupId}.center.${dependency-center-name}.api.dto.request.DemoReqDto;
import ${groupId}.center.${dependency-center-name}.api.dto.response.DemoRespDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Feign异常回调处理类
 *
 * @author yu.ce@foxmail.com
 * @date 2021-10-21 11:44
 * @since 1.0.0
 */
public class DemoFeignServiceFallbackFactory implements FallbackFactory<DemoFeignService> {
    private  static final Logger logger = LoggerFactory.getLogger("DemoFeignService");
    @Override
    public DemoFeignService create(Throwable throwable) {
        return new DemoFeignService() {
            @Override
            public RestResponse<DemoRespDto> saveOrUpdate(DemoReqDto DemoReqDto) {
                logger.error("DemoFeignService saveOrUpdate:{}调用接口异常回退:", JSON.toJSONString(DemoReqDto), throwable);
                return null;
            }

            @Override
            public RestResponse<List<DemoRespDto>> batchSave(List<DemoReqDto> reqDtoList) {
                return null;
            }

            @Override
            public RestResponse<Void> updateWithNull(DemoReqDto reqDto) {
                return null;
            }

            @Override
            public RestResponse<Void> updateNotNull(DemoReqDto reqDto) {
                return null;
            }
        };
    }
}
