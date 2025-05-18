package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserDepartment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	@Column(length = 50)
	private String name; 
	
	@Column(length = 50)
	private String departmentId;
	
	@Column(length = 50)
	private String departmentFilename;
	
	@OneToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	EndUserDB endUserDB;
	
	public UserDepartment() {
		// TODO Auto-generated constructor stub
	}
	
	public UserDepartment(String name, String departmentId, String departmentFilename) {
		this.name = name;
		this.departmentId = departmentId;
		this.departmentFilename = departmentFilename;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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
	public String getDepartmentFilename() {
		return departmentFilename;
	}
	public void setDepartmentFilename(String departmentFilename) {
		this.departmentFilename = departmentFilename;
	}
	public EndUserDB getEndUserDB() {
		return endUserDB;
	}
	public void setEndUserDB(EndUserDB endUserDB) {
		this.endUserDB = endUserDB;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDepartment [createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", name=");
		builder.append(name);
		builder.append(", departmentId=");
		builder.append(departmentId);
		builder.append("]");
		return builder.toString();
	}
}
