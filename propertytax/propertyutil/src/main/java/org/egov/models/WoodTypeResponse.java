package org.egov.models;

import java.util.List;

public class WoodTypeResponse {
	private ResponseInfo responseInfo;

	private List<WoodType> woodTypes;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
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
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		WoodTypeResponse other = (WoodTypeResponse) obj;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
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
		return "WoodTypeResponse [responseInfo=" + responseInfo + ", woodTypes=" + woodTypes + ", getResponseInfo()="
				+ getResponseInfo() + ", getWoodTypes()=" + getWoodTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public WoodTypeResponse(ResponseInfo responseInfo, List<WoodType> woodTypes) {
		super();
		this.responseInfo = responseInfo;
		this.woodTypes = woodTypes;
	}

	public WoodTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
