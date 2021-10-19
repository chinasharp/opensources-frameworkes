package org.opensourceframework.starter.nacos.registry.springcloud;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosRegistrationCustomizer;
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
        String restFullMetaData = MetadataHelper.getMetaDataForRestFull(context);

        if (StringUtils.isNotBlank(restFullMetaData)) {
            metadata.put("spring cloud.metadata-service.urls", restFullMetaData);
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
