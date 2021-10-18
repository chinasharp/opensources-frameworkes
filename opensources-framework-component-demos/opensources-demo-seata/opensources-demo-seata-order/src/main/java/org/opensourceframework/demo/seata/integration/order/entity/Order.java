package org.opensourceframework.demo.seata.integration.order.entity;

import org.opensourceframework.base.eo.BaseEo;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * <p>
 *
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Table(name="t_order")
public class Order extends BaseEo {

    private static final long serialVersionUID = 1L;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "commodity_code")
    private String commodityCode;

    @Column(name = "count")
    private Integer count;

    @Column(name = "amount")
    private Double amount;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "TOrder{" +
        ", id=" + id +
        ", orderNo=" + orderNo +
        ", userId=" + userId +
        ", commodityCode=" + commodityCode +
        ", count=" + count +
        ", amount=" + amount +
        "}";
    }
}
