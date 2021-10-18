package org.opensourceframework.demo.seata.integration.account.entity;

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
@Table(name = "t_account")
public class Account extends BaseEo {

    private static final long serialVersionUID = 1L;

    @Column(name="user_id")
    private String userId;

    @Column(name="amount")
    private Double amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TAccount{" +
        ", id=" + id +
        ", userId=" + userId +
        ", amount=" + amount +
        "}";
    }
}
