package org.opensourceframework.starter.encdec;

import org.opensourceframework.starter.encdec.configuration.EnableEncryptablePropertiesConfiguration;
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
@Import({EnableEncryptablePropertiesConfiguration.class})
public class JasyptSpringBootAutoConfiguration {

}
