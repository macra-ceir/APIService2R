package com.gl.ceir.config.model.app;

import java.util.List;
import java.util.Map;

public class TableData {
	private String dbName;
	private String tableName;
	private List<String> columns;
	private List<Map<String,String>> rowData;
	public TableData() {}
	public TableData( String tableName, List<String> columns, List<Map<String,String>> rowData ) {
		this.tableName = tableName;
		this.columns = columns;
		this.rowData = rowData;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<Map<String, String>> getRowData() {
		return rowData;
	}
	public void setRowData(List<Map<String, String>> rowData) {
		this.rowData = rowData;
	}
	@Override
	public String toString() {
		return "TableData [dbName=" + dbName + ", tableName=" + tableName + ", columns=" + columns.toString() + ", rowData="
				+ rowData.toString() + "]";
	}
}
