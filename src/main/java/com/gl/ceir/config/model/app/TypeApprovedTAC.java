package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Audited
public class TypeApprovedTAC {			 
			 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String manufacturerId;
	private String manufacturerName;
	private String country;

	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requestDate;
	
	@NotNull
//	@Column(unique=true)
	private String tac;
	
	@ColumnDefault("-1")
	private Integer status;

	@Column(name="user_id")
	private Long userId;
	private String userType;
	private Long adminUserId;
	private String adminUserType;
	
	
	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date approveDisapproveDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedOn;
	
	@NotNull
	private String txnId;

	private String trademark;
	
	private Long productName;
	
	private int modelNumber;
	
	private String manufacturerCountry;
	
	private String frequencyRange;

	private Long featureId;
	
	public String getTrademark() {
		return trademark;
	}
	public void setTrademark(String trademark) {
		this.trademark = trademark;
	}
	public String getManufacturerCountry() {
		return manufacturerCountry;
	}
	public void setManufacturerCountry(String manufacturerCountry) {
		this.manufacturerCountry = manufacturerCountry;
	}
	public String getFrequencyRange() {
		return frequencyRange;
	}
	public void setFrequencyRange(String frequencyRange) {
		this.frequencyRange = frequencyRange;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	public Date getApproveDisapproveDate() {
		return approveDisapproveDate;
	}
	public void setApproveDisapproveDate(Date approveDisapproveDate) {
		this.approveDisapproveDate = approveDisapproveDate;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
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
	public Long getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(Long adminUserId) {
		this.adminUserId = adminUserId;
	}
	public String getAdminUserType() {
		return adminUserType;
	}
	public void setAdminUserType(String adminUserType) {
		this.adminUserType = adminUserType;
	}
	public Long getProductName() {
		return productName;
	}
	public void setProductName(Long productName) {
		this.productName = productName;
	}
	public int getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(int modelNumber) {
		this.modelNumber = modelNumber;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
	
	@Override
	public String toString() {
		return "TypeApprovedDb [id=" + id + ", manufacturerId=" + manufacturerId + ", manufacturerName="
				+ manufacturerName + ", country=" + country + ", requestDate=" + requestDate + ", tac=" + tac
				+ ", status=" + status + ", approveDisapproveDate= "+ approveDisapproveDate + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + ", txnId=" + txnId + ",trademark="+trademark + ",modelNumber="+modelNumber + 
				",productName="+productName + ",manufacturerCountry="+ manufacturerCountry +",frequencyRange="+ frequencyRange+ "]";
	}
}
