package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Audited
public class TypeApprovedDb {

	 //private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			 
			 
	//private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String manufacturerId;
	
	private String manufacturerName;
	
	private String country;

	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requestDate;
	
	@NotNull
	private String tac;
	
	private Integer approveStatus;
	
	@ColumnDefault("-1")
	private Integer adminApproveStatus;

	@Column(name="user_id")
	private Long userId;
	
	private String userType;
	
	private Long adminUserId;
	
	private String adminUserType;
	
	
	@Type(type="date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date approveDisapproveDate;
	
	private String remark;
	private String adminRemark;
	
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

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@OneToOne
	@JoinColumn(name="user_id",insertable = false, updatable = false)
	@JsonIgnore
	private User userForTypeApprove;

	@Transient
	private String stateInterp;
	
	@Transient
	private String adminStateInterp;

	private String trademark;
	
	private Long productName;
	
	private int modelNumber;
	
	@Transient
	private String productNameInterp;
	
	@Transient
	private String modelNumberInterp;
	
	private String manufacturerCountry;
	
	private String frequencyRange;

	private Long featureId;
	
	@Transient
	private String userDisplayName;
	
	@Transient
	private String publicIp;
	
	@Transient
	private String browser;
	
	@OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
	private List<TypeApprovedAttachedFileInfo> attachedFiles = new ArrayList<>();
	
	private Integer deleteFlag;
	
	@Transient
	private String deleteFlagInterp;
	
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getDeleteFlagInterp() {
		return deleteFlagInterp;
	}
	public void setDeleteFlagInterp(String deleteFlagInterp) {
		this.deleteFlagInterp = deleteFlagInterp;
	}
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
	public List<TypeApprovedAttachedFileInfo> getAttachedFiles() {
		return attachedFiles;
	}
	public void setAttachedFiles(List<TypeApprovedAttachedFileInfo> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Integer getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public User getUserForTypeApprove() { 
		return userForTypeApprove; 
	} 
	public void setUserForTypeApprove(User userForTypeApprove) { 
		this.userForTypeApprove
		= userForTypeApprove; 
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getAdminApproveStatus() {
		return adminApproveStatus;
	}
	public void setAdminApproveStatus(Integer adminApproveStatus) {
		this.adminApproveStatus = adminApproveStatus;
	}
	public String getAdminStateInterp() {
		return adminStateInterp;
	}
	public void setAdminStateInterp(String adminStateInterp) {
		this.adminStateInterp = adminStateInterp;
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
	public String getAdminRemark() {
		return adminRemark;
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
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
	public String getProductNameInterp() {
		return productNameInterp;
	}
	public void setProductNameInterp(String productNameInterp) {
		this.productNameInterp = productNameInterp;
	}
	public String getModelNumberInterp() {
		return modelNumberInterp;
	}
	public void setModelNumberInterp(String modelNumberInterp) {
		this.modelNumberInterp = modelNumberInterp;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
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
		return "TypeApprovedDb [id=" + id + ", manufacturerId=" + manufacturerId + ", manufacturerName="
				+ manufacturerName + ", country=" + country + ", requestDate=" + requestDate + ", tac=" + tac
				+ ", approveStatus=" + approveStatus + ", adminApproveStatus=" + adminApproveStatus + ", userId="
				+ userId + ", userType=" + userType + ", adminUserId=" + adminUserId + ", adminUserType="
				+ adminUserType + ", approveDisapproveDate=" + approveDisapproveDate + ", remark=" + remark
				+ ", adminRemark=" + adminRemark + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
				+ ", txnId=" + txnId + ", userForTypeApprove=" + userForTypeApprove + ", stateInterp=" + stateInterp
				+ ", adminStateInterp=" + adminStateInterp + ", trademark=" + trademark + ", productName=" + productName
				+ ", modelNumber=" + modelNumber + ", productNameInterp=" + productNameInterp + ", modelNumberInterp="
				+ modelNumberInterp + ", manufacturerCountry=" + manufacturerCountry + ", frequencyRange="
				+ frequencyRange + ", featureId=" + featureId + ", userDisplayName=" + userDisplayName + ", publicIp="
				+ publicIp + ", browser=" + browser + ", attachedFiles=" + attachedFiles + ", deleteFlag=" + deleteFlag
				+ ", deleteFlagInterp=" + deleteFlagInterp + "]";
	}
	
}
