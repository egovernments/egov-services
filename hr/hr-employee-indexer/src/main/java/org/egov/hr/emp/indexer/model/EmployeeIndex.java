package org.egov.hr.emp.indexer.model;

import java.util.List;

import org.egov.hr.emp.indexer.model.es.EmployeeAssignment;
import org.egov.hr.emp.indexer.model.es.EmployeeDetails;
import org.egov.hr.emp.indexer.model.es.EmployeeEducation;
import org.egov.hr.emp.indexer.model.es.EmployeeJurisdiction;
import org.egov.hr.emp.indexer.model.es.EmployeeProbation;
import org.egov.hr.emp.indexer.model.es.EmployeeRegularisation;
import org.egov.hr.emp.indexer.model.es.EmployeeServiceHistory;
import org.egov.hr.emp.indexer.model.es.EmployeeTechnical;
import org.egov.hr.emp.indexer.model.es.EmployeeTest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
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
	