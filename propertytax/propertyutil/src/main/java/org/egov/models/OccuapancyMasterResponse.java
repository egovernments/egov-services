package org.egov.models;

import java.util.List;

public class OccuapancyMasterResponse {
	private ResponseInfo responseInfo;

	private List<OccuapancyMaster> occuapancyMasters;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<OccuapancyMaster> getOccuapancyMasters() {
		return occuapancyMasters;
	}

	public void setOccuapancyMasters(List<OccuapancyMaster> occuapancyMasters) {
		this.occuapancyMasters = occuapancyMasters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((occuapancyMasters == null) ? 0 : occuapancyMasters.hashCode());
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		OccuapancyMasterResponse other = (OccuapancyMasterResponse) obj;
		if (occuapancyMasters == null) {
			if (other.occuapancyMasters != null)
				return false;
		} else if (!occuapancyMasters.equals(other.occuapancyMasters))
			return false;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OccuapancyMasterResponse [responseInfo=" + responseInfo + ", occuapancyMasters=" + occuapancyMasters
				+ ", getResponseInfo()=" + getResponseInfo() + ", getOccuapancyMasters()=" + getOccuapancyMasters()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public OccuapancyMasterResponse(ResponseInfo responseInfo, List<OccuapancyMaster> occuapancyMasters) {
		super();
		this.responseInfo = responseInfo;
		this.occuapancyMasters = occuapancyMasters;
	}

	public OccuapancyMasterResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


}
