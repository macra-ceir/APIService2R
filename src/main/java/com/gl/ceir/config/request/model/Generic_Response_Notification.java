package com.gl.ceir.config.request.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class Generic_Response_Notification {

	private Integer errorCode;
	private String tag;
	private String message;
	private String txnId;
	private List<RegisterationUser> data = null;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public List<RegisterationUser> getData() {
		return data;
	}
	public void setData(List<RegisterationUser> data) {
		this.data = data;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Generic_Response_Notification [errorCode=");
		builder.append(errorCode);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", message=");
		builder.append(message);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", data=");
		builder.append(data);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}
	
}
