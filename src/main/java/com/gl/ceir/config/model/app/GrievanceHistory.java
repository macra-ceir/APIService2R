package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "grievance_his")
public class GrievanceHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name="grievance_id")
	private String grievanceId;
	
	private Long userId ;

	private String userType;
	
	@Column(length = 3)
	private int grievanceStatus;
	
	//@NotNull
	@Column(length = 20)
	private String txnId;
	
	@Column(length = 3)
	private int categoryId;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;

	@Column(length = 1000)
	private String remarks;
	
	private Long closedByUserId;
	
	private String closedByUserType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
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

	public int getGrievanceStatus() {
		return grievanceStatus;
	}

	public void setGrievanceStatus(int grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getClosedByUserType() {
		return closedByUserType;
	}

	public void setClosedByUserType(String closedByUserType) {
		this.closedByUserType = closedByUserType;
	}

	public Long getClosedByUserId() {
		return closedByUserId;
	}

	public void setClosedByUserId(Long closedByUserId) {
		this.closedByUserId = closedByUserId;
	}

	@Override
	public String toString() {
		return "GrievanceHistory [id=" + id + ", grievanceId=" + grievanceId + ", userId=" + userId + ", userType="
				+ userType + ", grievanceStatus=" + grievanceStatus + ", txnId=" + txnId + ", categoryId=" + categoryId
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", remarks=" + remarks
				+ ", closedByUserId=" + closedByUserId + ", closedByUserType=" + closedByUserType + "]";
	}
	
	
}
