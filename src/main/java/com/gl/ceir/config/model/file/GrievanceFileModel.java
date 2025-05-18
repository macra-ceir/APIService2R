package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class GrievanceFileModel {
	
	@CsvBindByName(column = "Grievance ID")
	@CsvBindByPosition(position = 3)
	private String grievanceId;
	
	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 4)
	private String grievanceStatus;
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 2)
	private String txnId;

	//private String user;
	
	//private String userType;
	
//	@CsvBindByName(column = "CATEGORY")
//	@CsvBindByPosition(position = 5)
//	private String category;
	
	@CsvBindByName(column = "File")
	@CsvBindByPosition(position = 6)
	private String fileName;
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String createdOn;

	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifiedOn;

	@CsvBindByName(column = "Remarks")
	@CsvBindByPosition(position = 5)
	private String remarks;
	
	
	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	/*public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}*/

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

//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategoryId(String category) {
//		this.category = category;
//	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	@Override
	public String toString() {
		return "Grievance:{grievanceId:"+grievanceId+",grievanceStatus"+grievanceStatus+","
				+ "createdOn"+createdOn+",modifiedOn:"+modifiedOn+","
						+ "remarks"+remarks+"}";
	}
}
