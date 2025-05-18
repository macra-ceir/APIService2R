package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class UserPasswordHistory {
	private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;    
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User userPassword;
	private String password;
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedOn;
	
	
	public UserPasswordHistory() {
		super();
	}
	
	public UserPasswordHistory(String password) {
		super();
		this.password = password;
	}

	public UserPasswordHistory(User userPassword, String password) {
		super();
		this.userPassword = userPassword;
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(User userPassword) {
		this.userPassword = userPassword;
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
		this.createdOn = createdOn;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	@Override
	public String toString() {
		return "UserPasswordHistory [id=" + id + ", password=" + password + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + "]";
	}
}
