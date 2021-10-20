package org.opensourceframework.starter.nacos.registry.springcloud;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosRegistrationCustomizer;
import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.opensourceframework.starter.nacos.helper.MetadataHelper;
import org.opensourceframework.starter.nacos.helper.ReflectHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 扩展Spring Cloud RestFull信息
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class NacosRegistration extends com.alibaba.cloud.nacos.registry.NacosRegistration {
    private static final Logger logger = LoggerFactory.getLogger(NacosRegistration.class);

    public NacosRegistration(List<NacosRegistrationCustomizer> registrationCustomizers, NacosDiscoveryProperties nacosDiscoveryProperties, ApplicationContext context) {
        super(registrationCustomizers, nacosDiscoveryProperties, context);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();

        Map<String, String> metadata = getNacosProperties().getMetadata();
        ApplicationContext context = getApplicationContext();

        Map<String , List<String>> serviceListMap = MetadataHelper.getDubboServiceList(context);
        if (MapUtils.isNotEmpty(serviceListMap)) {
            metadata.put("dubbo.services.invoke.methods", serviceListMap.toString());
        }

        serviceListMap = MetadataHelper.getSpringCloudServiceList(context);

        if (MapUtils.isNotEmpty(serviceListMap)) {
            metadata.put("springcloud.services.urls", serviceListMap.toString());
        }

    }

    private NacosDiscoveryProperties getNacosProperties() {
        NacosDiscoveryProperties discoveryProperties = (NacosDiscoveryProperties) ReflectHelper.getFieldValue(this, "nacosDiscoveryProperties");
        return discoveryProperties;
    }

    private ApplicationContext getApplicationContext() {
        ApplicationContext context = (ApplicationContext) ReflectHelper.getFieldValue(this, "context");
        return context;
    }
}
