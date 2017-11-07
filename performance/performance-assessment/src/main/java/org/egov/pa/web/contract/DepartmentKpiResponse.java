package org.egov.pa.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.DepartmentKpiList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentKpiResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("KPIs")
	private List<DepartmentKpiList> kpIs = null;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<DepartmentKpiList> getKpIs() {
		return kpIs;
	}

	public void setKpIs(List<DepartmentKpiList> kpIs) {
		this.kpIs = kpIs;
	}

}
