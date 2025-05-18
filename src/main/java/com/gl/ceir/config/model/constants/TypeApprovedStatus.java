package com.gl.ceir.config.model.constants;

public enum TypeApprovedStatus {
	APPROVED(0), REJECTED(1), NO_ACTION_YET(-1), INIT(2), Withdrawn_By_Importer(8);

	private int code;

	TypeApprovedStatus(int code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static TypeApprovedStatus getActionNames(int code) {
		for (TypeApprovedStatus codes : TypeApprovedStatus.values()) {
			if (codes.code == code )
				return codes;
		}

		return null;
	}
}