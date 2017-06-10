package org.egov.models;

import java.util.List;

public class OccuapancyMasterRequest {

	private RequestInfo requestInfo;
	
	private List<OccuapancyMaster> occuapancyMasters;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
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
		OccuapancyMasterRequest other = (OccuapancyMasterRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OccuapancyMasterRequest [requestInfo=" + requestInfo + ", getRequestInfo()=" + getRequestInfo()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public OccuapancyMasterRequest(RequestInfo requestInfo, List<OccuapancyMaster> occuapancyMasters) {
		super();
		this.requestInfo = requestInfo;
		this.occuapancyMasters = occuapancyMasters;
	}

	public OccuapancyMasterRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
