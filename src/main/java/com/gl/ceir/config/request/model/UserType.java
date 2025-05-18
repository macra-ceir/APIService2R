package com.gl.ceir.config.request.model;

import java.util.HashMap;
import java.util.Map;

public class UserType {
	private Integer id;
	private String usertypeName;
	private String createdOn;
	private String modifiedOn;
	private Integer status;
	private String statusInterp;
	private Integer selfRegister;
	private String defaultLink;
	private HibernateLazyInitializer hibernateLazyInitializer;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserType [id=");
		builder.append(id);
		builder.append(", usertypeName=");
		builder.append(usertypeName);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", status=");
		builder.append(status);
		builder.append(", statusInterp=");
		builder.append(statusInterp);
		builder.append(", selfRegister=");
		builder.append(selfRegister);
		builder.append(", defaultLink=");
		builder.append(defaultLink);
		builder.append(", hibernateLazyInitializer=");
		builder.append(hibernateLazyInitializer);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsertypeName() {
		return usertypeName;
	}
	public void setUsertypeName(String usertypeName) {
		this.usertypeName = usertypeName;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusInterp() {
		return statusInterp;
	}
	public void setStatusInterp(String statusInterp) {
		this.statusInterp = statusInterp;
	}
	public Integer getSelfRegister() {
		return selfRegister;
	}
	public void setSelfRegister(Integer selfRegister) {
		this.selfRegister = selfRegister;
	}
	public String getDefaultLink() {
		return defaultLink;
	}
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}
	public HibernateLazyInitializer getHibernateLazyInitializer() {
		return hibernateLazyInitializer;
	}
	public void setHibernateLazyInitializer(HibernateLazyInitializer hibernateLazyInitializer) {
		this.hibernateLazyInitializer = hibernateLazyInitializer;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
}
