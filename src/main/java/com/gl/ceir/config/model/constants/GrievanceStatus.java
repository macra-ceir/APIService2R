package com.gl.ceir.config.model.constants;

public enum GrievanceStatus {
	New(0), Pending_With_Aadmin(1), Pending_With_User(2), Closed(3);

	private int code;

	GrievanceStatus(int code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static GrievanceStatus getActionNames(int code) {
		for (GrievanceStatus codes : GrievanceStatus.values()) {
			if (codes.code == code )
				return codes;
		}

		return null;
	}
}
