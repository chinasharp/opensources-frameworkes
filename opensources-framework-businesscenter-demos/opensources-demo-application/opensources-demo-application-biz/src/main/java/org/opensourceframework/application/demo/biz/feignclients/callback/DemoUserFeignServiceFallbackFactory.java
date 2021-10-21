package org.opensourceframework.application.demo.biz.feignclients.callback;

import com.alibaba.fastjson.JSON;
import feign.hystrix.FallbackFactory;
import org.opensourceframework.application.demo.biz.feignclients.feignservice.DemoUserFeignService;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
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
public class DemoUserFeignServiceFallbackFactory implements FallbackFactory<DemoUserFeignService> {
    private  static final Logger logger = LoggerFactory.getLogger("DemoUserFeignService");
    @Override
    public DemoUserFeignService create(Throwable throwable) {
        return new DemoUserFeignService() {
            @Override
            public RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto) {
                logger.error("DemoUserFeignService saveOrUpdate:{}调用接口异常回退:", JSON.toJSONString(demoUserReqDto), throwable);
                return null;
            }

            @Override
            public RestResponse<List<DemoUserRespDto>> batchSave(List<DemoUserReqDto> reqDtoList) {
                return null;
            }

            @Override
            public RestResponse<Void> updateWithNull(DemoUserReqDto reqDto) {
                return null;
            }

            @Override
            public RestResponse<Void> updateNotNull(DemoUserReqDto reqDto) {
                return null;
            }
        };
    }
}
