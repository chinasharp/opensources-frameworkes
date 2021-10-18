package org.opensourceframework.center.demo.biz.dao.eo;

import org.opensourceframework.base.eo.BaseEo;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Eo实体类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Table(name = "demo_user")
public class DemoUserEo extends BaseEo {
	/**
	 * 账号
	 */
	@Column(name = "account")
	private String account;

	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 手机号
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 会员卡号
	 */
	@Column(name = "member_card_no")
	private String memberCardNo;

	/**
	 * 地址
	 */
	@Column(name = "address")
	private String address;

	public DemoUserEo() {
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
