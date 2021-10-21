package org.opensourceframework.center.demo.api.dto.request.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.opensourceframework.base.dto.BaseDto;

/**
 * 会员请求Dto
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ApiModel(value = "DemoMemberReqDto",description = "Demo Member 请求参数对象")
public class DemoMemberReqDto extends BaseDto {
	@ApiModelProperty(name = "cardNo" , value = "会员卡号")
	private String memberCardNo;

	@ApiModelProperty(name = "accumulatePoints" , value = "会员积分")
	private String accumulatePoints;

	@ApiModelProperty(name = "level", value = "会员等级")
	private Integer level;

	public DemoMemberReqDto() {
	}

	public String getMemberCardNo() {
		return memberCardNo;
	}

	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}

	public String getAccumulatePoints() {
		return accumulatePoints;
	}

	public void setAccumulatePoints(String accumulatePoints) {
		this.accumulatePoints = accumulatePoints;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
