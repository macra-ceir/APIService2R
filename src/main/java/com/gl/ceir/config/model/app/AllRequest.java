package com.gl.ceir.config.model.app;

import javax.persistence.Transient;

public class AllRequest{

	@Transient
	private String username;
	
	@Transient
	private long userTypeId;
	@Transient
	private long userId;
	@Transient
	private long featureId;
	
	@Transient
	private String  imei;
	
	@Transient
	private String nid;
	
	
	@Transient
	private String userType;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(long userTypeId) {
		this.userTypeId = userTypeId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AllRequest [username=");
		builder.append(username);
		builder.append(", userTypeId=");
		builder.append(userTypeId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", userType=");
		builder.append(userType);
		builder.append("]");
		return builder.toString();
	}
}
