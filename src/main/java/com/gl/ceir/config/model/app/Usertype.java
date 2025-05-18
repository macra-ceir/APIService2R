package com.gl.ceir.config.model.app;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity 
@Table(name = "user_type")
public class Usertype {   
	private static long serialVersionUID = 1L;
	@Id 
	private long id;
	private String userTypeName; 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdOn;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modifiedOn;

	@JsonIgnore 
	@OneToMany(mappedBy = "usertype", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<User> user;
 
	@JsonIgnore 
	@OneToMany(mappedBy = "usertypeData", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Userrole> userRole;    

//	@JsonIgnore
//	@OneToMany(mappedBy ="userTypeFeature",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
//	private List<UserToStakehoderfeatureMapping> userTofeatureMapping;
	
	private Integer status=1;
	

	public long getId() {
		return id; 
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
	
	public void setId(long id) {
		this.id = id;
	} 

	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) { 
		this.user = user;
	}
	public List<Userrole> getUserRole() {
		return userRole;
	}
	public void setUserRole(List<Userrole> userRole) {
		this.userRole = userRole;
	}
//	public List<UserToStakehoderfeatureMapping> getUserTofeatureMapping() {
//		return userTofeatureMapping;
//	}
//	public void setUserTofeatureMapping(List<UserToStakehoderfeatureMapping> userTofeatureMapping) {
//		this.userTofeatureMapping = userTofeatureMapping;
//	}
	public Usertype(long id) {
		super();
		this.id = id;
	}
	public Usertype() {
		super();
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static void setSerialVersionUID(long serialVersionUID) {
		Usertype.serialVersionUID = serialVersionUID;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	@Override
	public String toString() {
		return "Usertype [id=" + id + ", userTypeName=" + userTypeName + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + ", user=" + user + ", userRole=" + userRole + ", status=" + status + "]";
	}
	
}
