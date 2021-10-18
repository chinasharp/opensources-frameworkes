package org.opensourceframework.starter.encdec;

import org.opensourceframework.starter.encdec.configuration.EnableEncryptablePropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Configuration
@ConditionalOnProperty(
        name = {"jasypt.encryptor.bootstrap"},
        havingValue = "true",
        matchIfMissing = true
)
@Import({EnableEncryptablePropertiesConfiguration.class})
public class JasyptSpringCloudBootstrapConfiguration {
}
