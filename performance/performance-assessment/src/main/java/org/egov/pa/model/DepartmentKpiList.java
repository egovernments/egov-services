package org.egov.pa.model;

import java.util.List;

public class DepartmentKpiList {
	
	private Department department; 
	private List<KPI> kpiList;
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public List<KPI> getKpiList() {
		return kpiList;
	}
	public void setKpiList(List<KPI> kpiList) {
		this.kpiList = kpiList;
	} 
	
	

}
