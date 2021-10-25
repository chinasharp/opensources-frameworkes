package org.opensourceframework.center.demo.biz.dao.eo;

import org.opensourceframework.base.eo.BaseEo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 会员实体类
 *
 * @author yu.ce@foxmail.com
 * @date 2021-10-20 15:42
 * @since 1.0.0
 */
@Table(name = "demo_member")
public class DemoMemberEo extends BaseEo {
    @Column(name = "member_card_no")
    private String memberCardNo;

    /**
     * 积分
     */
    @Column(name = "accumulate_points")
    private Long accumulatePoints;

    /**
     * 等级
     */
    @Column(name = "level")
    private Integer level;

    public DemoMemberEo() {
    }

    public DemoMemberEo(String memberCardNo, Long accumulatePoints, Integer level) {
        this.memberCardNo = memberCardNo;
        this.accumulatePoints = accumulatePoints;
        this.level = level;
    }

    public String getMemberCardNo() {
        return memberCardNo;
    }

    public void setMemberCardNo(String memberCardNo) {
        this.memberCardNo = memberCardNo;
    }

    public Long getAccumulatePoints() {
        return accumulatePoints;
    }

    public void setAccumulatePoints(Long accumulatePoints) {
        this.accumulatePoints = accumulatePoints;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
