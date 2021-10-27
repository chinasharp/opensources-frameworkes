package org.opensourceframework.demo.seclog.controller;

import java.util.List;

public class QueryJinJianParam {

    private String pageNo;
    private String pageSize;

    //客户id
    private String customerId;
    //客户名称
    private String customerName;
    //用户id
    private String userId;
    //下属姓名
    private String subordinateName;
    //下属id
    private String subordinateId;
    //证件类型
    private String certType;
    //证件号码
    private String certID;
    //手机号码
    private String mobile;
    //渠道号
    private String channelId;
    //申请日期-开始
    private String applyStartDate;
    //申请日期-结束
    private String applyEndDate;
    //产品名称
    private String productName;
    //申请单编号
    private String applyId;
    //业务类型(授信/用款)
    private String bizType;
    //客户经理id
    private String manageId;
    //客户经理姓名
    private String mangeName;
    //营销中心
    private String marketingId;
    //所属机构
    private String orgId;
    //合作机构
    private String cooprateOrgId;
    //退回件
    private String backcheck;
    //重审件
    private String recheck;
    //新增件
    private String newcheck;
    //状态类型
    private String statusType;
    //操作人id
    private String operatorId;
    //当前节点
    private String taskNode;
    //状态
    private String status;
    //单选框（全部，未分配）
    private String radios;
    //审核结果
    private String approveResult;
    //拒绝原因
    private String refuseReason;
    //待办类型（全部，个人）
    private String daibanType;
    //已办类型（全部，个人）
    private String yibanType;
    //审核完成日期-开始
    private String finishStartDate;
    //审核完成日期-结束
    private String finishEndDate;
    //业务员接单开始时间
    private String receiveStartDate;
    //业务员接单结束时间
    private String receiveEndDate;
    //异常原因
    private String exceptionType;
    //是否首次加载
    private String isFirst;
    //处理人
    private String handelPersonName;
    //处理人列表
    private List<String> handelPersonList;
    //排序列名
    private String sort;
    //排序命令
    private String order;
    //营销索引
    private String marketingClew;
    //创建时间
    private String createTime;
    //优先级
    private String priority;
    //进件单类型
    private String autoPassFlag;
    //系统拒绝代码
    private String dtrefuseReason;
    //类型--补件
    private String patchStatus;
    //补件超时标志
    private String patchOverTime;
    private String productId;//产品id

    private List<String> firstCheckOperatorList; //一审处理人
    private String firstCheckOperatorName; //一审处理人姓名
    private String approveCode; //审批代码
    private String refuseCode; //拒绝代码

    /**
     * 申请人类型[EnumApplyCustomerType]
     */
    private String applyCustomerType;

    /**
     * 夫妻同申号
     */
    private String coupleSameApplyNo;


    private String productNo;

    private String finalStatus;//是否终态
    private String exceptionFlag;//异常标志

    public String getApplyCustomerType() {
        return applyCustomerType;
    }

    public void setApplyCustomerType(String applyCustomerType) {
        this.applyCustomerType = applyCustomerType;
    }

    public String getCoupleSameApplyNo() {
        return coupleSameApplyNo;
    }

    public void setCoupleSameApplyNo(String coupleSameApplyNo) {
        this.coupleSameApplyNo = coupleSameApplyNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPatchOverTime() {
		return patchOverTime;
	}

	public void setPatchOverTime(String patchOverTime) {
		this.patchOverTime = patchOverTime;
	}

	public String getPatchStatus() {
		return patchStatus;
	}

	public void setPatchStatus(String patchStatus) {
		this.patchStatus = patchStatus;
	}

	public String getDtrefuseReason() {
		return dtrefuseReason;
	}

	public void setDtrefuseReason(String dtrefuseReason) {
		this.dtrefuseReason = dtrefuseReason;
	}

	public String getAutoPassFlag() {
		return autoPassFlag;
	}

	public void setAutoPassFlag(String autoPassFlag) {
		this.autoPassFlag = autoPassFlag;
	}

	public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMarketingClew() {
        return marketingClew;
    }

    public void setMarketingClew(String marketingClew) {
        this.marketingClew = marketingClew;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getHandelPersonList() {
        return handelPersonList;
    }

    public void setHandelPersonList(List<String> handelPersonList) {
        this.handelPersonList = handelPersonList;
    }

    public String getHandelPersonName() {
        return handelPersonName;
    }

    public void setHandelPersonName(String handelPersonName) {
        this.handelPersonName = handelPersonName;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getReceiveStartDate() {
        return receiveStartDate;
    }

    public void setReceiveStartDate(String receiveStartDate) {
        this.receiveStartDate = receiveStartDate;
    }

    public String getReceiveEndDate() {
        return receiveEndDate;
    }

    public void setReceiveEndDate(String receiveEndDate) {
        this.receiveEndDate = receiveEndDate;
    }

    public String getFinishStartDate() {
        return finishStartDate;
    }

    public void setFinishStartDate(String finishStartDate) {
        this.finishStartDate = finishStartDate;
    }

    public String getFinishEndDate() {
        return finishEndDate;
    }

    public void setFinishEndDate(String finishEndDate) {
        this.finishEndDate = finishEndDate;
    }

    public String getYibanType() {
        return yibanType;
    }

    public void setYibanType(String yibanType) {
        this.yibanType = yibanType;
    }

    public String getDaibanType() {
        return daibanType;
    }

    public void setDaibanType(String daibanType) {
        this.daibanType = daibanType;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getRadios() {
        return radios;
    }

    public void setRadios(String radios) {
        this.radios = radios;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubordinateId() {
        return subordinateId;
    }

    public void setSubordinateId(String subordinateId) {
        this.subordinateId = subordinateId;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getBackcheck() {
        return backcheck;
    }

    public void setBackcheck(String backcheck) {
        this.backcheck = backcheck;
    }

    public String getRecheck() {
        return recheck;
    }

    public void setRecheck(String recheck) {
        this.recheck = recheck;
    }

    public String getNewcheck() {
        return newcheck;
    }

    public void setNewcheck(String newcheck) {
        this.newcheck = newcheck;
    }

    public String getSubordinateName() {
        return subordinateName;
    }

    public void setSubordinateName(String subordinateName) {
        this.subordinateName = subordinateName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertID() {
        return certID;
    }

    public void setCertID(String certID) {
        this.certID = certID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(String applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public String getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(String applyEndDate) {
        this.applyEndDate = applyEndDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getManageId() {
        return manageId;
    }

    public void setManageId(String manageId) {
        this.manageId = manageId;
    }

    public String getMangeName() {
        return mangeName;
    }

    public void setMangeName(String mangeName) {
        this.mangeName = mangeName;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCooprateOrgId() {
        return cooprateOrgId;
    }

    public void setCooprateOrgId(String cooprateOrgId) {
        this.cooprateOrgId = cooprateOrgId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public List<String> getFirstCheckOperatorList() {
        return firstCheckOperatorList;
    }

    public void setFirstCheckOperatorList(List<String> firstCheckOperatorList) {
        this.firstCheckOperatorList = firstCheckOperatorList;
    }

    public String getFirstCheckOperatorName() {
        return firstCheckOperatorName;
    }

    public void setFirstCheckOperatorName(String firstCheckOperatorName) {
        this.firstCheckOperatorName = firstCheckOperatorName;
    }

    public String getApproveCode() {
        return approveCode;
    }

    public void setApproveCode(String approveCode) {
        this.approveCode = approveCode;
    }

    public String getRefuseCode() {
        return refuseCode;
    }

    public void setRefuseCode(String refuseCode) {
        this.refuseCode = refuseCode;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(String exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }
}
