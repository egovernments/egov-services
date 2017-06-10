package org.egov.models;

import java.util.List;

public class FloorTypeRequest {

	private RequestInfo requestInfo;

	private List<FloorType> floorTypes;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
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
		result = prime * result + ((floorTypes == null) ? 0 : floorTypes.hashCode());
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
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
		FloorTypeRequest other = (FloorTypeRequest) obj;
		if (floorTypes == null) {
			if (other.floorTypes != null)
				return false;
		} else if (!floorTypes.equals(other.floorTypes))
			return false;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		return true;
	}

	public FloorTypeRequest(RequestInfo requestInfo, List<FloorType> floorTypes) {
		super();
		this.requestInfo = requestInfo;
		this.floorTypes = floorTypes;
	}

	public FloorTypeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FloorTypeRequest [requestInfo=" + requestInfo + ", floorTypes=" + floorTypes + ", getRequestInfo()="
				+ getRequestInfo() + ", getFloorTypes()=" + getFloorTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}


}
