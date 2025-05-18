package com.gl.ceir.config.model.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public enum TypeApproveDBOrderColumnMapping {
		
	createdOn("Created On"), modifiedOn("Modified On"), txnId("Transaction ID"), tac("TAC"), userId("User ID"),
	userType("User Type"), approveStatus("Status"), manufacturerCountry("Country"), trademark("trademark"), productName("brandName"), modelNumber("modelNumber");

	private String column;
	
	private static final Logger logger = LogManager.getLogger(TypeApproveDBOrderColumnMapping.class);

	TypeApproveDBOrderColumnMapping(String column) {
		this.column = column;
	}

	public String getColumn() {
		return column;
	}

	public static TypeApproveDBOrderColumnMapping getColumnMapping(String column) {
		if( column.equalsIgnoreCase("Display Name"))
			column = "User ID";
		for (TypeApproveDBOrderColumnMapping columns : TypeApproveDBOrderColumnMapping.values()) {
//			logger.info("Column to match:["+column+"] and current column value:["+columns.column+"]");
			if (columns.column.equals(column) )
				return columns;
		}

		return null;
	}
	
}

