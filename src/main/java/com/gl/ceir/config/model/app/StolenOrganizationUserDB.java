package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
public class StolenOrganizationUserDB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// Mysql
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	// Oracle
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_SEQ")
    //@SequenceGenerator(sequenceName = "stolen_org_seq", allocationSize = 1, name = "ORG_SEQ")
	private Long id;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	private String username;
	private String companyName;  
	private String propertyLocation;
	private String street;
	private String locality;
	
	@Column(length = 50)
	private String fileName;
			
	@Column(length = 50)
	private String district;
	
	@Column(length = 50)
	private String commune;
	
	@Column(length = 50)
	private String village;
	
	private Integer postalCode;
	
	private String province;
	
	private String country;
	
	// Authorize person Info.
	private String personnelFirstName;  
	private String personnelMiddleName;
	private String personnelLastName;
	private String email;
	private String phoneNo;
	
	// Place of device stolen.
	private String incidentPropertyLocation;
	private String incidentStreet;
	private String incidentLocality;
	
	@NotNull
	@Column(length = 50)
	private String incidentDistrict;
	
	@NotNull
	@Column(length = 50)
	private String incidentCommune;
	
	@NotNull
	@Column(length = 50)
	private String incidentVillage;
	
	@NotNull
	private Integer incidentPostalCode;
	
	private String incidentProvince;
	private String incidentCountry;
	
	@Lob
	// @Basic(fetch = FetchType.LAZY)
	private String remark;
	
	@OneToOne
	@JoinColumn(name = "stolen_id")
	@JsonIgnore
	StolenandRecoveryMgmt stolenandRecoveryMgmt;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPropertyLocation() {
		return propertyLocation;
	}
	public void setPropertyLocation(String propertyLocation) {
		this.propertyLocation = propertyLocation;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public Integer getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPersonnelFirstName() {
		return personnelFirstName;
	}
	public void setPersonnelFirstName(String personnelFirstName) {
		this.personnelFirstName = personnelFirstName;
	}
	public String getPersonnelMiddleName() {
		return personnelMiddleName;
	}
	public void setPersonnelMiddleName(String personnelMiddleName) {
		this.personnelMiddleName = personnelMiddleName;
	}
	public String getPersonnelLastName() {
		return personnelLastName;
	}
	public void setPersonnelLastName(String personnelLastName) {
		this.personnelLastName = personnelLastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getIncidentStreet() {
		return incidentStreet;
	}
	public void setIncidentStreet(String incidentStreet) {
		this.incidentStreet = incidentStreet;
	}
	public String getIncidentLocality() {
		return incidentLocality;
	}
	public void setIncidentLocality(String incidentLocality) {
		this.incidentLocality = incidentLocality;
	}
	public String getIncidentDistrict() {
		return incidentDistrict;
	}
	public void setIncidentDistrict(String incidentDistrict) {
		this.incidentDistrict = incidentDistrict;
	}
	public String getIncidentCommune() {
		return incidentCommune;
	}
	public void setIncidentCommune(String incidentCommune) {
		this.incidentCommune = incidentCommune;
	}
	public String getIncidentVillage() {
		return incidentVillage;
	}
	public void setIncidentVillage(String incidentVillage) {
		this.incidentVillage = incidentVillage;
	}
	public Integer getIncidentPostalCode() {
		return incidentPostalCode;
	}
	public void setIncidentPostalCode(Integer incidentPostalCode) {
		this.incidentPostalCode = incidentPostalCode;
	}
	public String getIncidentProvince() {
		return incidentProvince;
	}
	public void setIncidentProvince(String incidentProvince) {
		this.incidentProvince = incidentProvince;
	}
	public String getIncidentCountry() {
		return incidentCountry;
	}
	public void setIncidentCountry(String incidentCountry) {
		this.incidentCountry = incidentCountry;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StolenandRecoveryMgmt getStolenandRecoveryMgmt() {
		return stolenandRecoveryMgmt;
	}
	public void setStolenandRecoveryMgmt(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		this.stolenandRecoveryMgmt = stolenandRecoveryMgmt;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIncidentPropertyLocation() {
		return incidentPropertyLocation;
	}
	public void setIncidentPropertyLocation(String incidentPropertyLocation) {
		this.incidentPropertyLocation = incidentPropertyLocation;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StolenOrganizationUserDB [createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", username=");
		builder.append(username);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", propertyLocation=");
		builder.append(propertyLocation);
		builder.append(", street=");
		builder.append(street);
		builder.append(", locality=");
		builder.append(locality);
		builder.append(", district=");
		builder.append(district);
		builder.append(", commune=");
		builder.append(commune);
		builder.append(", village=");
		builder.append(village);
		builder.append(", postalCode=");
		builder.append(postalCode);
		builder.append(", province=");
		builder.append(province);
		builder.append(", country=");
		builder.append(country);
		builder.append(", personnelFirstName=");
		builder.append(personnelFirstName);
		builder.append(", personnelMiddleName=");
		builder.append(personnelMiddleName);
		builder.append(", personnelLastName=");
		builder.append(personnelLastName);
		builder.append(", email=");
		builder.append(email);
		builder.append(", phoneNo=");
		builder.append(phoneNo);
		builder.append(", incidentStreet=");
		builder.append(incidentStreet);
		builder.append(", incidentLocality=");
		builder.append(incidentLocality);
		builder.append(", incidentDistrict=");
		builder.append(incidentDistrict);
		builder.append(", incidentCommune=");
		builder.append(incidentCommune);
		builder.append(", incidentVillage=");
		builder.append(incidentVillage);
		builder.append(", incidentPostalCode=");
		builder.append(incidentPostalCode);
		builder.append(", incidentProvince=");
		builder.append(incidentProvince);
		builder.append(", incidentCountry=");
		builder.append(incidentCountry);
		builder.append("]");
		return builder.toString();
	}
	
}