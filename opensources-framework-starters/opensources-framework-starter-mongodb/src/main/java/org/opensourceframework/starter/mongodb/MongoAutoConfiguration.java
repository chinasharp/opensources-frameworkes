/*
 * @(#)MongoAutoConfiguration.java 1.0 2019-06-21
 *
 * Copyright (c) 2017, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensourceframework.starter.mongodb;

import org.opensourceframework.component.mongodb.MongoDateBaseFactory;
import org.opensourceframework.component.mongodb.config.MongoDataBaseConfig;
import org.opensourceframework.starter.mongodb.config.MongoProperties;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mongo Auto Configuration类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({MongoProperties.class})
public class MongoAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MongoAutoConfiguration.class);

    @Autowired
    private MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    private MongoDateBaseFactory mongoDateBaseFactory;

    @Bean("mongoDateBaseFactory")
    public MongoDateBaseFactory mongoDateBaseFactory(){
        return new MongoDateBaseFactory(mongoDataBaseConfig);
    }

    @Bean("mongoClient")
    public MongoClient mongoClient(){
        MongoClient mongoClient = mongoDateBaseFactory.createClient();
        logger.info("Connected to MongoDB server:{} successs." , mongoDataBaseConfig.getServerAddresses());
        return mongoClient;
    }
}
