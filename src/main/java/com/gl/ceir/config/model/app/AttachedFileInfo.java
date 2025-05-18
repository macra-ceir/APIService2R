package com.gl.ceir.config.model.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attached_file_info")
public class AttachedFileInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	private String fileName;
	
	@Column(name="grievance_id")
	private String grievanceId;
	
	@Column(name="message_id")
	private int messageId;
	
	private String docType;
	
	@Transient
	private String docTypeInterp;
	
	@Transient
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocTypeInterp() {
		return docTypeInterp;
	}

	public void setDocTypeInterp(String docTypeInterp) {
		this.docTypeInterp = docTypeInterp;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "AttachedFileInfo [id=" + id + ", fileName=" + fileName + ", grievanceId=" + grievanceId + ", messageId="
				+ messageId + ", docType=" + docType + ", docTypeInterp=" + docTypeInterp + ", url=" + url + "]";
	}
	
	
}
