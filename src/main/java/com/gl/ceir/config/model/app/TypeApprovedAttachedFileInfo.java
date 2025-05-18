package com.gl.ceir.config.model.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class TypeApprovedAttachedFileInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fileName;
	
	//private String txnId;
	
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

	/*public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}*/
	
	@Override
	public String toString() {
		return "TypeApprovedAttachedFileInfo [id=" + id + ", fileName=" + fileName + ", docType="
				+ docType + "]";
	}
	
}
