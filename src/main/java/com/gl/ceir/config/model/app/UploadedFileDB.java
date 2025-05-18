package com.gl.ceir.config.model.app;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gl.ceir.config.model.constants.CopyStatus;

@Entity
public class UploadedFileDB {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	private Date createdOn;
	
	private String txnId;
	
	private String filePath;
	
	private String fileName;
	
	@UpdateTimestamp
	private Date copyStartOn;
	
	@UpdateTimestamp
	private Date copyEndOn;
	
	@ColumnDefault("'NEW'")
	@Enumerated(EnumType.STRING)
	private CopyStatus copyStatus;
	
	private Long serverId;
	
	private int retryCount;

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
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

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCopyStartOn() {
		return copyStartOn;
	}

	public void setCopyStartOn(Date copyStartOn) {
		this.copyStartOn = copyStartOn;
	}

	public Date getCopyEndOn() {
		return copyEndOn;
	}

	public void setCopyEndOn(Date copyEndOn) {
		this.copyEndOn = copyEndOn;
	}

	public CopyStatus getCopyStatus() {
		return copyStatus;
	}

	public void setCopyStatus(CopyStatus copyStatus) {
		this.copyStatus = copyStatus;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Override
	public String toString() {
		return "UploadedFileDB [id=" + id + ", createdOn=" + createdOn + ", txnId=" + txnId + ", filePath=" + filePath
				+ ", fileName=" + fileName + ", copyStartOn=" + copyStartOn + ", copyEndOn=" + copyEndOn
				+ ", copyStatus=" + copyStatus + ", serverId=" + serverId + ", retryCount=" + retryCount + "]";
	}
	
}
