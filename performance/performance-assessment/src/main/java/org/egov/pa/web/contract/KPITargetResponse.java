package org.egov.pa.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.KpiTarget;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KPITargetResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("kpiTargets")
	private List<KpiTarget> kpiTargets = null;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<KpiTarget> getKpiTargets() {
		return kpiTargets;
	}

	public void setKpiTargets(List<KpiTarget> kpiTargets) {
		this.kpiTargets = kpiTargets;
	}
	
	

}
