package com.gl.ceir.config.model.app;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public class GrievanceReply {
	private String grievanceId;
	private Long userId ;
	private String userType;
	private Integer grievanceStatus;
	private String txnId;
	private int categoryId;
	private String fileName;
	private String reply;
	private long featureId;
	private List<AttachedFileInfo> attachedFiles = new ArrayList<>();
	private Long raisedByUserId;
	private String raisedByUserType;
	private String publicIp;
	private String browser;
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
	public Integer getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(Integer grievanceStatus) {
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}
	public List<AttachedFileInfo> getAttachedFiles() {
		return attachedFiles;
	}
	public void setAttachedFiles(List<AttachedFileInfo> attachedFiles) {
		this.attachedFiles = attachedFiles;
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
		return "GrievanceReply [grievanceId=" + grievanceId + ", userId=" + userId + ", userType=" + userType
				+ ", grievanceStatus=" + grievanceStatus + ", txnId=" + txnId + ", categoryId=" + categoryId
				+ ", fileName=" + fileName + ", reply=" + reply + ", featureId=" + featureId + ", attachedFiles="
				+ attachedFiles + ", raisedByUserId=" + raisedByUserId + ", raisedByUserType=" + raisedByUserType
				+ ", publicIp=" + publicIp + ", browser=" + browser + "]";
	}
}
