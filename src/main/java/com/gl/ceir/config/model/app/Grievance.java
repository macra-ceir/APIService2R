package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gl.ceir.config.model.constants.GrievanceStatus;

@Entity
@NamedQuery(name = "Grievance.getAllGrievanceStatusNotClosed",
query = "select g from Grievance g where g.grievanceId = ?1 and grievanceStatus != ?1")
@NamedQuery(name = "Grievance.getAllGrievanceStatusNotClosedForAdmin",
query = "select g from Grievance g where grievanceStatus != ?1")
@NamedQuery(name = "Grievance.getGrievanceByUserId",
query = "select g from Grievance g where g.grievanceId = ?1")
@Table(name = "grievance")
public class Grievance {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="grievance_id")
	private String grievanceId;
	
	@Column(name="user_id")
	private Long userId;

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
	
	@Transient
	private String stateInterp;
	
	@Transient
	private long featureId;
	
	@OneToOne
	@JoinColumn(name="user_id",insertable = false, updatable = false)
	@JsonIgnore
	private User user;
	
	@Transient
	private String userDisplayName;
	
	private String raisedBy;
	
	@Transient
	private Long raisedByUserId;
	
	@Transient
	private String raisedByUserType;
	
	@Transient
	private String publicIp;
	
	@Transient
	private String browser;
	
	@OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
	/*
	 * @CollectionTable( joinColumns = {@JoinColumn(name = "grievance_id")},
	 * foreignKey = @ForeignKey( foreignKeyDefinition =
	 * "FOREIGN KEY (grievance_id) REFERENCES grievance(grievance_id)" ) )
	 */
	@JoinColumn(name="grievance_id", insertable = false, updatable = false)
	private List<AttachedFileInfo> attachedFiles = new ArrayList<>();
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
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

	/*public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}*/

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	
	

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	@PostLoad
    public void postLoad() {
        if(stateInterp == null || stateInterp.isEmpty()) {
        	this.stateInterp = GrievanceStatus.getActionNames( this.grievanceStatus ).toString();
        }
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
		return "Grievance [grievanceId=" + grievanceId + ", userId=" + userId + ", userType=" + userType
				+ ", grievanceStatus=" + grievanceStatus + ", txnId=" + txnId + ", categoryId=" + categoryId
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", remarks=" + remarks + ", stateInterp="
				+ stateInterp + ", featureId=" + featureId + ", user=" + user + ", userDisplayName=" + userDisplayName
				+ ", raisedBy=" + raisedBy + ", raisedByUserId=" + raisedByUserId + ", raisedByUserType="
				+ raisedByUserType + ", publicIp=" + publicIp + ", browser=" + browser + ", attachedFiles="
				+ attachedFiles + "]";
	}
	
}
