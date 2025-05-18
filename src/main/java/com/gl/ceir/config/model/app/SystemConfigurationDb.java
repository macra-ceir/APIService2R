package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "sys_param")
public class SystemConfigurationDb implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	private Date createdOn;

	@UpdateTimestamp
	private Date modifiedOn;
	
	private String tag;
	
	private String value;
	
	private String description;
	
	private Integer type;
	
	@Transient
	private String typeInterp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getTypeInterp() {
		return typeInterp;
	}
	public void setTypeInterp(String typeInterp) {
		this.typeInterp = typeInterp;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public SystemConfigurationDb(String value) {
		super();
		this.value = value;
	}
	public SystemConfigurationDb(Long id, Date createdOn, Date modifiedOn, String tag, String value, String description,
			Integer type, String typeInterp) {
		super();
		this.id = id;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.tag = tag;
		this.value = value;
		this.description = description;
		this.type = type;
		this.typeInterp = typeInterp;
	}
	public SystemConfigurationDb() {
		super();
	}


}
