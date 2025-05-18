package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "grievance_msg")
public class GrievanceMsg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	@Column(name="grievance_id")
	private String grievanceId;
	
	@Column(name="user_id")
	private Long userId ;

	private String userType;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;
	
	@Column(length = 2000)
	private String reply;
	
	@Transient
	private Long raisedByUserId;
	
	@Transient
	private String raisedByUserType;
	
	
	@OneToOne
	@JoinColumn(name="grievance_id",insertable = false, updatable = false)
	private Grievance grievance;

	@OneToMany(
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	/*
	 * @CollectionTable( joinColumns = {@JoinColumn(name = "message_id")},
	 * foreignKey = @ForeignKey( foreignKeyDefinition =
	 * "FOREIGN KEY (message_id) REFERENCES grievance_msg(id)" ) )
	 */
	@JoinColumn(name="message_id", insertable = false, updatable = false)
	private List<AttachedFileInfo> attachedFiles = new ArrayList<>();
	
	@OneToOne
	@JoinColumn(name="user_id",insertable = false, updatable = false)
	@JsonIgnore
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Grievance getGrievance() {
		return grievance;
	}

	public void setGrievance(Grievance grievance) {
		this.grievance = grievance;
	}

	public List<AttachedFileInfo> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<AttachedFileInfo> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	public Long getRaisedByUserId() {
		return raisedByUserId;
	}

	public void setRaisedByUserId(Long raisedByUserId) {
		this.raisedByUserId = raisedByUserId;
	}

	public String getRaisedByUserType() {
		return raisedByUserType;
	}

	public void setRaisedByUserType(String raisedByUserType) {
		this.raisedByUserType = raisedByUserType;
	}

	@Override
	public String toString() {
		return "GrievanceMsg [id=" + id + ", grievanceId=" + grievanceId + ", userId=" + userId + ", userType="
				+ userType + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", reply=" + reply
				+ ", raisedByUserId=" + raisedByUserId + ", raisedByUserType=" + raisedByUserType + ", grievance="
				+ grievance + ", attachedFiles=" + attachedFiles + "]";
	}

}
