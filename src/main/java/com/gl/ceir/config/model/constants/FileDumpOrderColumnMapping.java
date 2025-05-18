package com.gl.ceir.config.model.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public enum FileDumpOrderColumnMapping {
		
	createdOn("Created On"), modifiedOn("Modified On"), fileName("File Name"), fileType("File Type");

	private String column;

	FileDumpOrderColumnMapping(String column) {
		this.column = column;
	}

	public String getColumn() {
		return column;
	}

	public static FileDumpOrderColumnMapping getColumnMapping(String column) {
		for (FileDumpOrderColumnMapping columns : FileDumpOrderColumnMapping.values()) {
			if (columns.column.equals(column) )
				return columns;
		}

		return null;
	}
	
}

