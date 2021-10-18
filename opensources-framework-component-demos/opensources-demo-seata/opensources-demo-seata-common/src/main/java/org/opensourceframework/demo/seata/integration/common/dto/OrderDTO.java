package org.opensourceframework.demo.seata.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单信息
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@Data
public class OrderDTO implements Serializable {

    private String orderNo;

    private String userId;

    private String commodityCode;

    private Integer orderCount;

    private BigDecimal orderAmount;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
