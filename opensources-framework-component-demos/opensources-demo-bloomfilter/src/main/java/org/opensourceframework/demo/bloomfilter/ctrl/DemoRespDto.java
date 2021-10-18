package org.opensourceframework.demo.bloomfilter.ctrl;

import org.opensourceframework.base.dto.BaseDto;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
public class DemoRespDto extends BaseDto {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 会员卡号
     */
    private String memberCardNo;

    /**
     * 地址
     */
    private String address;

    public DemoRespDto() {
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
