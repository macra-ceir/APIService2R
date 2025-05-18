package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
@Table(name = "sys_param_list_value")
public class SystemConfigListDb implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@JsonIgnore
	private Date createdOn;

	@UpdateTimestamp
	@JsonIgnore
	private Date modifiedOn;
	
	private String tag;
	
	private Integer value;
	
	private String interpretation;
	
	private Integer listOrder;
	
	@Column(length = 10)
	private String tagId;
	
	private String description;
	private String displayName;
	
	public SystemConfigListDb() {
	}
	
	public SystemConfigListDb(String tag, String description, String displayName) {
		this.tag = tag;
		this.description = description;
		this.displayName = displayName;
	}
	
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
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public Integer getListOrder() {
		return listOrder;
	}
	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	@Override
	public String toString() {
		return "SystemConfigListDb [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", tag="
				+ tag + ", value=" + value + ", interpretation=" + interpretation + ", listOrder=" + listOrder
				+ ", tagId=" + tagId + ", description=" + description + ", displayName=" + displayName + "]";
	}
	

	
}