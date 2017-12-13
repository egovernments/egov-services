package org.egov.pa.web.contract;

import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;

public class ValueResponse {
	
	private String tenantId;
	private KPI kpi;
	private KpiValue kpiValue;
	private String finYear;
	private String graphType;
	
	
	
	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public String getGraphType() {
		return graphType;
	}
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public KPI getKpi() {
		return kpi;
	}
	public void setKpi(KPI kpi) {
		this.kpi = kpi;
	}
	public KpiValue getKpiValue() {
		return kpiValue;
	}
	public void setKpiValue(KpiValue kpiValue) {
		this.kpiValue = kpiValue;
	} 
	
	public ValueResponse(String tenantId, KPI kpi, KpiValue value, String graphType) {
		this.tenantId = tenantId; 
		this.kpi = kpi;
		this.kpiValue = value;
		this.graphType = graphType; 
	}
	
	public ValueResponse() { 
	}
	
	
	

}
