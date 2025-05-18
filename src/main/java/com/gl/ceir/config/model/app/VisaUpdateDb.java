package com.gl.ceir.config.model.app;
import java.time.LocalDateTime;
import java.util.Date;

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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Audited
public class VisaUpdateDb {
 
	private static long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;
	
	@NotNull
	private Integer visaType; 
	
	@Column(length = 50)
	private String visaNumber;
	
	@Column(length = 50)
	@NotNull
	private String visaFileName;
	
	private String entryDateInCountry;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date visaExpiryDate;
	
	@Transient
	private String visaTypeInterp;
	
	private Integer status;
	
	@Transient
	private String stateInterp;
	
    @Transient  
	private long userId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	EndUserDB endUserDBData;
	
	private String txnId;
	
	private String remark;
	 
	private String nid;
	
    private String approvedBy;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public VisaUpdateDb() {
		super();
	}

	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public VisaUpdateDb(@NotNull Integer visaType, String visaNumber, @NotNull String visaFileName,
			String entryDateInCountry, Date visaExpiryDate, Integer status,EndUserDB userId,String txnId,String nid) {
		super();
		this.visaType = visaType;
		this.visaNumber = visaNumber;
		this.visaFileName = visaFileName;
		this.entryDateInCountry = entryDateInCountry;
		this.visaExpiryDate = visaExpiryDate;
		this.status = status;
		this.endUserDBData=userId;
		this.txnId=txnId;
		this.nid=nid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

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

	public Integer getVisaType() {
		return visaType;
	}

	public void setVisaType(Integer visaType) {
		this.visaType = visaType;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public String getVisaFileName() {
		return visaFileName;
	}

	public void setVisaFileName(String visaFileName) {
		this.visaFileName = visaFileName;
	}

	public String getEntryDateInCountry() {
		return entryDateInCountry;
	}

	public void setEntryDateInCountry(String entryDateInCountry) {
		this.entryDateInCountry = entryDateInCountry;
	}

	

	public Date getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(Date visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public String getVisaTypeInterp() {
		return visaTypeInterp;
	}

	public void setVisaTypeInterp(String visaTypeInterp) {
		this.visaTypeInterp = visaTypeInterp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}




	public EndUserDB getEndUserDBData() {
		return endUserDBData;
	}

	public void setEndUserDBData(EndUserDB endUserDBData) {
		this.endUserDBData = endUserDBData;
	}

	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}


	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VisaUpdateDb [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", visaType=");
		builder.append(visaType);
		builder.append(", visaNumber=");
		builder.append(visaNumber);
		builder.append(", visaFileName=");
		builder.append(visaFileName);
		builder.append(", entryDateInCountry=");
		builder.append(entryDateInCountry);
		builder.append(", visaExpiryDate=");
		builder.append(visaExpiryDate);
		builder.append(", visaTypeInterp=");
		builder.append(visaTypeInterp);
		builder.append(", status=");
		builder.append(status);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", approvedBy=");
		builder.append(approvedBy);
		builder.append("]");
		return builder.toString();
	}
	
}
