package org.egov.pa.web.contract;

import java.util.List;

public class KPITargetGetRequest {
	
	private List<String> kpiCode; 
	private List<String> finYear;
	public List<String> getKpiCode() {
		return kpiCode;
	}
	public void setKpiCode(List<String> kpiCode) {
		this.kpiCode = kpiCode;
	}
	public List<String> getFinYear() {
		return finYear;
	}
	public void setFinYear(List<String> finYear) {
		this.finYear = finYear;
	}

	
	
}
