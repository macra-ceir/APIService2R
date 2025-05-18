package com.gl.ceir.config.model.app;

import java.util.List;
import java.util.Map;

public class TableDataPageable {
//	private List<Map<String,String>> content;
	private Object content;
	private DataPageable pageable;
	private Integer totalPages;
	private long totalElements;
	private boolean last;
	private boolean first;
	private Integer numberOfElements;
	private Integer size;
	private Integer number;
	private boolean empty;
	private ReportDataSorting sort;

	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public DataPageable getPageable() {
		return pageable;
	}
	public void setPageable(DataPageable pageable) {
		this.pageable = pageable;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public Integer getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public ReportDataSorting getSort() {
		return sort;
	}
	public void setSort(ReportDataSorting sort) {
		this.sort = sort;
	}
}
