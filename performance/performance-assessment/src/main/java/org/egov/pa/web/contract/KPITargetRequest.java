package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pa.model.KpiTarget;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KPITargetRequest {
	
	@JsonProperty("RequestInfo")
	  private RequestInfo requestInfo = null;

	  @JsonProperty("kpiTargets")
	  private List<KpiTarget> kpiTargets = new ArrayList<KpiTarget>();

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<KpiTarget> getKpiTargets() {
		return kpiTargets;
	}

	public void setKpiTargets(List<KpiTarget> kpiTargets) {
		this.kpiTargets = kpiTargets;
	}
	  
	  

}
