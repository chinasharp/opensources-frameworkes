package org.opensourceframework.starter.nacos.helper;

import org.apache.commons.collections.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2021/3/25.
 */
public class MetadataHelper {
    
    public static String getMetaDataForRestFull(ApplicationContext context){
        Map<String, Object> restMap = context.getBeansWithAnnotation(RestController.class);

        StringBuilder restServicePathSb = new StringBuilder();
        for (Map.Entry<String, Object> entry : restMap.entrySet()) {
            Class<?> restClass = entry.getValue().getClass();
            if (restClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping classAnnotation = restClass.getAnnotation(RequestMapping.class);
                String[] classValueArray = classAnnotation.value();
                for (String classValue : classValueArray) {
                    Method[] methodArray = restClass.getMethods();
                    for (Method method : methodArray) {
                        String[] methodValueArray = null;
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                            methodValueArray = annotation.value();
                        }
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            PostMapping annotation = method.getAnnotation(PostMapping.class);
                            methodValueArray = annotation.value();
                        }
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping annotation = method.getAnnotation(GetMapping.class);
                            methodValueArray = annotation.value();
                        }

                        if (methodValueArray != null) {
                            for (String methodValue : methodValueArray) {
                                if (restServicePathSb.length() > 0) {
                                    restServicePathSb.append(",");
                                }
                                restServicePathSb.append(classValue).append(methodValue);
                            }
                        }
                    }
                }
            }
        }
        return restServicePathSb.toString();
    }

}
