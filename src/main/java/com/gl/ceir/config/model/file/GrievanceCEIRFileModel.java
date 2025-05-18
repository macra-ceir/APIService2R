package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class GrievanceCEIRFileModel {
	
	@CsvBindByName(column = "Grievance ID")
	@CsvBindByPosition(position = 3)
	private String grievanceId;
	
	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 7)
	private String grievanceStatus;
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 2)
	private String txnId;
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String createdOn;

	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifiedOn;

	@CsvBindByName(column = "REMARKS")
	@CsvBindByPosition(position = 8)
	private String remarks;
	
	@CsvBindByName(column = "User ID")
	@CsvBindByPosition(position = 4)
	private String userId;
	
	@CsvBindByName(column = "User Type")
	@CsvBindByPosition(position = 6)
	private String userType;
	
	@CsvBindByName(column = "Raised By")
	@CsvBindByPosition(position = 5)
	private String raisedBy;
	
	@CsvBindByName(column = "File")
	@CsvBindByPosition(position = 9)
	private String fileName;

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	public String getGrievanceStatus() {
		return grievanceStatus;
	}

	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "GrievanceCEIRFileModel [grievanceId=" + grievanceId + ", grievanceStatus=" + grievanceStatus
				+ ", txnId=" + txnId + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", remarks="
				+ remarks + ", userId=" + userId + ", userType=" + userType + ", raisedBy=" + raisedBy + ", fileName="
				+ fileName + "]";
	}
	
}
