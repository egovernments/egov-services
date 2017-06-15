package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoofTypeRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<RoofType> roofTypes;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<RoofType> getRoofTypes() {
		return roofTypes;
	}

	public void setRoofTypes(List<RoofType> roofTypes) {
		this.roofTypes = roofTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
		result = prime * result + ((roofTypes == null) ? 0 : roofTypes.hashCode());
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
		RoofTypeRequest other = (RoofTypeRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		if (roofTypes == null) {
			if (other.roofTypes != null)
				return false;
		} else if (!roofTypes.equals(other.roofTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoofTypeRequest [requestInfo=" + requestInfo + ", roofTypes=" + roofTypes + ", getRequestInfo()="
				+ getRequestInfo() + ", getRoofTypes()=" + getRoofTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public RoofTypeRequest(RequestInfo requestInfo, List<RoofType> roofTypes) {
		super();
		this.requestInfo = requestInfo;
		this.roofTypes = roofTypes;
	}

	public RoofTypeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


}
