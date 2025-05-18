package com.gl.ceir.config.model.app;

public class TypeApproveDelete{
	private Long importerId;
	private Long userId;
	private String txnId;
	private String tac;
	
	/*public TypeApproveDelete( Long importerId, Long userId, String txnId) {
		this.importerId = importerId;
		this.userId = userId;
		this.txnId = txnId;
	}*/
	
	public Long getImporterId() {
		return importerId;
	}
	public void setImporterId(Long importerId) {
		this.importerId = importerId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	@Override
	public String toString() {
		return "TypeApproveDelete [importerId=" + importerId + ", userId=" + userId + ", txnId=" + txnId + ", tac="
				+ tac + "]";
	}
	
}
