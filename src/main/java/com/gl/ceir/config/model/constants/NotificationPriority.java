package com.gl.ceir.config.model.constants;

public enum NotificationPriority {
	LOW(1), HIGH(2);
	
	private int code;

	NotificationPriority(int code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static NotificationPriority getActionNames(int code) {
		for (NotificationPriority codes : NotificationPriority.values()) {
			if (codes.getCode() == code)
				return codes;
		}

		return null;
	}
}
