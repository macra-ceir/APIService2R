package com.gl.ceir.config.model.app;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ReportStatusDb {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nonnull
	@Column(name="reportname_id")
	private Long reportnameId;
	
	private Integer scheduleFlag;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime rundate;
	
	private Integer tryCount;

	private Integer typeFlag;
	
	@ManyToOne
	@JoinColumn(name="reportname_id",insertable = false, updatable = false)
	@JsonIgnore
	private ReportDb report;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportnameId() {
		return reportnameId;
	}

	public void setReportnameId(Long reportnameId) {
		this.reportnameId = reportnameId;
	}

	public Integer getScheduleFlag() {
		return scheduleFlag;
	}

	public void setScheduleFlag(Integer scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
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

	public LocalDateTime getRundate() {
		return rundate;
	}

	public void setRundate(LocalDateTime rundate) {
		this.rundate = rundate;
	}
	
	public ReportDb getReport() {
		return report;
	}

	public void setReport(ReportDb report) {
		this.report = report;
	}

	public Integer getTryCount() {
		return tryCount;
	}

	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}
	
	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Override
	public String toString() {
		return "ReportStatusDb [id=" + id + ", reportnameId=" + reportnameId + ", scheduleFlag=" + scheduleFlag
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", rundate=" + rundate + ", tryCount="
				+ tryCount + ", typeFlag=" + typeFlag + ", report=" + report + "]";
	}
	
}
