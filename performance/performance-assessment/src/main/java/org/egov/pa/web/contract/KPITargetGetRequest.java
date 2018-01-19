package org.egov.pa.web.contract;

import java.util.List;

public class KPITargetGetRequest {
	
	private List<String> kpiCode; 
	private List<String> finYear;
	private List<Long> departmentId; 
	private List<String> categoryId; 
	private String tenantId;
	
	
	
	
	
	
	
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<String> getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(List<String> categoryId) {
		this.categoryId = categoryId;
	}
	public List<Long> getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(List<Long> departmentId) {
		this.departmentId = departmentId;
	}
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
