package org.egov.hrms.web.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.hrms.model.Assignment;
import org.egov.hrms.model.DepartmentalTest;
import org.egov.hrms.model.EducationalQualification;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.Role;
import org.egov.hrms.model.ServiceHistory;
import org.egov.hrms.service.MDMSService;
import org.egov.hrms.utils.HRMSConstants;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public class EmployeeValidator {
	
	@Autowired
	private MDMSService mdmsService;
	
	public void validateCreateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		Map<String, List<String>> mdmsData = mdmsService.getMDMSData(request.getRequestInfo(), request.getEmployees().get(0).getTenantId());
		for(Employee employee: request.getEmployees()) {
			validateEmployee(employee, errorMap, mdmsData);
			validateAssignments(employee, errorMap, mdmsData);
			validateJurisdiction(employee, errorMap, mdmsData);
			validateServiceHistory(employee, errorMap, mdmsData);
			validateEducationalDetails(employee, errorMap, mdmsData);
			validateDepartmentalTest(employee, errorMap, mdmsData);
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}
	
	private void validateEmployee(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		if(employee.getUser().getMobileNumber().length() != 10)
			errorMap.put("HRMS_INVALID_MOB_NO", "Mobile number of the employee is invalid!");
		
		if(CollectionUtils.isEmpty(employee.getUser().getRoles()))
			errorMap.put("HRMS_INVALID_ROLES", "An employee must have atleast one role!");
		else {
			for(Role role: employee.getUser().getRoles()) {
				if(!mdmsData.get(HRMSConstants.HRMS_MDMS_ROLES_CODE).contains(role.getCode()))
					errorMap.put("HRMS_INVALID_ROLE", "Employee contains an invalid role: " + role.getCode());
			}
		}
		
		if(!mdmsData.get(HRMSConstants.HRMS_MDMS_EMP_STATUS_CODE).contains(employee.getEmployeeStatus()))
			errorMap.put("HRMS_INVALID_EMP_STATUS", "Employee status is invalid!");
		if(!mdmsData.get(HRMSConstants.HRMS_MDMS_EMP_TYPE_CODE).contains(employee.getEmployeeType()))
			errorMap.put("HRMS_INVALID_EMP_TYPE", "Employee type is invalid!");
		if(employee.getDateOfAppointment() > new Date().getTime())
			errorMap.put("HRMS_INVALID_DATE_OF_APPOINTMENT", "Employee date of appointment is invalid!");
	}
	
	private void validateAssignments(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		for(Assignment assignment: employee.getAssignments()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DEPT_CODE).contains(assignment.getDepartment()))
				errorMap.put("HRMS_INVALID_DEPT", "Department of the employee is invalid!");
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DESG_CODE).contains(assignment.getDesignation()))
				errorMap.put("HRMS_INVALID_DESG", "Designation of the employee is invalid!");
			if(assignment.getFromDate() > new Date().getTime() || assignment.getToDate() > new Date().getTime() 
					|| assignment.getFromDate() > assignment.getToDate())
				errorMap.put("HRMS_INVALID_ASSIGNMENT_PERIOD", "Period of assignemnt (fromDate to toDate) is invalid!");
		}
		
	}
	
	private void validateJurisdiction(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		//add validation for jurisdictions.
	}
	
	private void validateServiceHistory(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		for(ServiceHistory history: employee.getServiceHistory()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_SERVICE_STATUS_CODE).contains(history.getServiceStatus()))
				errorMap.put("HRMS_INVALID_SERVICE_STATUS", "Service status of the employee is invalid!: "+history.getServiceStatus());	
			if(history.getServiceFrom() > new Date().getTime() || history.getServiceTo() > new Date().getTime() 
					|| history.getServiceFrom() > history.getServiceTo())
				errorMap.put("HRMS_INVALID_SERVICE_PERIOD", "Service period (serviceFrom to serviceTo) of the employee is invalid!");	
		}
	}
	
	private void validateEducationalDetails(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		for(EducationalQualification education : employee.getEducation()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_QUALIFICATION_CODE).contains(education.getQualification()))
				errorMap.put("HRMS_INVALID_QUALIFICATION", "Qualification of the employee is invalid!: "+education.getQualification());	
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_STREAMS_CODE).contains(education.getStream()))
				errorMap.put("HRMS_INVALID_EDUCATIONAL_STREAM", "Education stream of the employee is invalid!: "+education.getStream());
		}
	}
	
	private void validateDepartmentalTest(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		for(DepartmentalTest test: employee.getTests()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DEPT_TEST_CODE).contains(test.getTest()))
				errorMap.put("HRMS_INVALID_DEPARTMENTAL_TEST", "Departmental test of the employee is invalid!: "+test.getTest());
		}
		
	}


}
