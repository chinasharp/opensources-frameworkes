package org.opensourceframework.canal.adapter.demo.boot.config.opensourceframework_telemarket;

import org.opensourceframework.canal.adapter.es.core.annotation.ESDocument;
import org.opensourceframework.canal.adapter.es.core.annotation.ESField;
import org.opensourceframework.canal.adapter.es.core.config.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 注解定义esmapping配置 单表数据同步MappingD Demo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ESDocument(table = "customer_info", index = "customer_info", isHumpCol = true,
        destination = "CANAL_TELEMARKET_TOPIC", groupId = "opensourceframework_telemarket",
        dataSourceKey = "opensourceframework_telemarket", outerAdapterKey = "opensourceframework_telemarket")
public class CustomerInfo extends Document {
    private String idCard;
    private String phone;
    private String userName;
    private String customerSource;

    private Date createTime;
    private Date updateTime;
}
