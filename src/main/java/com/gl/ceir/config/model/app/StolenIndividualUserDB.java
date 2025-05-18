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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Audited
@Entity
public class StolenIndividualUserDB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// Mysql
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	// Oracle
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IND_SEQ")
     //@SequenceGenerator(sequenceName = "stolen_ind_seq", allocationSize = 1, name = "IND_SEQ")
	private Long id;

	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@UpdateTimestamp
	private LocalDateTime modifiedOn;
	private String nid;
	
	private String firstName;  
	private String middleName;
	private String lastName;

	@Column(length = 50)
	private String nidFileName;
	
	// User address fields.
	private String propertyLocation;
	private String street;
	private String locality;
	
	@Column(length = 50)
	private String district;

	@Column(length = 50)
	private String commune;

	@Column(length = 50)
	private String village;

	private Integer postalCode;

	private String province;
	private String country;
	private String email;
	@Column(length = 15)
	private String phoneNo;
	private Integer docType;
	@Transient
	private Integer docTypeInterp;
	
	@Column(length = 15)
	private String alternateContactNumber;
	@Column(length = 50)
	private String deviceBrandName;

	private Long imeiEsnMeid1;
	private Long imeiEsnMeid2;
	private Long imeiEsnMeid3;
	private Long imeiEsnMeid4;
	private Integer deviceIdType;
	private Integer deviceType;
	@Column(length = 50)
	private String modelNumber;
	
	@Column(length = 15)
	private String contactNumber;
	private Integer operator;
	private Integer multiSimStatus;
	private Integer multiSimStatusInterp;

	// Place of device Stolen
	private String deviceStolenPropertyLocation;
	private String deviceStolenStreet;
	private String deviceStolenLocality;
	@NotNull
	@Column(length = 50)
	private String deviceStolenDistrict;
	@NotNull
	@Column(length = 50)
	private String deviceStolenCommune;
	@NotNull
	@Column(length = 50)
	private String deviceStolenVillage;
	@NotNull
	private Integer deviceStolenPostalCode;
	
	private String deviceStolenProvince;
	private String deviceStolenCountry;
	private String deviceSerialNumber;

	@Lob
	//@Basic(fetch = FetchType.LAZY)
	private String remark;
	
	@OneToOne
	@JoinColumn(name = "stolen_id")
	@JsonIgnore
	StolenandRecoveryMgmt stolenandRecoveryMgmt;

	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
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
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	public Integer getDocTypeInterp() {
		return docTypeInterp;
	}
	public void setDocTypeInterp(Integer docTypeInterp) {
		this.docTypeInterp = docTypeInterp;
	}
	public String getAlternateContactNumber() {
		return alternateContactNumber;
	}
	public void setAlternateContactNumber(String alternateContactNumber) {
		this.alternateContactNumber = alternateContactNumber;
	}
	public String getDeviceBrandName() {
		return deviceBrandName;
	}
	public void setDeviceBrandName(String deviceBrandName) {
		this.deviceBrandName = deviceBrandName;
	}
	public Integer getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public String getDeviceStolenPropertyLocation() {
		return deviceStolenPropertyLocation;
	}
	public void setDeviceStolenPropertyLocation(String deviceStolenPropertyLocation) {
		this.deviceStolenPropertyLocation = deviceStolenPropertyLocation;
	}
	public String getDeviceStolenStreet() {
		return deviceStolenStreet;
	}
	public void setDeviceStolenStreet(String deviceStolenStreet) {
		this.deviceStolenStreet = deviceStolenStreet;
	}
	public String getDeviceStolenLocality() {
		return deviceStolenLocality;
	}
	public void setDeviceStolenLocality(String deviceStolenLocality) {
		this.deviceStolenLocality = deviceStolenLocality;
	}
	public String getDeviceStolenDistrict() {
		return deviceStolenDistrict;
	}
	public void setDeviceStolenDistrict(String deviceStolenDistrict) {
		this.deviceStolenDistrict = deviceStolenDistrict;
	}
	public String getDeviceStolenCommune() {
		return deviceStolenCommune;
	}
	public void setDeviceStolenCommune(String deviceStolenCommune) {
		this.deviceStolenCommune = deviceStolenCommune;
	}
	public String getDeviceStolenVillage() {
		return deviceStolenVillage;
	}
	public void setDeviceStolenVillage(String deviceStolenVillage) {
		this.deviceStolenVillage = deviceStolenVillage;
	}
	public Integer getDeviceStolenPostalCode() {
		return deviceStolenPostalCode;
	}
	public void setDeviceStolenPostalCode(Integer deviceStolenPostalCode) {
		this.deviceStolenPostalCode = deviceStolenPostalCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNidFileName() {
		return nidFileName;
	}
	public void setNidFileName(String nidFileName) {
		this.nidFileName = nidFileName;
	}
	public Integer getMultiSimStatus() {
		return multiSimStatus;
	}
	public void setMultiSimStatus(Integer multiSimStatus) {
		this.multiSimStatus = multiSimStatus;
	}
	public Integer getMultiSimStatusInterp() {
		return multiSimStatusInterp;
	}
	public void setMultiSimStatusInterp(Integer multiSimStatusInterp) {
		this.multiSimStatusInterp = multiSimStatusInterp;
	}
	public String getDeviceStolenProvince() {
		return deviceStolenProvince;
	}
	public void setDeviceStolenProvince(String deviceStolenProvince) {
		this.deviceStolenProvince = deviceStolenProvince;
	}
	public String getDeviceStolenCountry() {
		return deviceStolenCountry;
	}
	public void setDeviceStolenCountry(String deviceStolenCountry) {
		this.deviceStolenCountry = deviceStolenCountry;
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
	public Long getImeiEsnMeid1() {
		return imeiEsnMeid1;
	}
	public void setImeiEsnMeid1(Long imeiEsnMeid1) {
		this.imeiEsnMeid1 = imeiEsnMeid1;
	}
	public Long getImeiEsnMeid2() {
		return imeiEsnMeid2;
	}
	public void setImeiEsnMeid2(Long imeiEsnMeid2) {
		this.imeiEsnMeid2 = imeiEsnMeid2;
	}
	public Long getImeiEsnMeid3() {
		return imeiEsnMeid3;
	}
	public void setImeiEsnMeid3(Long imeiEsnMeid3) {
		this.imeiEsnMeid3 = imeiEsnMeid3;
	}
	public Long getImeiEsnMeid4() {
		return imeiEsnMeid4;
	}
	public void setImeiEsnMeid4(Long imeiEsnMeid4) {
		this.imeiEsnMeid4 = imeiEsnMeid4;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StolenIndividualUserDB [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", middleName=");
		builder.append(middleName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", nidFileName=");
		builder.append(nidFileName);
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
		builder.append(", email=");
		builder.append(email);
		builder.append(", phoneNo=");
		builder.append(phoneNo);
		builder.append(", docType=");
		builder.append(docType);
		builder.append(", docTypeInterp=");
		builder.append(docTypeInterp);
		builder.append(", alternateContactNumber=");
		builder.append(alternateContactNumber);
		builder.append(", deviceBrandName=");
		builder.append(deviceBrandName);
		builder.append(", imeiEsnMeid1=");
		builder.append(imeiEsnMeid1);
		builder.append(", imeiEsnMeid2=");
		builder.append(imeiEsnMeid2);
		builder.append(", imeiEsnMeid3=");
		builder.append(imeiEsnMeid3);
		builder.append(", imeiEsnMeid4=");
		builder.append(imeiEsnMeid4);
		builder.append(", deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", modelNumber=");
		builder.append(modelNumber);
		builder.append(", contactNumber=");
		builder.append(contactNumber);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", multiSimStatus=");
		builder.append(multiSimStatus);
		builder.append(", multiSimStatusInterp=");
		builder.append(multiSimStatusInterp);
		builder.append(", deviceStolenPropertyLocation=");
		builder.append(deviceStolenPropertyLocation);
		builder.append(", deviceStolenStreet=");
		builder.append(deviceStolenStreet);
		builder.append(", deviceStolenLocality=");
		builder.append(deviceStolenLocality);
		builder.append(", deviceStolenDistrict=");
		builder.append(deviceStolenDistrict);
		builder.append(", deviceStolenCommune=");
		builder.append(deviceStolenCommune);
		builder.append(", deviceStolenVillage=");
		builder.append(deviceStolenVillage);
		builder.append(", deviceStolenPostalCode=");
		builder.append(deviceStolenPostalCode);
		builder.append(", deviceStolenProvince=");
		builder.append(deviceStolenProvince);
		builder.append(", deviceStolenCountry=");
		builder.append(deviceStolenCountry);
		builder.append(", deviceSerialNumber=");
		builder.append(deviceSerialNumber);
		builder.append(", remark=");
		builder.append(remark);
		
		builder.append("]");
		return builder.toString();
	}

}
