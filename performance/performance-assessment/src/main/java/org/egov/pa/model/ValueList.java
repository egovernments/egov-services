package org.egov.pa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValueList {
	
	@JsonProperty("finYear")
	private String finYear;
	
	@JsonProperty("kpiValues")
	private List<KpiValue> kpiValueList;

	public List<KpiValue> getKpiValueList() {
		return kpiValueList;
	}

	public void setKpiValueList(List<KpiValue> kpiValueList) {
		this.kpiValueList = kpiValueList;
	}

	public String getFinYear() {
		return finYear;
	}

	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	
	
	

}
