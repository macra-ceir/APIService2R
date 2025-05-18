package com.gl.ceir.config.model.app;

import javax.persistence.Transient;

public class GrievanceFilterRequest {
	public Long userId;
	public String userType;
	public String startDate;
	public String endDate;
	public String modifiedOn;
	public String txnId;
	public String grievanceId;
	private int grievanceStatus;
	private String searchString;
	private Long featureId;
	private String filterUserName;
	private String filterUserType;
	private Long userTypeId;
	private String raisedBy;
	private String orderColumnName;
	private String order;
	private String publicIp;
	private String browser;
	
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGrievanceId() {
		return grievanceId;
	}
	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}
	public int getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(int grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
	public String getFilterUserName() {
		return filterUserName;
	}
	public void setFilterUserName(String filterUserName) {
		this.filterUserName = filterUserName;
	}
	public String getFilterUserType() {
		return filterUserType;
	}
	public void setFilterUserType(String filterUserType) {
		this.filterUserType = filterUserType;
	}
	
	public Long getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getOrderColumnName() {
		return orderColumnName;
	}
	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	@Override
	public String toString() {
		return "GrievanceFilterRequest [userId=" + userId + ", userType=" + userType + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", modifiedOn=" + modifiedOn + ", txnId=" + txnId + ", grievanceId="
				+ grievanceId + ", grievanceStatus=" + grievanceStatus + ", searchString=" + searchString
				+ ", featureId=" + featureId + ", filterUserName=" + filterUserName + ", filterUserType="
				+ filterUserType + ", userTypeId=" + userTypeId + ", raisedBy=" + raisedBy + ", orderColumnName="
				+ orderColumnName + ", order=" + order + ", publicIp=" + publicIp + ", browser=" + browser + "]";
	}
}
