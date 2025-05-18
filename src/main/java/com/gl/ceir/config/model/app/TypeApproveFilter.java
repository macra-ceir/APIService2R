package com.gl.ceir.config.model.app;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

public class TypeApproveFilter {
	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate startDate;
	
	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate endDate;
	
	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate modifiedOn;
	
	private Integer status;
	private Integer adminStatus;
	private String  tac;
	private Long userId;
	private String searchString;
	private String txnId;
	private String userType;
	private long featureId;
	private String filterUserType;
	private String filterUserName;
	private Long userTypeId;
	private Long productName;
	private int modelNumber;
	private String displayName;
	private String countryName;
	private String orderColumnName;
	private String order;
	private String publicIp;
	private String browser;
	private String trademark;
	
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public Integer getAdminStatus() {
		return adminStatus;
	}
	public void setAdminStatus(Integer adminStatus) {
		this.adminStatus = adminStatus;
	}
	public long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getFilterUserType() {
		return filterUserType;
	}
	public void setFilterUserType(String filterUserType) {
		this.filterUserType = filterUserType;
	}
	public String getFilterUserName() {
		return filterUserName;
	}
	public void setFilterUserName(String filterUserName) {
		this.filterUserName = filterUserName;
	}
	public Long getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}
	public LocalDate getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDate modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Long getProductName() {
		return productName;
	}
	public void setProductName(Long productName) {
		this.productName = productName;
	}
	public int getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(int modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
	public String getTrademark() {
		return trademark;
	}
	public void setTrademark(String trademark) {
		this.trademark = trademark;
	}
	@Override
	public String toString() {
		return "TypeApproveFilter [startDate=" + startDate + ", endDate=" + endDate + ", modifiedOn=" + modifiedOn
				+ ", status=" + status + ", adminStatus=" + adminStatus + ", tac=" + tac + ", userId=" + userId
				+ ", searchString=" + searchString + ", txnId=" + txnId + ", userType=" + userType + ", featureId="
				+ featureId + ", filterUserType=" + filterUserType + ", filterUserName=" + filterUserName
				+ ", userTypeId=" + userTypeId + ", productName=" + productName + ", modelNumber=" + modelNumber
				+ ", displayName=" + displayName + ", countryName=" + countryName + ", orderColumnName="
				+ orderColumnName + ", order=" + order + ", publicIp=" + publicIp + ", browser=" + browser
				+ ", trademark=" + trademark + "]";
	}
}
