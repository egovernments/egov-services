package org.egov.swagger.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.response.ResponseInfo;

public class ReportDataResponse {
	
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;
	
	public ResponseInfo getRequestInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo requestInfo) {
		this.responseInfo = requestInfo;
	}

	@JsonProperty("reportResponses")
	public List<ReportResponse> reportResponses = new ArrayList<ReportResponse>();

	public List<ReportResponse> getReportResponses() {
		return reportResponses;
	}

	public void setReportResponses(List<ReportResponse> reportResponses) {
		this.reportResponses = reportResponses;
	}
	
	

}
