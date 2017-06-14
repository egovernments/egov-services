package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WallTypeRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<WallType> wallTypes;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<WallType> getWallTypes() {
		return wallTypes;
	}

	public void setWallTypes(List<WallType> wallTypes) {
		this.wallTypes = wallTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
		result = prime * result + ((wallTypes == null) ? 0 : wallTypes.hashCode());
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
		WallTypeRequest other = (WallTypeRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		if (wallTypes == null) {
			if (other.wallTypes != null)
				return false;
		} else if (!wallTypes.equals(other.wallTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WallTypeRequest [requestInfo=" + requestInfo + ", wallTypes=" + wallTypes + ", getRequestInfo()="
				+ getRequestInfo() + ", getWallTypes()=" + getWallTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public WallTypeRequest(RequestInfo requestInfo, List<WallType> wallTypes) {
		super();
		this.requestInfo = requestInfo;
		this.wallTypes = wallTypes;
	}

	public WallTypeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
