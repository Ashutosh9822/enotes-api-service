package com.becoder.entity;

import java.sql.Date;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModel {

	private Boolean isActive;

	private Boolean isDeleted;

	private Integer createdBy;

	private Date createdOn;

	private Integer updatedBy;

	private Date updatedOn;
	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "BaseModel [isActive=" + isActive + ", isDeleted=" + isDeleted + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
	}


}
