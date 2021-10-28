#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api.dto.request;

import ${groupId}.base.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 请求Dto示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ApiModel(value = "DemoReqDto",description = "Demo 请求参数对象")
public class DemoReqDto extends BaseDto {
	@ApiModelProperty(name = "account" , value = "用户账号")
	private String account;

	@ApiModelProperty(name = "password" , value = "账号密码")
	private String password;

	@ApiModelProperty(name = "name" , value = "用户姓名")
	private String name;

	@ApiModelProperty(name = "phone" , value = "用户手机号")
	private String phone;

	@ApiModelProperty(name = "cardNo" , value = "会员卡号")
	private String memberCardNo;

	@ApiModelProperty(name = "address" , value = "住址")
	private String address;

	public DemoReqDto() {
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemberCardNo() {
		return memberCardNo;
	}

	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
