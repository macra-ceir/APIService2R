package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class TypeApproveCEIRFileModel {
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 2)
	private String txnId;
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String createdOn;
	
	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifiedOn;
	
	@CsvBindByName(column = "Display Name")
	@CsvBindByPosition(position = 3)
	private String displayName;
	
	@CsvBindByName(column = "Country")
	@CsvBindByPosition(position = 7)
	private String country;
	
	@CsvBindByName(column = "TAC")
	@CsvBindByPosition(position = 4)
	private String tac;

	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 9)
	private String status;

	@CsvBindByName(column = "Model Number")
	@CsvBindByPosition(position = 6)
	private String modelNumber;
	
	@CsvBindByName(column = "Brand Name")
	@CsvBindByPosition(position = 5)
	private String productName;
	
	@CsvBindByName(column = "User Type")
	@CsvBindByPosition(position = 8)
	private String userType;
	
	@CsvBindByName(column = "File")
	@CsvBindByPosition(position = 10)
	private String fileName;

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTac() {
		return tac;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "TypeApproveCEIRFileModel [txnId=" + txnId + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
				+ ", displayName=" + displayName + ", country=" + country + ", tac=" + tac + ", status=" + status
				+ ", modelNumber=" + modelNumber + ", productName=" + productName + ", userType=" + userType
				+ ", fileName=" + fileName + "]";
	}
	
}

