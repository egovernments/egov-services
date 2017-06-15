package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsageMasterRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<UsageMaster> usageMasters;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<UsageMaster> getUsageMasters() {
		return usageMasters;
	}

	public void setUsageMasters(List<UsageMaster> usageMasters) {
		this.usageMasters = usageMasters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
		result = prime * result + ((usageMasters == null) ? 0 : usageMasters.hashCode());
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
		UsageMasterRequest other = (UsageMasterRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		if (usageMasters == null) {
			if (other.usageMasters != null)
				return false;
		} else if (!usageMasters.equals(other.usageMasters))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsageMasterRequest [requestInfo=" + requestInfo + ", usageMasters=" + usageMasters
				+ ", getRequestInfo()=" + getRequestInfo() + ", getUsageMasters()=" + getUsageMasters()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public UsageMasterRequest(RequestInfo requestInfo, List<UsageMaster> usageMasters) {
		super();
		this.requestInfo = requestInfo;
		this.usageMasters = usageMasters;
	}

	public UsageMasterRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


}
