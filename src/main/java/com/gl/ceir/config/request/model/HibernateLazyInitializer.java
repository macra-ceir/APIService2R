package com.gl.ceir.config.request.model;

import java.util.HashMap;
import java.util.Map;

public class HibernateLazyInitializer {
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HibernateLazyInitializer [additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
