package org.opensourceframework.canal.adapter.demo.boot.config.opensourceframework_aliorder;

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
@ESDocument(table = "order_member_info", index = "order_member_info", isHumpCol = true,
        destination = "CANAL_ALI_ORDER_TOPIC", groupId = "opensourceframework_aliorder",
        dataSourceKey = "opensourceframework_aliorder", outerAdapterKey = "opensourceframework_aliorder")
public class OrderMemberInfo extends Document {
    private String rentRecordNo;
    private Integer memberGrade;
    private Integer productType;
    private Integer leaseType;
    @ESField(column = "rentDiscount" , type="scaled_float" ,scalingFactor = 1000)
    private BigDecimal rentDiscount;
    @ESField(column = "buyoutDiscount" , type="scaled_float" ,scalingFactor = 1000)
    private BigDecimal buyoutDiscount;
    @ESField(column = "renewalDiscount" , type="scaled_float" ,scalingFactor = 1000)
    private BigDecimal renewalDiscount;
    protected Date createOn;
}
