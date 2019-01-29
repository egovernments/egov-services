package org.egov.hrms.web.validator;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.model.Assignment;
import org.egov.hrms.model.DepartmentalTest;
import org.egov.hrms.model.EducationalQualification;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.Role;
import org.egov.hrms.model.ServiceHistory;
import org.egov.hrms.service.EmployeeService;
import org.egov.hrms.service.MDMSService;
import org.egov.hrms.service.UserService;
import org.egov.hrms.utils.HRMSConstants;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.hrms.web.contract.EmployeeResponse;
import org.egov.hrms.web.contract.EmployeeSearchCriteria;
import org.egov.hrms.web.contract.UserResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class EmployeeValidator {
	
	@Autowired
	private MDMSService mdmsService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;


	public void validateCreateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		validateExistingDuplicates(request ,errorMap);
		Map<String, List<String>> mdmsData = mdmsService.getMDMSData(request.getRequestInfo(), request.getEmployees().get(0).getTenantId());
		if(!CollectionUtils.isEmpty(mdmsData.keySet())){
			for(Employee employee: request.getEmployees()) {
//				validateMdmsData(employee, errorMap, mdmsData);
			}
		}else{
			log.info("MDMS data couldn't be fetched so skipping validaion!");
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}

	private void validateExistingDuplicates(EmployeeRequest request, Map<String, String> errorMap) {
		List<Employee> employees = request.getEmployees();
		validateEmployeeCode(employees,errorMap,request.getRequestInfo());
		validateUserMobile(employees,errorMap,request.getRequestInfo());

	}

	private void validateUserMobile(List<Employee> employees, Map<String, String> errorMap, RequestInfo requestInfo) {
		employees.forEach(employee -> {
			UserResponse userResponse = userService.getSingleUser(requestInfo,employee,"MobileNumber");
			if(!CollectionUtils.isEmpty(userResponse.getUser())){
				errorMap.put("HRMS_USER_EXIST_MOB","User already present for Mobile Number "+userResponse.getUser().get(0).getMobileNumber());
			}

		});
	}

	private void validateEmployeeCode(List<Employee> employees, Map<String, String> errorMap, RequestInfo requestInfo) {
        List < String> emoCodes = employees.stream().map(employee -> employee.getCode())
                                                    .collect(Collectors.toList());

        EmployeeResponse employeeResponse= employeeService.search(EmployeeSearchCriteria.builder().codes(emoCodes).build(),requestInfo);
			if(!CollectionUtils.isEmpty(employeeResponse.getEmployees()))
				errorMap.put("HRMS_INVALID_CODE","Employee Code already used for another employee");
	}

	private void validateMdmsData(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		validateEmployee(employee, errorMap, mdmsData);
		validateAssignments(employee, errorMap, mdmsData);
		validateJurisdiction(employee, errorMap, mdmsData);
		validateServiceHistory(employee, errorMap, mdmsData);
		validateEducationalDetails(employee, errorMap, mdmsData);
		validateDepartmentalTest(employee, errorMap, mdmsData);
	}
	
	public void validateDataConsistency(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData, Employee existingEmp) {
		validateConsistencyAssignment(existingEmp,employee,errorMap);
		validateConsistencyJurisdiction(existingEmp,employee,errorMap);
		validateConsistencyDepartmentalTest(existingEmp,employee,errorMap);
		validateConsistencyEducationalDetails(existingEmp,employee,errorMap);
		validateConsistencyServiceHistory(existingEmp,employee,errorMap);
		validateConsistencyEmployeeDocument(existingEmp,employee,errorMap);
		
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


	public void validateUpdateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		Map<String, List<String>> mdmsData = mdmsService.getMDMSData(request.getRequestInfo(), request.getEmployees().get(0).getTenantId());
		List <String> uuidList = request.getEmployees().stream().map(Employee :: getUuid).collect(Collectors.toList()); 
		EmployeeResponse existingEmployeeResponse = employeeService.search(EmployeeSearchCriteria.builder().uuids(uuidList).build(),request.getRequestInfo());
		List <Employee> existingEmployees = existingEmployeeResponse.getEmployees();
		for(Employee employee: request.getEmployees()){
			Employee existingEmp = existingEmployees.stream().filter(existingEmployee -> existingEmployee.getUuid().equals(employee.getUuid())).findFirst().get();
			validateDataConsistency(employee, errorMap, mdmsData, existingEmp);
			//		validateMdmsData(employee, errorMap, mdmsData);

		}
		
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {	
			throw new CustomException(errorMap);
		}


	}

	private void validateConsistencyJurisdiction(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap) {
		boolean check =
				updatedEmployeeData.getJurisdictions().stream()
						.map(jurisdiction -> jurisdiction.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getJurisdictions().stream()
								.map(jurisdiction -> jurisdiction.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put("HRMS_UPDATE_JURISDICTION_INCOSISTENT","Jurisdiction data in update request should contain all previous assginment data ");
		}

	}

	private void validateConsistencyAssignment(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap) {
		boolean check =
				updatedEmployeeData.getAssignments().stream()
						.map(assignment -> assignment.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getAssignments().stream()
								.map(assignment -> assignment.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put("HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT","Assignment data in update request should contain all previous assginment data ");
		}
	}

	private void validateConsistencyDepartmentalTest(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		boolean check =
				updatedEmployeeData.getTests().stream()
						.map(test -> test.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getTests().stream()
								.map(test -> test.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put("HRMS_UPDATE_TESTS_INCOSISTENT","Tests data in update request should contain all previous assginment data ");
		}

	}

	private void validateConsistencyEducationalDetails(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		boolean check =
				updatedEmployeeData.getEducation().stream()
						.map(educationalQualification -> educationalQualification.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getEducation().stream()
								.map(educationalQualification -> educationalQualification.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put("HRMS_UPDATE_EDUCATION_INCOSISTENT","Education data in update request should contain all previous assginment data ");
		}

	}

	private void validateConsistencyServiceHistory(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		boolean check =
				updatedEmployeeData.getServiceHistory().stream()
						.map(serviceHistory -> serviceHistory.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getServiceHistory().stream()
								.map(serviceHistory -> serviceHistory.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put("HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT","Service History data in update request should contain all previous assginment data ");
		}

	}

	private void validateConsistencyEmployeeDocument(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap) {
		boolean check =
				updatedEmployeeData.getDocuments().stream()
						.map(employeeDocument -> employeeDocument.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getDocuments().stream()
								.map(employeeDocument -> employeeDocument.getId())
								.collect(Collectors.toList()));
		if (!check) {
			errorMap.put("HRMS_UPDATE_DOCUMENT_INCOSISTENT", "Employee Document data in update request should contain all previous assginment data ");
		}

	}
}
