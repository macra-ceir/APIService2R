package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Audited
public class RegularizeDeviceDb implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@UpdateTimestamp
	private LocalDateTime modifiedOn;
	
	@NotNull
	private String nid;
	
	private Integer deviceStatus;
	@Transient
	private String deviceStatusInterp;
	
	private Integer taxPaidStatus;
	@Transient
	private String taxPaidStatusInterp;
	
	private Integer deviceType;
	@Transient
	private String deviceTypeInterp;
	
	private Integer deviceIdType;
	@Transient
	private String deviceIdTypeInterp;
	
	private String multiSimStatus;
	
	private String country;
	
	private String deviceSerialNumber;
	
	private String txnId;
	
	// @NotNull
	private Double price;
	
	private Integer currency;
	@Transient
	private String currencyInterp;
	
	@NotNull
	@Column(unique = true, length = 18)
	private String firstImei;
	
	@Column(unique = true, length = 18)
	private String secondImei;
	
	@Column(unique = true, length = 18)
	private String thirdImei;
	
	@Column(unique = true, length = 18)
	private String fourthImei;
	
	private String remark;

	//@NotNull
	private Integer status;
	@Transient
	private String stateInterp;
	
	@ManyToOne 
	@JoinColumn(name = "userId") 
	private EndUserDB endUserDB;
	
	@NotNull
	@Column(length = 20)
	private String origin;
	
	private long creatorUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getFirstImei() {
		return firstImei;
	}

	public void setFirstImei(String firstImei) {
		this.firstImei = firstImei;
	}

	public String getSecondImei() {
		return secondImei;
	}

	public void setSecondImei(String secondImei) {
		this.secondImei = secondImei;
	}

	public String getThirdImei() {
		return thirdImei;
	}

	public void setThirdImei(String thirdImei) {
		this.thirdImei = thirdImei;
	}

	public String getFourthImei() {
		return fourthImei;
	}

	public void setFourthImei(String fourthImei) {
		this.fourthImei = fourthImei;
	}

	public Integer getTaxPaidStatus() {
		return taxPaidStatus;
	}

	public void setTaxPaidStatus(Integer taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
	}

	public String getMultiSimStatus() {
		return multiSimStatus;
	}

	public void setMultiSimStatus(String multiSimStatus) {
		this.multiSimStatus = multiSimStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}

	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public EndUserDB getEndUserDB() {
		return endUserDB;
	}

	public void setEndUserDB(EndUserDB endUserDB) {
		this.endUserDB = endUserDB;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public Integer getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getTaxPaidStatusInterp() {
		return taxPaidStatusInterp;
	}

	public void setTaxPaidStatusInterp(String taxPaidStatusInterp) {
		this.taxPaidStatusInterp = taxPaidStatusInterp;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceTypeInterp() {
		return deviceTypeInterp;
	}

	public void setDeviceTypeInterp(String deviceTypeInterp) {
		this.deviceTypeInterp = deviceTypeInterp;
	}

	public Integer getDeviceIdType() {
		return deviceIdType;
	}

	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}

	public String getDeviceIdTypeInterp() {
		return deviceIdTypeInterp;
	}

	public void setDeviceIdTypeInterp(String deviceIdTypeInterp) {
		this.deviceIdTypeInterp = deviceIdTypeInterp;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getCurrencyInterp() {
		return currencyInterp;
	}

	public void setCurrencyInterp(String currencyInterp) {
		this.currencyInterp = currencyInterp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeviceStatusInterp() {
		return deviceStatusInterp;
	}

	public void setDeviceStatusInterp(String deviceStatusInterp) {
		this.deviceStatusInterp = deviceStatusInterp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public long getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegularizeDeviceDb [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", deviceStatus=");
		builder.append(deviceStatus);
		builder.append(", deviceStatusInterp=");
		builder.append(deviceStatusInterp);
		builder.append(", taxPaidStatus=");
		builder.append(taxPaidStatus);
		builder.append(", taxPaidStatusInterp=");
		builder.append(taxPaidStatusInterp);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", deviceTypeInterp=");
		builder.append(deviceTypeInterp);
		builder.append(", deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceIdTypeInterp=");
		builder.append(deviceIdTypeInterp);
		builder.append(", multiSimStatus=");
		builder.append(multiSimStatus);
		builder.append(", country=");
		builder.append(country);
		builder.append(", deviceSerialNumber=");
		builder.append(deviceSerialNumber);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", price=");
		builder.append(price);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", currencyInterp=");
		builder.append(currencyInterp);
		builder.append(", firstImei=");
		builder.append(firstImei);
		builder.append(", secondImei=");
		builder.append(secondImei);
		builder.append(", thirdImei=");
		builder.append(thirdImei);
		builder.append(", fourthImei=");
		builder.append(fourthImei);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", status=");
		builder.append(status);
		builder.append(", endUserDB=");
		builder.append(endUserDB);
		builder.append("]");
		return builder.toString();
	}

}
