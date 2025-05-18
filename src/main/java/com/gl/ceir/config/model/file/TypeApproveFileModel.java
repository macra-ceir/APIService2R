package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class TypeApproveFileModel {
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String createdOn;
	
	@CsvBindByName(column = "Trademark")
	@CsvBindByPosition(position = 1)
	private String trandemark;
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 2)
	private String txnId;

	@CsvBindByName(column = "TAC")
	@CsvBindByPosition(position = 3)
	private String tac;

	@CsvBindByName(column = "Brand Name")
	@CsvBindByPosition(position = 4)
	private String productName;

	@CsvBindByName(column = "Model Number")
	@CsvBindByPosition(position = 5)
	private String modelNumber;
	
	@CsvBindByName(column = "Country")
	@CsvBindByPosition(position = 6)
	private String country;	

	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 7)
	private String status;

	@CsvBindByName(column = "File")
	@CsvBindByPosition(position = 8)
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

	public String getTrandemark() {
		return trandemark;
	}

	public void setTrandemark(String trandemark) {
		this.trandemark = trandemark;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}

