package org.opensourceframework.demo.seata.integration.storage.entity;

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
@Table(name="t_storage")
public class Storage extends BaseEo {

    private static final long serialVersionUID = 1L;
    @Column(name = "commodity_code")
    private String commodityCode;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private Integer count;

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TStorage{" +
        ", id=" + id +
        ", commodityCode=" + commodityCode +
        ", name=" + name +
        ", count=" + count +
        "}";
    }
}
