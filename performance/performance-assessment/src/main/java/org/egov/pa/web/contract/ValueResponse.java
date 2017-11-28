package org.egov.pa.web.contract;

import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;

public class ValueResponse {
	
	private String tenantId;
	private KPI kpi;
	private KpiValue kpiValue;
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
	
	public ValueResponse(String tenantId, KPI kpi, KpiValue value) {
		this.tenantId = tenantId; 
		this.kpi = kpi;
		this.kpiValue = value;
	}
	
	public ValueResponse() { 
	}
	
	
	

}
