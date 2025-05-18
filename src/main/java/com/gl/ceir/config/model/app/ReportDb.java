package com.gl.ceir.config.model.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "report_definition_detail")
public class ReportDb implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="report_name_id")
	private Long reportnameId;
	
	@Nonnull
	@Column(length = 50)
	private String reportName;
	
	private Integer status;
	
	private Integer reportOrder;
	
	@Column(length = 2000)
	private String mysqlInputQuery;
	
	@Column(length = 2000)
	private String oracleInputQuery;
	
	@Column(length = 100)
	private String outputTable;
	
	@Column(length = 1000)
	private String insertQuery;
	
	@Column(length = 1500)
	private String reportDataQuery;
	
	private Integer viewFlag;

	private String txnIdField;
	
	private String keyColumn;

	
	@Column(name="order_column_name")
	private String orderColumnName;
	
	@Column(name="order_by")
	private String orderBy;
	
	@Column(name="report_category")
	private Integer reportCategory;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;

	@Transient
	private List<ReportTrend> reportTrends;

	@Transient
	private Map<String,String> reportColumns;

	String  chartQuery;

	public String getReportName() {
		return reportName;
	}


	public void setReportName(String reportName) {
		this.reportName = reportName;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getReportOrder() {
		return reportOrder;
	}


	public void setReportOrder(Integer reportOrder) {
		this.reportOrder = reportOrder;
	}


	public String getMysqlInputQuery() {
		return mysqlInputQuery;
	}


	public void setMysqlInputQuery(String mysqlInputQuery) {
		this.mysqlInputQuery = mysqlInputQuery;
	}


	public String getOracleInputQuery() {
		return oracleInputQuery;
	}


	public void setOracleInputQuery(String oracleInputQuery) {
		this.oracleInputQuery = oracleInputQuery;
	}


	public String getOutputTable() {
		return outputTable;
	}


	public void setOutputTable(String outputTable) {
		this.outputTable = outputTable;
	}


	public String getInsertQuery() {
		return insertQuery;
	}


	public void setInsertQuery(String insertQuery) {
		this.insertQuery = insertQuery;
	}


	public String getReportDataQuery() {
		return reportDataQuery;
	}


	public void setReportDataQuery(String reportDataQuery) {
		this.reportDataQuery = reportDataQuery;
	}


	

	public String getTxnIdField() {
		return txnIdField;
	}


	public void setTxnIdField(String txnIdField) {
		this.txnIdField = txnIdField;
	}


	public String getKeyColumn() {
		return keyColumn;
	}


	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}


	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}


	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getOrderBy() {
		return orderBy;
	}


	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	

	public Integer getReportCategory() {
		return reportCategory;
	}


	public void setReportCategory(Integer reportCategory) {
		this.reportCategory = reportCategory;
	}

	public String getOrderColumnName() {
		return orderColumnName;
	}


	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}

	public List<ReportTrend> getReportTrends() {
		return reportTrends;
	}


	public void setReportTrends(List<ReportTrend> reportTrends) {
		this.reportTrends = reportTrends;
	}


	public Integer getViewFlag() {
		return viewFlag;
	}


	public void setViewFlag(Integer viewFlag) {
		this.viewFlag = viewFlag;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Long getReportNameId() {
		return reportnameId;
	}


	public void setReportNameId(Long reportNameId) {
		this.reportnameId = reportNameId;
	}


	public Map<String, String> getReportColumns() {
		return reportColumns;
	}

	public void setReportColumns(Map<String, String> reportColumns) {
		this.reportColumns = reportColumns;
	}

//	@Override
//	public String toString() {
//		return "ReportDb [reportNameId=" + reportnameId + ", reportName=" + reportName + ", status=" + status
//				+ ", reportOrder=" + reportOrder + ", mysqlInputQuery=" + mysqlInputQuery + ", oracleInputQuery="
//				+ oracleInputQuery + ", outputTable=" + outputTable + ", insertQuery=" + insertQuery
//				+ ", reportDataQuery=" + reportDataQuery + ", viewFlag=" + viewFlag + ", txnIdField=" + txnIdField
//				+ ", keyColumn=" + keyColumn + ", orderColumnName=" + orderColumnName + ", orderBy=" + orderBy
//				+ ", reportCategory=" + reportCategory + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
//				+ ", reportTrends=" + reportTrends + ", getReportName()=" + getReportName() + ", getStatus()="
//				+ getStatus() + ", getReportOrder()=" + getReportOrder() + ", getMysqlInputQuery()="
//				+ getMysqlInputQuery() + ", getOracleInputQuery()=" + getOracleInputQuery() + ", getOutputTable()="
//				+ getOutputTable() + ", getInsertQuery()=" + getInsertQuery() + ", getReportDataQuery()="
//				+ getReportDataQuery() + ", getTxnIdField()=" + getTxnIdField() + ", getKeyColumn()=" + getKeyColumn()
//				+ ", getCreatedOn()=" + getCreatedOn() + ", getModifiedOn()=" + getModifiedOn() + ", getOrderBy()="
//				+ getOrderBy() + ", getReportCategory()=" + getReportCategory() + ", getOrderColumnName()="
//				+ getOrderColumnName() + ", getReportTrends()=" + getReportTrends() + ", getViewFlag()=" + getViewFlag()
//				+ ", getReportNameId()=" + getReportNameId() + ", getClass()=" + getClass() + ", hashCode()="
//				+ hashCode() + ", toString()=" + super.toString() + "]";
//	}


	public String getChartQuery() {
		return chartQuery;
	}

	public void setChartQuery(String chartQuery) {
		this.chartQuery = chartQuery;
	}

	@Override
	public String toString() {
		return "ReportDb{" +
				"reportnameId=" + reportnameId +
				", reportName='" + reportName + '\'' +
				", status=" + status +
				", reportOrder=" + reportOrder +
				", mysqlInputQuery='" + mysqlInputQuery + '\'' +
				", oracleInputQuery='" + oracleInputQuery + '\'' +
				", outputTable='" + outputTable + '\'' +
				", insertQuery='" + insertQuery + '\'' +
				", reportDataQuery='" + reportDataQuery + '\'' +
				", viewFlag=" + viewFlag +
				", txnIdField='" + txnIdField + '\'' +
				", keyColumn='" + keyColumn + '\'' +
				", orderColumnName='" + orderColumnName + '\'' +
				", orderBy='" + orderBy + '\'' +
				", reportCategory=" + reportCategory +
				", createdOn=" + createdOn +
				", modifiedOn=" + modifiedOn +
				", reportTrends=" + reportTrends +
				", reportColumns=" + reportColumns +
				", chartQuery='" + chartQuery + '\'' +
				'}';
	}
}
