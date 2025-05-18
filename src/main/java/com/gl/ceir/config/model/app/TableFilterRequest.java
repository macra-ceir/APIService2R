package com.gl.ceir.config.model.app;

import java.util.List;

public class TableFilterRequest {
	private String dbName;

	private List<DateRange> dateRange;

	private String tableName;
	private String startDate;
	private String endDate;
	private String searchString;
	private Long reportnameId;
	private String groupBy;
	private List<String> columns;
	private String txnId;
	private boolean lastDate;
	private Integer typeFlag;
	private Integer dayDataLimit;
	private Long userId;
	private String userType;
	private Long featureId;
	private String publicIp;
	private String browser;
	private String newTxnID;

	private Long top ;

	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public Long getReportnameId() {
		return reportnameId;
	}
	public void setReportnameId(Long reportnameId) {
		this.reportnameId = reportnameId;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public boolean isLastDate() {
		return lastDate;
	}
	public void setLastDate(boolean lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getTypeFlag() {
		return typeFlag;
	}
	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}
	public Integer getDayDataLimit() {
		return dayDataLimit;
	}
	public void setDayDataLimit(Integer dayDataLimit) {
		this.dayDataLimit = dayDataLimit;
	}
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
	
	
	
	public String getNewTxnID() {
		return newTxnID;
	}
	public void setNewTxnID(String newTxnID) {
		this.newTxnID = newTxnID;
	}


	public Long getTop() {
		return top;
	}

	public void setTop(Long top) {
		this.top = top;
	}


	public List<DateRange> getDateRange() {
		return dateRange;
	}

	public void setDateRange(List<DateRange> dateRange) {
		this.dateRange = dateRange;
	}

	public TableFilterRequest(Long reportnameId, Long top, Integer typeFlag) {
		this.reportnameId = reportnameId;
		this.top = top;
		this.typeFlag = typeFlag;
	}

	@Override
	public String toString() {
		return "TableFilterRequest{" +
				"dbName='" + dbName + '\'' +
				", dateRange=" + dateRange +
				", tableName='" + tableName + '\'' +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", searchString='" + searchString + '\'' +
				", reportnameId=" + reportnameId +
				", groupBy='" + groupBy + '\'' +
				", columns=" + columns +
				", txnId='" + txnId + '\'' +
				", lastDate=" + lastDate +
				", typeFlag=" + typeFlag +
				", dayDataLimit=" + dayDataLimit +
				", userId=" + userId +
				", userType='" + userType + '\'' +
				", featureId=" + featureId +
				", publicIp='" + publicIp + '\'' +
				", browser='" + browser + '\'' +
				", newTxnID='" + newTxnID + '\'' +
				", top=" + top +
				'}';
	}
}
