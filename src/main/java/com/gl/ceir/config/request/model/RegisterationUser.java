package com.gl.ceir.config.request.model;

import java.util.HashMap;
import java.util.Map;

public class RegisterationUser {

	private Integer id;
	private String username;
	private String createdOn;
	private String modifiedOn;
	private Integer currentStatus;
	private Integer previousStatus;
	private String remark;
	private Integer parentId;
	private String userLanguage;
	private String modifiedBy;
	private String referenceId;
	private String approvedBy;
	private UserType usertype;
	private String stateInterp;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public Integer getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Integer getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(Integer previousStatus) {
		this.previousStatus = previousStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getUserLanguage() {
		return userLanguage;
	}
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public UserType getUsertype() {
		return usertype;
	}
	public void setUsertype(UserType usertype) {
		this.usertype = usertype;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterationUser [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", currentStatus=");
		builder.append(currentStatus);
		builder.append(", previousStatus=");
		builder.append(previousStatus);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", userLanguage=");
		builder.append(userLanguage);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", referenceId=");
		builder.append(referenceId);
		builder.append(", approvedBy=");
		builder.append(approvedBy);
		builder.append(", usertype=");
		builder.append(usertype);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

	
}
