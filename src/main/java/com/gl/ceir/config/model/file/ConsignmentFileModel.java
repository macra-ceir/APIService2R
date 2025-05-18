package com.gl.ceir.config.model.file;

public class ConsignmentFileModel {
	private Long consignmentId;
	
	private String consignmentStatus;
	
	private String txnId;

	private String supplierName;
	
	private String TaxPaidStatus;
	
	private String fileName;
	
	private String createdOn;

	private String modifiedOn;

	public Long getConsignmentId() {
		return consignmentId;
	}

	public void setConsignmentId(Long consignmentId) {
		this.consignmentId = consignmentId;
	}

	public String getConsignmentStatus() {
		return consignmentStatus;
	}

	public void setConsignmentStatus(String consignmentStatus) {
		this.consignmentStatus = consignmentStatus;
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

	public String getTaxPaidStatus() {
		return TaxPaidStatus;
	}

	public void setTaxPaidStatus(String taxPaidStatus) {
		TaxPaidStatus = taxPaidStatus;
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
		.append("ConsignmentFileModel [consignmentId=")
		.append(consignmentId)
		.append(", consignmentStatus=")
		.append(consignmentStatus)
		.append(", txnId=")
		.append(txnId)
		.append(", supplierName=")
		.append(supplierName)
		.append(", TaxPaidStatus=")
		.append(TaxPaidStatus)
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
