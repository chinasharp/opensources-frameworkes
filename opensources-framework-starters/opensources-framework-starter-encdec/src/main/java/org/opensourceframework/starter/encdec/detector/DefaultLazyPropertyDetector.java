package org.opensourceframework.starter.encdec.detector;

import org.opensourceframework.starter.encdec.EncryptablePropertyDetector;
import org.opensourceframework.starter.encdec.util.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.util.Optional;

/**
 * Default Lazy property detector that delegates to a custom {@link EncryptablePropertyDetector} bean or initializes a
 * default {@link DefaultPropertyDetector}.
 *
 * @author Ulises Bocchio
 */
@Slf4j
public class DefaultLazyPropertyDetector implements EncryptablePropertyDetector {

    private final Singleton<EncryptablePropertyDetector> singleton;

    public DefaultLazyPropertyDetector(String prefix, String suffix, String customDetectorBeanName, BeanFactory bf) {
        singleton = new Singleton<>(() ->
                Optional.of(customDetectorBeanName)
                        .filter(bf::containsBean)
                        .map(name -> (EncryptablePropertyDetector) bf.getBean(name))
                        .map(bean -> {
                            log.info("Found Custom Detector Bean {} with name: {}", bean, customDetectorBeanName);
                            return bean;
                        })
                        .orElseGet(() -> {
                            log.info("Property Detector custom Bean not found with name '{}'. Initializing Default Property Detector", customDetectorBeanName);
                            return new DefaultPropertyDetector(prefix, suffix);
                        }));
    }

    @Override
    public boolean isEncrypted(String property) {
        return singleton.get().isEncrypted(property);
    }

    @Override
    public String unwrapEncryptedValue(String property) {
        return singleton.get().unwrapEncryptedValue(property);
    }
}
