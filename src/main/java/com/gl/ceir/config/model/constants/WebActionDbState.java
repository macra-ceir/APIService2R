package com.gl.ceir.config.model.constants;

public enum WebActionDbState {
	SUCCESS(0), INIT(1), PROCESSING(2), ERROR(3);
	
	private int code;

	WebActionDbState(int code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static WebActionDbState getActionNames(int code) {
		for (WebActionDbState codes : WebActionDbState.values()) {
			if (codes.equals(code))
				return codes;
		}

		return null;
	}
}
