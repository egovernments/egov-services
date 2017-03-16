package org.egov.eis.index.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeIndex {
	
	EmployeeDetails employeeDetails;
	List<EmployeeAssignment> employeeAssignment;
	List<EmployeeRegularisation> employeeRegularisation;
	List<EmployeeTechnical> employeeTechnical;
	List<EmployeeServiceHistory> employeeService;
	List<EmployeeEducation> employeeEducation;
	List<EmployeeJurisdiction> employeeJurisdiction;
	List<EmployeeProbation> employeeprobation;
	List<EmployeeTest> employeeTest;
	
}
	