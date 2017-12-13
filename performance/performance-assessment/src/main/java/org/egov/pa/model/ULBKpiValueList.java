package org.egov.pa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ULBKpiValueList {
	
	@JsonProperty("ulbName")
	private String ulbName;
	
	@JsonProperty("finYears")
	private List<ValueList> finYearList;

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	public List<ValueList> getFinYearList() {
		return finYearList;
	}

	public void setFinYearList(List<ValueList> finYearList) {
		this.finYearList = finYearList;
	} 

	
}
