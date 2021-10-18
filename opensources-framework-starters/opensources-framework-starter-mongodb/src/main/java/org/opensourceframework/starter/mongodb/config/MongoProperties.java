/*
 * @(#)MongoProperties.java 1.0 2019-06-21
 *
 * Copyright (c) 2017, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensourceframework.starter.mongodb.config;

import org.opensourceframework.component.mongodb.config.MongoDataBaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Mongo edas配置实体类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.mongodb.registryvo")
public class MongoProperties extends MongoDataBaseConfig {
    public MongoProperties(){}

}
