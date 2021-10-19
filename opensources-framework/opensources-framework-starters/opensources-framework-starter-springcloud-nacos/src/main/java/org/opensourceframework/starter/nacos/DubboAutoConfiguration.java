package org.opensourceframework.starter.nacos;

import com.alibaba.cloud.dubbo.registry.SpringCloudRegistryFactory;
import org.opensourceframework.starter.nacos.helper.ReflectHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.logger.Level;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * 自动加载配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
public class DubboAutoConfiguration {
    /**
     * 解决SpringCloudRegistryFactory SPI生成实例时，反射时无法获取属性applicationContext 报错的问题
     *
     * @return
     */
    @Bean(name = "applicationContext")
    @ConditionalOnClass(value = {SpringCloudRegistryFactory.class, SimpleDiscoveryClientAutoConfiguration.class,
            CommonsClientAutoConfiguration.class})
    public ConfigurableApplicationContext configurableApplicationContext(){
        return (ConfigurableApplicationContext) ReflectHelper.getStaticField(SpringCloudRegistryFactory.class , "applicationContext");
    }

    @Bean
    public Level level(){
        Level level = Level.INFO;
        String envLevel = System.getProperty("opensourceframework.env.logger.level");
        if(StringUtils.isNotBlank(envLevel)){
            if(Level.INFO.toString().equalsIgnoreCase(envLevel)){
                level = Level.INFO;
            }
            if(Level.DEBUG.toString().equalsIgnoreCase(envLevel)){
                level = Level.DEBUG;
            }
        }
        return level;
    }

    @Bean
    public File file(){
        String dirPath = System.getProperty("opensourceframework.env.logger.workdir");
        if(StringUtils.isBlank(dirPath)){
            dirPath = getClass().getClassLoader().getResource("").getPath();
        }
        dirPath = dirPath.concat(File.separator).concat("dubbo.log");
        return new File(dirPath);
    }
}

