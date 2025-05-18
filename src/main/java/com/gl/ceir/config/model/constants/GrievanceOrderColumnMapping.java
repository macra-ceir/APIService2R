package com.gl.ceir.config.model.constants;

public enum GrievanceOrderColumnMapping {
		
	createdOn("Created On"), modifiedOn("Modified On"), txnId("Transaction ID"), grievanceId("Grievance ID"), userId("User ID"),
	userType("User Type"), raisedBy("Raised By"), grievanceStatus("Status");

	private String column;

	GrievanceOrderColumnMapping(String column) {
		this.column = column;
	}

	public String getColumn() {
		return column;
	}

	public static GrievanceOrderColumnMapping getColumnMapping(String column) {
		for (GrievanceOrderColumnMapping columns : GrievanceOrderColumnMapping.values()) {
			if ( columns.column.equals(column) )
				return columns;
		}

		return null;
	}
	
}
