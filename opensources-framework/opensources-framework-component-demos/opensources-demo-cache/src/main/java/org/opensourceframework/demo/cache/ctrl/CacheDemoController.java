package org.opensourceframework.demo.cache.ctrl;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.cache.cache.local.UserLocalCache;
import org.opensourceframework.demo.cache.cache.mix.UserMixCache;
import org.opensourceframework.demo.cache.cache.remote.UserRemoteCache;
import org.opensourceframework.demo.cache.entity.UserFactory;
import org.opensourceframework.demo.cache.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
@Api(tags = "使用Cache Demo 包括本地缓存、分布式缓存、本地+分布式混合缓存的使用 ")
@RestController
@RequestMapping("/v1/demo")
public class CacheDemoController {
    @Resource
    private UserMixCache userMixCache;

    @Resource
    private UserLocalCache userLocalCache;

    @Resource
    private UserRemoteCache userRemoteCache;

    @PostMapping("/mix_cache/add")
    @ApiOperation(value="使用混合缓存写数据,配置说明:" +
            "混合缓存是否开启本地缓存:opensourceframework.mix.cache.local.flag: true " +
            "混合缓存是否开启分布式缓存:opensourceframework.mix.cache.remote.flag: true" +
            "同步分布式缓存是否使用异步方式(实时性要求高时设置此属性为false):opensourceframework.mix.cache.enable.async: true", notes="使用混合缓存写数据")
    public RestResponse<UserInfo> addMixCache(@RequestParam(name = "id") Long id){
        UserInfo userInfo = UserFactory.buildUserInfo(id);
        userMixCache.setCache(userInfo);
        return RestResponse.buildSuccessResponse(userInfo);
    }

    @PostMapping("/local_cache/add")
    @ApiOperation(value="使用本地缓存写数据", notes="使用本地缓存写数据")
    public RestResponse<UserInfo> addLocalache(@RequestParam(name = "id") Long id){
        UserInfo userInfo = UserFactory.buildUserInfo(id);
        userLocalCache.setCache(userInfo);
        return RestResponse.buildSuccessResponse(userInfo);
    }

    @PostMapping("/remote_cache/add")
    @ApiOperation(value="使用分布式缓存写数据", notes="使用分布式缓存写数据")
    public RestResponse<UserInfo> addRemoteCache(@RequestParam(name = "id") Long id){
        UserInfo userInfo = UserFactory.buildUserInfo(id);
        userRemoteCache.setCache(userInfo);
        return RestResponse.buildSuccessResponse(userInfo);
    }

    @PostMapping("/mix_cache/{id}")
    @ApiOperation(value="通过混合缓存获取数据", notes="通过混合缓存获取数据")
    public RestResponse<UserInfo> getMixCache(@PathVariable(name = "id") Long id){
        UserInfo userInfo = userMixCache.getCache(id);
        return RestResponse.buildSuccessResponse(userInfo);
    }

    @PostMapping("/local_cache/{id}")
    @ApiOperation(value="使用本地缓存获取数据", notes="使用本地缓存获取数据")
    public RestResponse<UserInfo> getLocalache(@PathVariable(name = "id") Long id){
        UserInfo userInfo = userLocalCache.getCache(id);
        return RestResponse.buildSuccessResponse(userInfo);
    }

    @PostMapping("/remote_cache/{id}")
    @ApiOperation(value="使用分布式缓存获取数据", notes="使用分布式缓存获取数据")
    public RestResponse<UserInfo> getRemoteCache(@PathVariable(name = "id") Long id){
        UserInfo userInfo = userRemoteCache.getCache(id);
        return RestResponse.buildSuccessResponse(userInfo);
    }

}
