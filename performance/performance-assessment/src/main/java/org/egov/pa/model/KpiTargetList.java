package org.egov.pa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KpiTargetList {
	
	@JsonProperty("kpiTargets")
	private List<KpiTarget> targetList ;

	
	public List<KpiTarget> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<KpiTarget> targetList) {
		this.targetList = targetList;
	} 
	
}
