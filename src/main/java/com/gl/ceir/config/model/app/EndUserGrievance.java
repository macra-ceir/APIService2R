package com.gl.ceir.config.model.app;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public class EndUserGrievance {
	
	private String grievanceId;
	
	private String txnId;
	
	private int categoryId;
	
	private String remarks;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String phoneNo;
	
	private String email;
	
	private String userType;
	
	private long featureId;
	
	private String raisedBy;
	
	@Transient
	private Long raisedByUserId;
	
	@Transient
	private String raisedByUserType;
	
	@Transient
	private String publicIp;
	
	@Transient
	private String browser;
	
	private List<AttachedFileInfo> attachedFiles = new ArrayList<>();

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<AttachedFileInfo> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<AttachedFileInfo> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	
	public long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public Long getRaisedByUserId() {
		return raisedByUserId;
	}

	public void setRaisedByUserId(Long raisedByUserId) {
		this.raisedByUserId = raisedByUserId;
	}

	public String getRaisedByUserType() {
		return raisedByUserType;
	}

	public void setRaisedByUserType(String raisedByUserType) {
		this.raisedByUserType = raisedByUserType;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Override
	public String toString() {
		return "EndUserGrievance [grievanceId=" + grievanceId + ", txnId=" + txnId + ", categoryId=" + categoryId
				+ ", remarks=" + remarks + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", phoneNo=" + phoneNo + ", email=" + email + ", userType=" + userType + ", featureId="
				+ featureId + ", raisedBy=" + raisedBy + ", raisedByUserId=" + raisedByUserId + ", raisedByUserType="
				+ raisedByUserType + ", publicIp=" + publicIp + ", browser=" + browser + ", attachedFiles="
				+ attachedFiles + "]";
	}
	
}
