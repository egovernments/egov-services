package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Collection of audit related fields used by most models Author : Narendra
 */

public class AuditDetails {
	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public Long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((lastModifiedBy == null) ? 0 : lastModifiedBy.hashCode());
		result = prime * result + ((lastModifiedTime == null) ? 0 : lastModifiedTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditDetails other = (AuditDetails) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (lastModifiedBy == null) {
			if (other.lastModifiedBy != null)
				return false;
		} else if (!lastModifiedBy.equals(other.lastModifiedBy))
			return false;
		if (lastModifiedTime == null) {
			if (other.lastModifiedTime != null)
				return false;
		} else if (!lastModifiedTime.equals(other.lastModifiedTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuditDetails [createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + ", createdTime="
				+ createdTime + ", lastModifiedTime=" + lastModifiedTime + ", getCreatedBy()=" + getCreatedBy()
				+ ", getLastModifiedBy()=" + getLastModifiedBy() + ", getCreatedTime()=" + getCreatedTime()
				+ ", getLastModifiedTime()=" + getLastModifiedTime() + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}

	public AuditDetails(String createdBy, String lastModifiedBy, Long createdTime, Long lastModifiedTime) {
		super();
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public AuditDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

}
