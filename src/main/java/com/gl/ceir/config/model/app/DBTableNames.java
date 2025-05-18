package com.gl.ceir.config.model.app;

import java.util.List;

public class DBTableNames {
	
	private String dbName;
	private List< String > tableNames;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public List<String> getTableNames() {
		return tableNames;
	}
	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	
}
