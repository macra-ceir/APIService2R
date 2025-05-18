package com.gl.ceir.config.model.app;

public class FileDumpFilterRequest {
	public Long userId;
	public String userType;
	public String startDate;
	public String endDate;
	public Integer fileType;
	public Integer serviceDump;
	public String searchString;
	public Long featureId;
	public String publicIp;
	public String browser;
	public String fileName;
	public String order;
	public String orderColumnName;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
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
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public Integer getServiceDump() {
		return serviceDump;
	}
	public void setServiceDump(Integer serviceDump) {
		this.serviceDump = serviceDump;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getOrderColumnName() {
		return orderColumnName;
	}
	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}
	@Override
	public String toString() {
		return "FileDumpFilterRequest [userId=" + userId + ", userType=" + userType + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", fileType=" + fileType + ", serviceDump=" + serviceDump + ", searchString="
				+ searchString + ", featureId=" + featureId + ", publicIp=" + publicIp + ", browser=" + browser
				+ ", fileName=" + fileName + ", order=" + order + ", orderColumnName=" + orderColumnName + "]";
	}
}
