package com.gl.ceir.config.model.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gl.ceir.config.util.Utility;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "users")
public class User {  
	private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	@JsonIgnore
	private String password;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;
	
	@JsonIgnore
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime passwordDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedOn;

	private Integer currentStatus;  
	private Integer previousStatus; 
	private String remark;
   
	private Integer parentId=0;
	
    private String userLanguage;
    
	private String modifiedBy;
	


	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	UserProfile userProfile;



//	@JsonIgnore
//	@OneToOne(mappedBy = "userDetails", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
//	UserTemporarydetails userTemporarydetails;  


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_type_id", nullable = false)
	private Usertype usertype;

//	@JsonIgnore
//	@OneToMany(mappedBy = "userTrack",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	List<LoginTracking> loginTracking;

	@JsonIgnore
	@OneToMany(mappedBy = "userData", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Userrole> userRole; 

//	@JsonIgnore
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//	private List<UserSecurityquestion> userSecurityquestion;

	@JsonIgnore
	@OneToMany(mappedBy = "userPassword",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<UserPasswordHistory> userPasswordHistory;

	@Transient
    private String stateInterp;
	public long getId() {      
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn =modifiedOn;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}


	public Usertype getUsertype() { 
		return usertype; 
	} 
	public void setUsertype(Usertype usertype) { 
		this.usertype = usertype; 
	}

//	public List<UserSecurityquestion> getUserSecurityquestion() {
//		return userSecurityquestion;
//	}
//	public void setUserSecurityquestion(List<UserSecurityquestion> userSecurityquestion) {
//		this.userSecurityquestion = userSecurityquestion;
//	}
	public List<Userrole> getUserRole() {
		return userRole;
	}
	public void setUserRole(List<Userrole> userRole) {
		this.userRole = userRole;
	}

//	public List<LoginTracking> getLoginTracking() {
//		return loginTracking;
//	}
//	public void setLoginTracking(List<LoginTracking> loginTracking) {
//		this.loginTracking = loginTracking;
//	}
	public Integer getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Integer getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(Integer previousStatus) {
		this.previousStatus = previousStatus;
	}  


//	public UserTemporarydetails getUserTemporarydetails() {
//		return userTemporarydetails;
//	}
//	public void setUserTemporarydetails(UserTemporarydetails userTemporarydetails) {
//		this.userTemporarydetails = userTemporarydetails;
//	}


	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	//	public List<TypeApprovedDb> getTypeApprovedDb() { return typeApprovedDb; }
	//	public void setTypeApprovedDb(List<TypeApprovedDb> typeApprovedDb) {
	//		this.typeApprovedDb = typeApprovedDb; }
	//


	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	
	public String getUserLanguage() {
		return userLanguage;
	}
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	
	public List<UserPasswordHistory> getUserPasswordHistory() {
		return userPasswordHistory;
	}
	public void setUserPasswordHistory(List<UserPasswordHistory> userPasswordHistory) {
		this.userPasswordHistory = userPasswordHistory;
	}



	public User() {
		super();
	}
	public User(String username, String password,Integer currentStatus, Integer previousStatus, Usertype usertype) {
		super();
		this.username = username;
		this.password = password;
		this.currentStatus = currentStatus;
		this.previousStatus = previousStatus;
		this.usertype = usertype;
	}
	
	public static User getDefaultUser() {
		User user = new User();
		user.setUsername(Utility.getTxnId());
		user.setPassword("NA");
		return user;
	}
	
	public User(Integer currentStatus, Integer previousStatus) {
		super();
		this.currentStatus = currentStatus;
		this.previousStatus = previousStatus;
	}
	public LocalDateTime getPasswordDate() {
		return passwordDate;
	}
	public void setPasswordDate(LocalDateTime passwordDate) {
		this.passwordDate = passwordDate;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static void setSerialVersionUID(long serialVersionUID) {
		User.serialVersionUID = serialVersionUID;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", passwordDate=");
		builder.append(passwordDate);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", currentStatus=");
		builder.append(currentStatus);
		builder.append(", previousStatus=");
		builder.append(previousStatus);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", userLanguage=");
		builder.append(userLanguage);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append("]");
		return builder.toString();
	}
	
	
	

}
