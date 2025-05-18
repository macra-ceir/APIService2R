package com.gl.ceir.config.model.file;

public class StockFileModel {
	
	private Long stockId;
	
	private String stockStatus;
	
	private String txnId;

	private String supplierName;
	
	private String fileName;
	
	private String createdOn;

	private String modifiedOn;

	
	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
		.append("ConsignmentFileModel [stockId=")
		.append(stockId)
		.append(", stockStatus=")
		.append(stockStatus)
		.append(", txnId=")
		.append(txnId)
		.append(", supplierName=")
		.append(supplierName)
		.append(", fileName=")
		.append(fileName)
		.append(", createdOn=")
		.append(createdOn)
		.append(", modifiedOn=")
		.append(modifiedOn)
		.append("]");
		
		return builder.toString();
	}

}
