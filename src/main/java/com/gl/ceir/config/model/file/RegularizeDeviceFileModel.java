package com.gl.ceir.config.model.file;

public class RegularizeDeviceFileModel {
	
	private String deviceIdTypeInterp;
	private String deviceTypeInterp;
	private Double price;
	private String currency;
	private String country;
	private String TaxPaidStatus;
	private String createdOn;
	private String modifiedOn;
	
	public String getDeviceIdTypeInterp() {
		return deviceIdTypeInterp;
	}
	public void setDeviceIdTypeInterp(String deviceIdTypeInterp) {
		this.deviceIdTypeInterp = deviceIdTypeInterp;
	}
	public String getDeviceTypeInterp() {
		return deviceTypeInterp;
	}
	public void setDeviceTypeInterp(String deviceTypeInterp) {
		this.deviceTypeInterp = deviceTypeInterp;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTaxPaidStatus() {
		return TaxPaidStatus;
	}
	public void setTaxPaidStatus(String taxPaidStatus) {
		TaxPaidStatus = taxPaidStatus;
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
		StringBuilder builder = new StringBuilder();
		builder.append("RegularizeDeviceFileModel [deviceIdTypeInterp=");
		builder.append(deviceIdTypeInterp);
		builder.append(", deviceTypeInterp=");
		builder.append(deviceTypeInterp);
		builder.append(", price=");
		builder.append(price);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", TaxPaidStatus=");
		builder.append(TaxPaidStatus);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append("]");
		return builder.toString();
	}

}
