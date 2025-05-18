package com.gl.ceir.config.model.constants;

public enum StolenStatus {
	
	INIT(0, "INIT"), PROCESSING(1, "Processing"), SUCCESS(2, "Success"), ERROR(3, "Error");

	private int code;
	private String desc;

	StolenStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static StolenStatus getActionNames(int code) {
		for (StolenStatus consignmentStatus : StolenStatus.values()) {
			if (consignmentStatus.getCode() == code)
				return consignmentStatus;
		}

		return null;
	}
}
