package com.gl.ceir.config.model.file;

public class StolenAndRecoveryFileModel {
	
	private Long stolenId;
	
	private String stolenStatus;
	
	private String txnId;

	private String supplierName;
	
	private String fileName;
	
	private String createdOn;

	private String modifiedOn;

	public Long getStolenId() {
		return stolenId;
	}

	public void setStolenId(Long stolenId) {
		this.stolenId = stolenId;
	}

	public String getStolenStatus() {
		return stolenStatus;
	}

	public void setStolenStatus(String stolenStatus) {
		this.stolenStatus = stolenStatus;
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
		.append("ConsignmentFileModel [stolenId=")
		.append(stolenId)
		.append(", stolenStatus=")
		.append(stolenStatus)
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
