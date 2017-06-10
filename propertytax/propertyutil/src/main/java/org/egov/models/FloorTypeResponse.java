package org.egov.models;

import java.util.List;

public class FloorTypeResponse {

	private ResponseInfo ResponseInfo;

	private List<FloorType> floorTypes;

	public ResponseInfo getResponseInfo() {
		return ResponseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		ResponseInfo = responseInfo;
	}

	public List<FloorType> getFloorTypes() {
		return floorTypes;
	}

	public void setFloorTypes(List<FloorType> floorTypes) {
		this.floorTypes = floorTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ResponseInfo == null) ? 0 : ResponseInfo.hashCode());
		result = prime * result + ((floorTypes == null) ? 0 : floorTypes.hashCode());
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
		FloorTypeResponse other = (FloorTypeResponse) obj;
		if (ResponseInfo == null) {
			if (other.ResponseInfo != null)
				return false;
		} else if (!ResponseInfo.equals(other.ResponseInfo))
			return false;
		if (floorTypes == null) {
			if (other.floorTypes != null)
				return false;
		} else if (!floorTypes.equals(other.floorTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FloorTypeResponse [ResponseInfo=" + ResponseInfo + ", floorTypes=" + floorTypes + ", getResponseInfo()="
				+ getResponseInfo() + ", getFloorTypes()=" + getFloorTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public FloorTypeResponse(org.egov.models.ResponseInfo responseInfo, List<FloorType> floorTypes) {
		super();
		ResponseInfo = responseInfo;
		this.floorTypes = floorTypes;
	}

	public FloorTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
