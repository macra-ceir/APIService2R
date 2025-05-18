package com.gl.ceir.config.model.app;

public class ReportTrend {
	
	private Integer typeFlag;
	
	private String typeFlagInterp;
	
	public ReportTrend() {}
	
	public ReportTrend( Integer typeFlag, String typeFlagInterp ) {
		this.typeFlag = typeFlag;
		this.typeFlagInterp = typeFlagInterp;
	}

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public String getTypeFlagInterp() {
		return typeFlagInterp;
	}

	public void setTypeFlagInterp(String typeFlagInterp) {
		this.typeFlagInterp = typeFlagInterp;
	}

	@Override
	public String toString() {
		return "ReportTrend [typeFlag=" + typeFlag + ", typeFlagInterp=" + typeFlagInterp + "]";
	}
	
}
