package com.gl.ceir.config.model.app;

public class DataPageable {
	
	private ReportDataSorting sort;
	private Integer pageSize;
	private Integer pageNumber;
	private Integer offset;
	private boolean unpaged;
	private boolean paged;
	public DataPageable() {
		// TODO Auto-generated constructor stub
	}
	public DataPageable( ReportDataSorting sort, Integer pageSize, Integer pageNumber, Integer offset, boolean unpaged, boolean paged) {
		this.sort = sort;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.offset = offset;
		this.unpaged = unpaged;
		this.paged = paged;
	}
	public ReportDataSorting getSort() {
		return sort;
	}
	public void setSort(ReportDataSorting sort) {
		this.sort = sort;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public boolean isUnpaged() {
		return unpaged;
	}
	public void setUnpaged(boolean unpaged) {
		this.unpaged = unpaged;
	}
	public boolean isPaged() {
		return paged;
	}
	public void setPaged(boolean paged) {
		this.paged = paged;
	}
	
}
