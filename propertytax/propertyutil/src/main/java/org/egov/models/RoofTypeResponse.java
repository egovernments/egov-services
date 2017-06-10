package org.egov.models;

import java.util.List;

public class RoofTypeResponse {
	private ResponseInfo responseInfo;

	private List<RoofType> roofTypes;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
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
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		RoofTypeResponse other = (RoofTypeResponse) obj;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
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
		return "RoofTypeResponse [responseInfo=" + responseInfo + ", roofTypes=" + roofTypes + ", getResponseInfo()="
				+ getResponseInfo() + ", getRoofTypes()=" + getRoofTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public RoofTypeResponse(ResponseInfo responseInfo, List<RoofType> roofTypes) {
		super();
		this.responseInfo = responseInfo;
		this.roofTypes = roofTypes;
	}

	public RoofTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


}
