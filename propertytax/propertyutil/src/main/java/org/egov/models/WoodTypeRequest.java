package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WoodTypeRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<WoodType> woodTypes;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<WoodType> getWoodTypes() {
		return woodTypes;
	}

	public void setWoodTypes(List<WoodType> woodTypes) {
		this.woodTypes = woodTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
		result = prime * result + ((woodTypes == null) ? 0 : woodTypes.hashCode());
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
		WoodTypeRequest other = (WoodTypeRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		if (woodTypes == null) {
			if (other.woodTypes != null)
				return false;
		} else if (!woodTypes.equals(other.woodTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WoodTypeRequest [requestInfo=" + requestInfo + ", woodTypes=" + woodTypes + ", getRequestInfo()="
				+ getRequestInfo() + ", getWoodTypes()=" + getWoodTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public WoodTypeRequest(RequestInfo requestInfo, List<WoodType> woodTypes) {
		super();
		this.requestInfo = requestInfo;
		this.woodTypes = woodTypes;
	}

	public WoodTypeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
