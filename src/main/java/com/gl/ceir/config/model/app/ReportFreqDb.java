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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "report_freq_schedule")
public class ReportFreqDb {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nonnull
	@Column(name="report_name_id")
	private Long reportnameId;
	
	@Nonnull
	private Integer typeFlag;
	
	@Transient
	private String typeFlagInterp;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;
	
	@Nonnull
	private Integer status;

	@ManyToOne
	@JoinColumn(name="report_name_id",insertable = false, updatable = false)
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

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public ReportDb getReport() {
		return report;
	}

	public void setReport(ReportDb report) {
		this.report = report;
	}

	public String getTypeFlagInterp() {
		return typeFlagInterp;
	}

	public void setTypeFlagInterp(String typeFlagInterp) {
		this.typeFlagInterp = typeFlagInterp;
	}

	@Override
	public String toString() {
		return "ReportFreqDb [id=" + id + ", reportnameId=" + reportnameId + ", typeFlag=" + typeFlag
				+ ", typeFlagInterp=" + typeFlagInterp + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
				+ ", status=" + status + ", report=" + report + "]";
	}
	
}
