package com.gl.ceir.config.model.app;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
public class VisaDb implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@Column(nullable = false, updatable = false)
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
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	EndUserDB endUserDB;
	
	public VisaDb() {

	}
	
	public VisaDb(Integer visaType, String visaNumber, String visaFileName, String entryDateInCountry, String visaExpiryDate) {

	}
	
	public VisaDb(@NotNull Integer visaType, String visaNumber, @NotNull String visaFileName, String entryDateInCountry,
			Date visaExpiryDate, EndUserDB endUserDB) {
		super();
		this.visaType = visaType;
		this.visaNumber = visaNumber;
		this.visaFileName = visaFileName;
		this.entryDateInCountry = entryDateInCountry;
		this.visaExpiryDate = visaExpiryDate;
		this.endUserDB = endUserDB;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EndUserDB getEndUserDB() {
		return endUserDB;
	}

	public void setEndUserDB(EndUserDB endUserDB) {
		this.endUserDB = endUserDB;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getVisaFileName() {
		return visaFileName;
	}

	public void setVisaFileName(String visaFileName) {
		this.visaFileName = visaFileName;
	}

	
	public String getVisaTypeInterp() {
		return visaTypeInterp;
	}

	public void setVisaTypeInterp(String visaTypeInterp) {
		this.visaTypeInterp = visaTypeInterp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VisaDb [id=");
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
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
