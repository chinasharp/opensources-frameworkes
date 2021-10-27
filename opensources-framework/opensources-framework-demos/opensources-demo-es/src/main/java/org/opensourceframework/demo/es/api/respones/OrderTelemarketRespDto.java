package org.opensourceframework.demo.es.api.respones;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/25 下午7:49
 */
public class OrderTelemarketRespDto implements Serializable {
	private Long id;
	private Long orderId;
	private String orderNo;
	private Long telemarketOrderId;
	private Long orderTelemarkId;
	private String telemarketId;
	private Long logId;
	private Long userId;
	private String telUserId;
	private String opId;
	private Long extendId;
	private String channelNo;
	private String logisticsNo;
	private String returnLogisticsNo;
	private Integer stockFlag;
	private String snCode;
	private String imei;
	private Date applyTime;
	private Date approvalTime;
	private Date applyReCheckTime;
	private Date terminateApplyTime;
	private Date payTime;
	private Date signTime;
	private Date backTime;
	private Date returnTime;
	private Date compensationTime;
	private Date createOn;
	private Date updateOn;
	private Date state;
	private Integer creditState;
	private String agreementUrl;
	private String sealAgreementUrl;
	private String evid;
	private String signServiceId;
	private BigDecimal discountFee;
	private Integer productType;
	private Integer applyInvoice;
	private Integer invoiceEnd;
	private String channelType;
	private String couponId;
	private String couponAmount;
	private BigDecimal salePayAmount;
	private String loginName;
	private Integer orderType;
	private String storeNo;
	private String bfReceivePhotoUrl;
	private String receivePhotoUrl;
	private Integer rentType;
	private Integer depositSource;
	private Integer repeatOrder;
	private String inviteCode;
	private String zoneCode;
	private Integer cooperationMode;
	private String remark;
	private String parentStoreNo;
	private String transactionId;
	private String zmOrderNo;
	private String zmUserId;
	private Integer storeType;
	private BigDecimal remissionDeposit;
	private Integer orderVersion;
	private Integer fundId;
	private Integer merchantId;
	private Integer deleted;
	private String reletAgreementUrl;
	private String reletSealAgreementUrl;
	private String sourceOrder;
	private Integer oldUser;
	private Integer step;
	private String loanAidType;
	private String withholdAgreemUrl;

	public OrderTelemarketRespDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getTelemarketOrderId() {
		return telemarketOrderId;
	}

	public void setTelemarketOrderId(Long telemarketOrderId) {
		this.telemarketOrderId = telemarketOrderId;
	}

	public Long getOrderTelemarkId() {
		return orderTelemarkId;
	}

	public void setOrderTelemarkId(Long orderTelemarkId) {
		this.orderTelemarkId = orderTelemarkId;
	}

	public String getTelemarketId() {
		return telemarketId;
	}

	public void setTelemarketId(String telemarketId) {
		this.telemarketId = telemarketId;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTelUserId() {
		return telUserId;
	}

	public void setTelUserId(String telUserId) {
		this.telUserId = telUserId;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public Long getExtendId() {
		return extendId;
	}

	public void setExtendId(Long extendId) {
		this.extendId = extendId;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getReturnLogisticsNo() {
		return returnLogisticsNo;
	}

	public void setReturnLogisticsNo(String returnLogisticsNo) {
		this.returnLogisticsNo = returnLogisticsNo;
	}

	public Integer getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(Integer stockFlag) {
		this.stockFlag = stockFlag;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	public Date getApplyReCheckTime() {
		return applyReCheckTime;
	}

	public void setApplyReCheckTime(Date applyReCheckTime) {
		this.applyReCheckTime = applyReCheckTime;
	}

	public Date getTerminateApplyTime() {
		return terminateApplyTime;
	}

	public void setTerminateApplyTime(Date terminateApplyTime) {
		this.terminateApplyTime = terminateApplyTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Date getCompensationTime() {
		return compensationTime;
	}

	public void setCompensationTime(Date compensationTime) {
		this.compensationTime = compensationTime;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public Date getState() {
		return state;
	}

	public void setState(Date state) {
		this.state = state;
	}

	public Integer getCreditState() {
		return creditState;
	}

	public void setCreditState(Integer creditState) {
		this.creditState = creditState;
	}

	public String getAgreementUrl() {
		return agreementUrl;
	}

	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}

	public String getSealAgreementUrl() {
		return sealAgreementUrl;
	}

	public void setSealAgreementUrl(String sealAgreementUrl) {
		this.sealAgreementUrl = sealAgreementUrl;
	}

	public String getEvid() {
		return evid;
	}

	public void setEvid(String evid) {
		this.evid = evid;
	}

	public String getSignServiceId() {
		return signServiceId;
	}

	public void setSignServiceId(String signServiceId) {
		this.signServiceId = signServiceId;
	}

	public BigDecimal getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getApplyInvoice() {
		return applyInvoice;
	}

	public void setApplyInvoice(Integer applyInvoice) {
		this.applyInvoice = applyInvoice;
	}

	public Integer getInvoiceEnd() {
		return invoiceEnd;
	}

	public void setInvoiceEnd(Integer invoiceEnd) {
		this.invoiceEnd = invoiceEnd;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}

	public BigDecimal getSalePayAmount() {
		return salePayAmount;
	}

	public void setSalePayAmount(BigDecimal salePayAmount) {
		this.salePayAmount = salePayAmount;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getBfReceivePhotoUrl() {
		return bfReceivePhotoUrl;
	}

	public void setBfReceivePhotoUrl(String bfReceivePhotoUrl) {
		this.bfReceivePhotoUrl = bfReceivePhotoUrl;
	}

	public String getReceivePhotoUrl() {
		return receivePhotoUrl;
	}

	public void setReceivePhotoUrl(String receivePhotoUrl) {
		this.receivePhotoUrl = receivePhotoUrl;
	}

	public Integer getRentType() {
		return rentType;
	}

	public void setRentType(Integer rentType) {
		this.rentType = rentType;
	}

	public Integer getDepositSource() {
		return depositSource;
	}

	public void setDepositSource(Integer depositSource) {
		this.depositSource = depositSource;
	}

	public Integer getRepeatOrder() {
		return repeatOrder;
	}

	public void setRepeatOrder(Integer repeatOrder) {
		this.repeatOrder = repeatOrder;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public Integer getCooperationMode() {
		return cooperationMode;
	}

	public void setCooperationMode(Integer cooperationMode) {
		this.cooperationMode = cooperationMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentStoreNo() {
		return parentStoreNo;
	}

	public void setParentStoreNo(String parentStoreNo) {
		this.parentStoreNo = parentStoreNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getZmOrderNo() {
		return zmOrderNo;
	}

	public void setZmOrderNo(String zmOrderNo) {
		this.zmOrderNo = zmOrderNo;
	}

	public String getZmUserId() {
		return zmUserId;
	}

	public void setZmUserId(String zmUserId) {
		this.zmUserId = zmUserId;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public BigDecimal getRemissionDeposit() {
		return remissionDeposit;
	}

	public void setRemissionDeposit(BigDecimal remissionDeposit) {
		this.remissionDeposit = remissionDeposit;
	}

	public Integer getOrderVersion() {
		return orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getReletAgreementUrl() {
		return reletAgreementUrl;
	}

	public void setReletAgreementUrl(String reletAgreementUrl) {
		this.reletAgreementUrl = reletAgreementUrl;
	}

	public String getReletSealAgreementUrl() {
		return reletSealAgreementUrl;
	}

	public void setReletSealAgreementUrl(String reletSealAgreementUrl) {
		this.reletSealAgreementUrl = reletSealAgreementUrl;
	}

	public String getSourceOrder() {
		return sourceOrder;
	}

	public void setSourceOrder(String sourceOrder) {
		this.sourceOrder = sourceOrder;
	}

	public Integer getOldUser() {
		return oldUser;
	}

	public void setOldUser(Integer oldUser) {
		this.oldUser = oldUser;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getLoanAidType() {
		return loanAidType;
	}

	public void setLoanAidType(String loanAidType) {
		this.loanAidType = loanAidType;
	}

	public String getWithholdAgreemUrl() {
		return withholdAgreemUrl;
	}

	public void setWithholdAgreemUrl(String withholdAgreemUrl) {
		this.withholdAgreemUrl = withholdAgreemUrl;
	}
}
