package org.egov.eis.indexer.model.es;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class EmployeeIndex {
	
	private EmployeeDetails employeeDetails;

	private List<EmployeeAssignment> employeeAssignment = new ArrayList<>();

	private List<EmployeeRegularisation> employeeRegularisation = new ArrayList<>();

	private List<EmployeeTechnical> employeeTechnical = new ArrayList<>();

	private List<EmployeeServiceHistory> employeeService = new ArrayList<>();

	private List<EmployeeEducation> employeeEducation = new ArrayList<>();

	private List<EmployeeJurisdiction> employeeJurisdiction = new ArrayList<>();

	private List<EmployeeProbation> employeeProbation = new ArrayList<>();

	private List<EmployeeTest> employeeTest = new ArrayList<>();
	
}
	