package org.egov.models;

import java.util.List;

public class UsageMasterResponse {
	private ResponseInfo responseInfo;
	private List<UsageMaster> usageMasters;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
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
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		UsageMasterResponse other = (UsageMasterResponse) obj;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
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
		return "UsageMasterResponse [responseInfo=" + responseInfo + ", usageMasters=" + usageMasters
				+ ", getResponseInfo()=" + getResponseInfo() + ", getUsageMasters()=" + getUsageMasters()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public UsageMasterResponse(ResponseInfo responseInfo, List<UsageMaster> usageMasters) {
		super();
		this.responseInfo = responseInfo;
		this.usageMasters = usageMasters;
	}

	public UsageMasterResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
