package org.egov.hrms.web.validator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.model.*;
import org.egov.hrms.service.EmployeeService;
import org.egov.hrms.service.MDMSService;
import org.egov.hrms.service.UserService;
import org.egov.hrms.utils.ErrorConstants;
import org.egov.hrms.utils.HRMSConstants;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.hrms.web.contract.EmployeeResponse;
import org.egov.hrms.web.contract.EmployeeSearchCriteria;
import org.egov.hrms.web.contract.UserResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeValidator {
	
	@Autowired
	private MDMSService mdmsService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;


	/**
	 * Validates employee request for create. Validations include:
	 * 1. Validating MDMS codes
	 * 2. Performing data sanity checks
	 * 
	 * @param request
	 */
	public void validateCreateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		validateExistingDuplicates(request ,errorMap);
		if(!CollectionUtils.isEmpty(errorMap.keySet()))
			throw new CustomException(errorMap);
		Map<String, List<String>> mdmsData = mdmsService.getMDMSData(request.getRequestInfo(), request.getEmployees().get(0).getTenantId());
		if(!CollectionUtils.isEmpty(mdmsData.keySet())){
			request.getEmployees().stream().forEach(employee -> validateMdmsData(employee, errorMap, mdmsData));
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet()))
			throw new CustomException(errorMap);
	}

	/**
	 * Checks if the employee being created is duplicate with the following:
	 * 1. Validating mobile number
	 * 2. Validating username
	 * 
	 * @param request
	 * @param errorMap
	 */
	private void validateExistingDuplicates(EmployeeRequest request, Map<String, String> errorMap) {
		List<Employee> employees = request.getEmployees();
        validateUserMobile(employees,errorMap,request.getRequestInfo());
        validateUserName(employees,errorMap,request.getRequestInfo());
	}

	/**
	 * Checks if the mobile number is duplicate.
	 * 
	 * @param employees
	 * @param errorMap
	 * @param requestInfo
	 */
    private void validateUserMobile(List<Employee> employees, Map<String, String> errorMap, RequestInfo requestInfo) {
        employees.forEach(employee -> {
            UserResponse userResponse = userService.getSingleUser(requestInfo,employee,"MobileNumber");
            if(!CollectionUtils.isEmpty(userResponse.getUser())){
                errorMap.put(ErrorConstants.HRMS_USER_EXIST_MOB_CODE,
                		ErrorConstants.HRMS_USER_EXIST_MOB_MSG+userResponse.getUser().get(0).getMobileNumber());
            }
        });
    }

    /**
     * Checks if the username is duplicate
     * 
     * @param employees
     * @param errorMap
     * @param requestInfo
     */
    private void validateUserName(List<Employee> employees, Map<String, String> errorMap, RequestInfo requestInfo) {
        employees.forEach(employee -> {
            if(employee.getCode()!=null){
                UserResponse userResponse = userService.getSingleUser(requestInfo,employee,"UserName");
                if(!CollectionUtils.isEmpty(userResponse.getUser())){
                    log.info("User: "+ (userResponse.getUser().get(0)));
                    errorMap.put(ErrorConstants.HRMS_USER_EXIST_USERNAME_CODE,
                    		ErrorConstants.HRMS_USER_EXIST_USERNAME_MSG+userResponse.getUser().get(0).getUserName());
                }
            }
        });
    }

    /**
     * Validates MDMS codes of the request.
     * 
     * @param employee
     * @param errorMap
     * @param mdmsData
     */
	private void validateMdmsData(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		validateEmployee(employee, errorMap, mdmsData);
		validateAssignments(employee, errorMap, mdmsData);
		validateServiceHistory(employee, errorMap, mdmsData);
		//validateEducationalDetails(employee, errorMap, mdmsData);
		//validateDepartmentalTest(employee, errorMap, mdmsData);
	}
	
	/**
	 * Performs checks for maintaining data consistency
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 * @param existingEmp
	 */
	public void validateDataConsistency(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData, Employee existingEmp) {

		validateConsistencyAssignment(existingEmp,employee,errorMap);
		validateConsistencyJurisdiction(existingEmp,employee,errorMap);
		validateConsistencyDepartmentalTest(existingEmp,employee,errorMap);
		validateConsistencyEducationalDetails(existingEmp,employee,errorMap);
		validateConsistencyServiceHistory(existingEmp,employee,errorMap);
		validateConsistencyEmployeeDocument(existingEmp,employee,errorMap);
		validateConsistencyDeactivationDetails(existingEmp,employee,errorMap);
		validateDeactivationDetails(existingEmp,employee,errorMap);
	}

	/**
	 * Checks the following:
	 * 1. Whether the mobile number is valid
	 * 2. Whether the roles are valid
	 * 3. Whether the employee status mentioned is valid.
	 * 4. Whether the employee type mentioned is valid
	 * 5. Whether the date of appointment of the employee is valid.
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 */
	private void validateEmployee(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {

		if(employee.getUser().getMobileNumber().length() != 10)
			errorMap.put(ErrorConstants.HRMS_INVALID_MOB_NO_CODE, ErrorConstants.HRMS_INVALID_MOB_NO_MSG);
		
		if(CollectionUtils.isEmpty(employee.getUser().getRoles()))
			errorMap.put(ErrorConstants.HRMS_MISSING_ROLES_CODE, ErrorConstants.HRMS_INVALID_ROLES_MSG);
		else {
			for(Role role: employee.getUser().getRoles()) {
				if(!mdmsData.get(HRMSConstants.HRMS_MDMS_ROLES_CODE).contains(role.getCode()))
					errorMap.put(ErrorConstants.HRMS_INVALID_ROLE_CODE, ErrorConstants.HRMS_INVALID_ROLE_MSG + role.getCode());
			}
		}
		if(!mdmsData.get(HRMSConstants.HRMS_MDMS_EMP_STATUS_CODE).contains(employee.getEmployeeStatus()))
			errorMap.put(ErrorConstants.HRMS_INVALID_EMP_STATUS_CODE, ErrorConstants.HRMS_INVALID_EMP_STATUS_MSG);
		if(!mdmsData.get(HRMSConstants.HRMS_MDMS_EMP_TYPE_CODE).contains(employee.getEmployeeType()))
			errorMap.put(ErrorConstants.HRMS_INVALID_EMP_TYPE_CODE, ErrorConstants.HRMS_INVALID_EMP_TYPE_CODE);
		if(employee.getDateOfAppointment() > new Date().getTime())
			errorMap.put(ErrorConstants.HRMS_INVALID_DATE_OF_APPOINTMENT_CODE, ErrorConstants.HRMS_INVALID_DATE_OF_APPOINTMENT_MSG);
		if(employee.getDateOfAppointment() < employee.getUser().getDob())
			errorMap.put(ErrorConstants.HRMS_INVALID_DATE_OF_APPOINTMENT_DOB_CODE, ErrorConstants.HRMS_INVALID_DATE_OF_APPOINTMENT_DOB_MSG);
	}
	
	/**
	 * Checks the following:
	 * 1. If there is more than one current assignment.
	 * 2. if period of assignment of any of the assignments overlap with that of others.
	 * 3. if the Department code is valid
	 * 4. If the Designation code is valid
	 * 5. If the assignment dates are valid
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 */
	private void validateAssignments(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		List<Assignment> currentAssignments = employee.getAssignments().stream().filter(assignment -> assignment.getIsCurrentAssignment()).collect(Collectors.toList());
		if(currentAssignments.size() != 1){
			errorMap.put(ErrorConstants.HRMS_INVALID_CURRENT_ASSGN_CODE, ErrorConstants.HRMS_INVALID_CURRENT_ASSGN_MSG);
		}
		employee.getAssignments().sort(new Comparator<Assignment>() {
			@Override
			public int compare(Assignment assignment1, Assignment assignment2) {
				return assignment1.getFromDate().compareTo(assignment2.getFromDate());
			}
		});
		int length = employee.getAssignments().size();
		boolean overlappingCheck =false;
		for(int i=0;i<length-1;i++){
			if(employee.getAssignments().get(i).getToDate() > employee.getAssignments().get(i+1).getFromDate())
				overlappingCheck=true;
		}
		if(overlappingCheck)
			errorMap.put(ErrorConstants.HRMS_OVERLAPPING_ASSGN_CODE, ErrorConstants.HRMS_OVERLAPPING_ASSGN_MSG);

		for(Assignment assignment: employee.getAssignments()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DEPT_CODE).contains(assignment.getDepartment()))
				errorMap.put(ErrorConstants.HRMS_INVALID_DEPT_CODE, ErrorConstants.HRMS_INVALID_DEPT_MSG);
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DESG_CODE).contains(assignment.getDesignation()))
				errorMap.put(ErrorConstants.HRMS_INVALID_DESG_CODE, ErrorConstants.HRMS_INVALID_DESG_MSG);
            if(assignment.getFromDate() > new Date().getTime() || assignment.getToDate() > new Date().getTime()
                    || assignment.getFromDate() > assignment.getToDate())
                errorMap.put(ErrorConstants.HRMS_INVALID_ASSIGNMENT_PERIOD_CODE, ErrorConstants.HRMS_INVALID_ASSIGNMENT_PERIOD_MSG);
            if(assignment.getFromDate() < employee.getUser().getDob() || assignment.getToDate() < employee.getUser().getDob())
                errorMap.put(ErrorConstants.HRMS_INVALID_ASSIGNMENT_DATES_CODE, ErrorConstants.HRMS_INVALID_ASSIGNMENT_DATES_MSG);

        }
		
	}

	/**
	 * Checks the follwing:
	 * 1. If the status of service is valid.
	 * 2. If the service period is valid.
	 * 3. If the service dates is valid.
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 */
	private void validateServiceHistory(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		if(!CollectionUtils.isEmpty(employee.getServiceHistory())){
			for(ServiceHistory history: employee.getServiceHistory()) {
				if(!mdmsData.get(HRMSConstants.HRMS_MDMS_EMP_STATUS_CODE).contains(history.getServiceStatus()))
					errorMap.put(ErrorConstants.HRMS_INVALID_SERVICE_STATUS_CODE, ErrorConstants.HRMS_INVALID_SERVICE_STATUS_MSG+history.getServiceStatus());
				if(history.getServiceFrom() > new Date().getTime() || history.getServiceTo() > new Date().getTime()
						|| history.getServiceFrom() > history.getServiceTo())
					errorMap.put(ErrorConstants.HRMS_INVALID_SERVICE_PERIOD_CODE, ErrorConstants.HRMS_INVALID_SERVICE_PERIOD_MSG);
				if(history.getServiceFrom() < employee.getUser().getDob() || history.getServiceTo() < employee.getUser().getDob())
					errorMap.put(ErrorConstants.HRMS_INVALID_SERVICE_DATES_CODE, ErrorConstants.HRMS_INVALID_SERVICE_DATES_MSG);
			}
		}
	}
	
	/**
	 * Checks the following:
	 * 1. If the qualification is valid.
	 * 2. If the specialization provided is valid.
	 * 3. If the year of passing is valid.
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 */
	private void validateEducationalDetails(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		if(!CollectionUtils.isEmpty(employee.getEducation())){
			for(EducationalQualification education : employee.getEducation()) {
				if(!mdmsData.get(HRMSConstants.HRMS_MDMS_QUALIFICATION_CODE).contains(education.getQualification()))
					errorMap.put(ErrorConstants.HRMS_INVALID_QUALIFICATION_CODE, ErrorConstants.HRMS_INVALID_QUALIFICATION_MSG+education.getQualification());
				if(!mdmsData.get(HRMSConstants.HRMS_MDMS_STREAMS_CODE).contains(education.getStream()))
					errorMap.put(ErrorConstants.HRMS_INVALID_EDUCATIONAL_STREAM_CODE, ErrorConstants.HRMS_INVALID_EDUCATIONAL_STREAM_MSG+education.getStream());
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(employee.getUser().getDob());
				if( education.getYearOfPassing() < cal.get(Calendar.YEAR)){
					errorMap.put(ErrorConstants.HRMS_INVALID_EDUCATIONAL_PASSING_YEAR_CODE, ErrorConstants.HRMS_INVALID_EDUCATIONAL_PASSING_YEAR_MSG);
				}
			}
		}
	}
	
	/**
	 * Checks the follwing:
	 * 1. If the dept test is valid.
	 * 2. If the year of passing is valid.
	 * 
	 * @param employee
	 * @param errorMap
	 * @param mdmsData
	 */
	private void validateDepartmentalTest(Employee employee, Map<String, String> errorMap, Map<String, List<String>> mdmsData) {
		for(DepartmentalTest test: employee.getTests()) {
			if(!mdmsData.get(HRMSConstants.HRMS_MDMS_DEPT_TEST_CODE).contains(test.getTest()))
				errorMap.put(ErrorConstants.HRMS_INVALID_DEPARTMENTAL_TEST_CODE, ErrorConstants.HRMS_INVALID_DEPARTMENTAL_TEST_MSG+test.getTest());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(employee.getUser().getDob());
            if( test.getYearOfPassing() < cal.get(Calendar.YEAR)){
                errorMap.put(ErrorConstants.HRMS_INVALID_DEPARTMENTAL_TEST_PASSING_YEAR_CODE, ErrorConstants.HRMS_INVALID_DEPARTMENTAL_TEST_PASSING_YEAR_MSG);
            }

		}
		
	}

	/**
	 * Validates if the deactivation details are provided every time an employee is deactivated.
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateDeactivationDetails(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getDeactivationDetails())) {
			for (DeactivationDetails deactivationDetails : updatedEmployeeData.getDeactivationDetails()) {
				if (deactivationDetails.getId()==null){
					if(updatedEmployeeData.isActive()){
						errorMap.put(ErrorConstants.HRMS_INVALID_DEACT_REQUEST_CODE, ErrorConstants.HRMS_INVALID_DEACT_REQUEST_MSG);
					}
				}
			}
		}
	}
	
	/**
	 * Validates the employee request for update. Validates the following:
	 * 1. MDMS codes in the request
	 * 2. Performs data consistency checks.
	 * 
	 * @param request
	 */
	public void validateUpdateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		Map<String, List<String>> mdmsData = mdmsService.getMDMSData(request.getRequestInfo(), request.getEmployees().get(0).getTenantId());
		List <String> uuidList = request.getEmployees().stream().map(Employee :: getUuid).collect(Collectors.toList()); 
		EmployeeResponse existingEmployeeResponse = employeeService.search(EmployeeSearchCriteria.builder().uuids(uuidList).build(),request.getRequestInfo());
		List <Employee> existingEmployees = existingEmployeeResponse.getEmployees();
		for(Employee employee: request.getEmployees()){
			if(validateEmployeeForUpdate(employee,errorMap)){
				Employee existingEmp = existingEmployees.stream().filter(existingEmployee -> existingEmployee.getUuid().equals(employee.getUuid())).findFirst().get();
				validateDataConsistency(employee, errorMap, mdmsData, existingEmp);
			}
			validateMdmsData(employee, errorMap, mdmsData);
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {	
			throw new CustomException(errorMap);
		}


	}

	/**
	 * Checks if the ID, UUID and Code are present in the update request
	 * 
	 * @param employee
	 * @param errorMap
	 * @return
	 */
	private boolean validateEmployeeForUpdate(Employee employee, Map<String, String> errorMap) {
		boolean isvalid = true;
		if(employee.getId() == null){
			errorMap.put(ErrorConstants.HRMS_UPDATE_NULL_ID_CODE, ErrorConstants.HRMS_UPDATE_NULL_ID_MSG);
			isvalid=false;
		}
		if(StringUtils.isEmpty(employee.getCode())){
			errorMap.put(ErrorConstants.HRMS_UPDATE_NULL_CODE_CODE, ErrorConstants.HRMS_UPDATE_NULL_CODE_MSG);
			isvalid=false;
		}
		if(StringUtils.isEmpty(employee.getUuid())){
			errorMap.put(ErrorConstants.HRMS_UPDATE_NULL_UUID_CODE, ErrorConstants.HRMS_UPDATE_NULL_UUID_MSG);
			isvalid=false;
		}

		return isvalid;

	}

	/**
	 * Juridictions once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyJurisdiction(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap) {
		boolean check =
				updatedEmployeeData.getJurisdictions().stream()
						.map(jurisdiction -> jurisdiction.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getJurisdictions().stream()
								.map(jurisdiction -> jurisdiction.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put(ErrorConstants.HRMS_UPDATE_JURISDICTION_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_JURISDICTION_INCOSISTENT_MSG);
		}

	}
	
	/**
	 * Assignments once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyAssignment(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap) {
		boolean check =
				updatedEmployeeData.getAssignments().stream()
						.map(assignment -> assignment.getId())
						.collect(Collectors.toList())
						.containsAll(existingEmp.getAssignments().stream()
								.map(assignment -> assignment.getId())
								.collect(Collectors.toList()));
		if(!check){
			errorMap.put(ErrorConstants.HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT_MSG);
		}
	}

	/**
	 * Dept Test details once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyDepartmentalTest(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getTests())){
			boolean check =
					updatedEmployeeData.getTests().stream()
							.map(test -> test.getId())
							.collect(Collectors.toList())
							.containsAll(existingEmp.getTests().stream()
									.map(test -> test.getId())
									.collect(Collectors.toList()));
			if(!check){
				errorMap.put(ErrorConstants.HRMS_UPDATE_TESTS_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_TESTS_INCOSISTENT_MSG);
			}
		}

	}

	/**
	 * Education Details once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyEducationalDetails(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getEducation())){
			boolean check =
					updatedEmployeeData.getEducation().stream()
							.map(educationalQualification -> educationalQualification.getId())
							.collect(Collectors.toList())
							.containsAll(existingEmp.getEducation().stream()
									.map(educationalQualification -> educationalQualification.getId())
									.collect(Collectors.toList()));
			if(!check){
				errorMap.put(ErrorConstants.HRMS_UPDATE_EDUCATION_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_EDUCATION_INCOSISTENT_MSG);
			}
		}
	}

	/**
	 * Service History once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyServiceHistory(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getServiceHistory())){
			boolean check =
					updatedEmployeeData.getServiceHistory().stream()
							.map(serviceHistory -> serviceHistory.getId())
							.collect(Collectors.toList())
							.containsAll(existingEmp.getServiceHistory().stream()
									.map(serviceHistory -> serviceHistory.getId())
									.collect(Collectors.toList()));
			if(!check){
				errorMap.put(ErrorConstants.HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT_MSG);
			}

		}

	}

	/**
	 * Documents once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyEmployeeDocument(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getDocuments())){
			boolean check =
					updatedEmployeeData.getDocuments().stream()
							.map(employeeDocument -> employeeDocument.getId())
							.collect(Collectors.toList())
							.containsAll(existingEmp.getDocuments().stream()
									.map(employeeDocument -> employeeDocument.getId())
									.collect(Collectors.toList()));
			if (!check) {
				errorMap.put(ErrorConstants.HRMS_UPDATE_DOCUMENT_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_DOCUMENT_INCOSISTENT_MSG);
			}
		}

	}

	/**
	 * Deactivation Details once created in the system cannot be deleted, they can however be changed. Validates that condition
	 * 
	 * @param existingEmp
	 * @param updatedEmployeeData
	 * @param errorMap
	 */
	private void validateConsistencyDeactivationDetails(Employee existingEmp, Employee updatedEmployeeData, Map<String, String> errorMap){
		if(!CollectionUtils.isEmpty(updatedEmployeeData.getDeactivationDetails())){
			boolean check =
					updatedEmployeeData.getDeactivationDetails().stream()
							.map(deactivationDetails -> deactivationDetails.getId())
							.collect(Collectors.toList())
							.containsAll(existingEmp.getDocuments().stream()
									.map(employeeDocument -> employeeDocument.getId())
									.collect(Collectors.toList()));
			if (!check) {
				errorMap.put(ErrorConstants.HRMS_UPDATE_DEACT_DETAILS_INCOSISTENT_CODE, ErrorConstants.HRMS_UPDATE_DEACT_DETAILS_INCOSISTENT_MSG);
			}
		}

	}

}
