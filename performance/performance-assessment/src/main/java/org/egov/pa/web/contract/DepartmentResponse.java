package org.egov.pa.web.contract;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("MdmsRes")
	private MdmsRes mdmdRes;
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}
	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}
	public MdmsRes getMdmsRes() {
		return mdmdRes;
	}
	public void setMdmsRes(MdmsRes mdmsRes) {
		this.mdmdRes = mdmsRes;
	}
	
	
	
	

}
