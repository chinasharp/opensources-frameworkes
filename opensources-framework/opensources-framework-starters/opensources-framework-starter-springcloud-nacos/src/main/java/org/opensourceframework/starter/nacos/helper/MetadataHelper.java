package org.opensourceframework.starter.nacos.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Nacos服务元数据详情
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public class MetadataHelper {
    /**
     * Dubbo Service Invoke Method List
     *
     * @param context
     * @return
     */
    public static Map<String , List<String>> getDubboServiceList(ApplicationContext context){
        Map<String , List<String>> serviceMethodMaps = Maps.newHashMap();

        Map<String, Object> dubboServiceMap = context.getBeansWithAnnotation(DubboService.class);
        if(MapUtils.isNotEmpty(dubboServiceMap)) {
            List<Class<?>> dubboServiceClassList = Lists.newArrayList();
            for (Map.Entry<String, Object> entry : dubboServiceMap.entrySet()) {
                Class<?> dubboServiceImplClass = entry.getValue().getClass();
                Class<?>[] dubboInterfaces = dubboServiceImplClass.getInterfaces();
                if (dubboInterfaces.length > 0) {
                    dubboServiceClassList.addAll(Arrays.asList(dubboInterfaces));
                } else {
                    dubboServiceClassList.add(dubboServiceImplClass);
                }
            }

            for (Class dubboClazz : dubboServiceClassList) {
                Method[] dubboServiceMethods = dubboClazz.getMethods();
                if (dubboServiceMethods.length > 0) {
                    List<String> methodNameList = Lists.newArrayList();
                    for (Method method : dubboServiceMethods) {
                        methodNameList.add(invokeMethodInfo(method));
                    }
                    if (CollectionUtils.isNotEmpty(methodNameList)) {
                        serviceMethodMaps.put(dubboClazz.getName(), methodNameList);
                    }
                }
            }
        }
        return serviceMethodMaps;
    }

    private static String invokeMethodInfo(Method method){
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        if(method.getParameterCount() == 0){
            sb.append("()");
        }else {
            sb.append("(");
            Arrays.stream(method.getParameterTypes()).forEach(clazz ->{
                sb.append(clazz.getName());
            });
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * SpringCloud Service Url List
     *
     * @param context
     * @return
     */
    public static Map<String , List<String>> getSpringCloudServiceList(ApplicationContext context){
        Map<String , List<String>> serviceUrlMaps = Maps.newHashMap();

        Map<String, Object> restControllerMap = context.getBeansWithAnnotation(RestController.class);
        if(MapUtils.isNotEmpty(restControllerMap)) {
            for (Map.Entry<String, Object> entry : restControllerMap.entrySet()) {
                Class<?> restClass = entry.getValue().getClass();
                if (restClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping classAnnotation = restClass.getAnnotation(RequestMapping.class);
                    for (String classReqPath : classAnnotation.value()) {
                        List<String> serviceUrlLists = Lists.newArrayList();
                        Method[] methodArray = restClass.getMethods();
                        for (Method method : methodArray) {
                            String[] methodReqPathArray = null;
                            if (method.isAnnotationPresent(RequestMapping.class)) {
                                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                                methodReqPathArray = annotation.value();
                            }
                            if (method.isAnnotationPresent(PostMapping.class)) {
                                PostMapping annotation = method.getAnnotation(PostMapping.class);
                                methodReqPathArray = annotation.value();
                            }
                            if (method.isAnnotationPresent(GetMapping.class)) {
                                GetMapping annotation = method.getAnnotation(GetMapping.class);
                                methodReqPathArray = annotation.value();
                            }

                            if (methodReqPathArray != null) {
                                Arrays.stream(methodReqPathArray).forEach(reqPath -> {
                                    serviceUrlLists.add(classReqPath + reqPath);
                                });
                            }
                        }

                        if (CollectionUtils.isNotEmpty(serviceUrlLists)) {
                            serviceUrlMaps.put(restClass.getName(), serviceUrlLists);
                        }
                    }
                }
            }
        }
        return serviceUrlMaps;
    }

}
