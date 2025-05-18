package com.gl.ceir.config.model.constants;
public enum UserStatus {
	NEW(0, "New"), 
	OTP_VERIFICATION_PENDING(1, "OTP Verification Pending"), 
	PENDING_ADMIN_APPROVAL(2, "Pending Admin Approval"),
	APPROVED(3, "Approved"),
	REJECTED(4, "Rejected"), 
	DISABLE(5, "Disable"), 
	DEACTIVATE(6, "Deactivate"),
	NL(null,null);
	private Integer code;
	private String description;

	UserStatus(Integer code, String description) {
		this.code = code;
		this.description = description; 
	}       

	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}


	public static UserStatus getUserStatusByCode(Integer code) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getCode() == code) {
				return userStatus;
			}
		}
		return null;
	}

	public static UserStatus getUserStatusByDesc(String desc) {
		System.out.println("inside:::::::::::::::::::");
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getDescription().equals(desc)) {
				return userStatus;
			}
		}
		return null;
	}
}