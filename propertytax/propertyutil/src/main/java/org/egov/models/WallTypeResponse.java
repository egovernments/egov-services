package org.egov.models;

import java.util.List;

public class WallTypeResponse {
	private ResponseInfo responseInfo;
	
	private List<WallType> wallTypes;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
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
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		WallTypeResponse other = (WallTypeResponse) obj;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
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
		return "WallTypeResponse [responseInfo=" + responseInfo + ", wallTypes=" + wallTypes + ", getResponseInfo()="
				+ getResponseInfo() + ", getWallTypes()=" + getWallTypes() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}

	public WallTypeResponse(ResponseInfo responseInfo, List<WallType> wallTypes) {
		super();
		this.responseInfo = responseInfo;
		this.wallTypes = wallTypes;
	}

	public WallTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
