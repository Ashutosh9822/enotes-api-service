package com.becoder.dto;

import java.util.List;

public class NotesResponse {
	
	private List<NotesDto> notes; 
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private Integer totalElements;
	
	private Integer totalPages;
	
	private Boolean isFirst;
	
	private Boolean isLast;

	public List<NotesDto> getNotes() {
		return notes;
	}

	public void setNotes(List<NotesDto> notes) {
		this.notes = notes;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	@Override
	public String toString() {
		return "NotesResponse [pageNo=" + pageNo + ", pageSize=" + pageSize + ", totalElements=" + totalElements
				+ ", totalPages=" + totalPages + ", isFirst=" + isFirst + ", isLast=" + isLast + "]";
	}
	
	
	
}
