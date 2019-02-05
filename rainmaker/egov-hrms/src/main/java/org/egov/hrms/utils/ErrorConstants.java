package org.egov.hrms.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorConstants {
	
	public static final String HRMS_USER_EXIST_MOB_CODE = "HRMS_USER_EXIST_MOB";
	public static final String HRMS_USER_EXIST_MOB_MSG = "User already present for Mobile Number: ";
	
	public static final String HRMS_USER_EXIST_USERNAME_CODE = "HRMS_USER_EXIST_USERNAME";
	public static final String HRMS_USER_EXIST_USERNAME_MSG = "User already present for UserName: ";
	
	public static final String HRMS_INVALID_MOB_NO_CODE = "HRMS_INVALID_MOB_NO";
	public static final String HRMS_INVALID_MOB_NO_MSG = "Mobile number of the employee is invalid";
	
	public static final String HRMS_MISSING_ROLES_CODE = "HRMS_MISSING_ROLES";
	public static final String HRMS_INVALID_ROLES_MSG = "An employee must have atleast one role!";
	
	public static final String HRMS_INVALID_ROLE_CODE = "HRMS_INVALID_ROLE";
	public static final String HRMS_INVALID_ROLE_MSG = "Employee contains an invalid role: ";
	
	public static final String HRMS_INVALID_EMP_STATUS_CODE = "HRMS_INVALID_EMP_STATUS_ROLE";
	public static final String HRMS_INVALID_EMP_STATUS_MSG = "Employee status is invalid!";
	
	public static final String HRMS_INVALID_EMP_TYPE_CODE = "HRMS_INVALID_EMP_TYPE";
	public static final String HRMS_INVALID_EMP_TYPE_MSG = "Employee type is invalid!";
	
	public static final String HRMS_INVALID_DATE_OF_APPOINTMENT_CODE = "HRMS_INVALID_DATE_OF_APPOINTMENT";
	public static final String HRMS_INVALID_DATE_OF_APPOINTMENT_MSG = "Employee date of appointment is invalid!";
	
	public static final String HRMS_INVALID_DATE_OF_APPOINTMENT_DOB_CODE = "HRMS_INVALID_DATE_OF_APPOINTMENT_DOB";
	public static final String HRMS_INVALID_DATE_OF_APPOINTMENT_DOB_MSG = "Employee date of appointment is before DOB!";
	
	public static final String HRMS_INVALID_CURRENT_ASSGN_CODE = "HRMS_INVALID_CURRENT_ASSGN";
	public static final String HRMS_INVALID_CURRENT_ASSGN_MSG = "There should be exactly 1 current assignement!";
	
	public static final String HRMS_OVERLAPPING_ASSGN_CODE = "HRMS_OVERLAPPING_ASSGN";
	public static final String HRMS_OVERLAPPING_ASSGN_MSG = "Period of assignements of employee should not overlap!";
	
	public static final String HRMS_INVALID_DEPT_CODE = "HRMS_INVALID_DEPT";
	public static final String HRMS_INVALID_DEPT_MSG = "Department of the employee is invalid!";
	
	public static final String HRMS_INVALID_DESG_CODE = "HRMS_INVALID_DESG";
	public static final String HRMS_INVALID_DESG_MSG = "Designation of the employee is invalid!";
	
	public static final String HRMS_INVALID_ASSIGNMENT_PERIOD_CODE = "HRMS_INVALID_ASSIGNMENT_PERIOD";
	public static final String HRMS_INVALID_ASSIGNMENT_PERIOD_MSG = "Period of assignemnt (fromDate to toDate) is invalid!";
	
	public static final String HRMS_INVALID_ASSIGNMENT_DATES_CODE = "HRMS_INVALID_ASSIGNMENT_DATES";
	public static final String HRMS_INVALID_ASSIGNMENT_DATES_MSG = "Period of assignemnt (fromDate to toDate) is before DOB!";
	
	public static final String HRMS_INVALID_SERVICE_STATUS_CODE = "HRMS_INVALID_SERVICE_STATUS";
	public static final String HRMS_INVALID_SERVICE_STATUS_MSG = "Service status of the employee is invalid!: ";
	
	public static final String HRMS_INVALID_SERVICE_PERIOD_CODE = "HRMS_INVALID_SERVICE_PERIOD";
	public static final String HRMS_INVALID_SERVICE_PERIOD_MSG = "Service period (serviceFrom to serviceTo) of the employee is invalid!";
	
	public static final String HRMS_INVALID_SERVICE_DATES_CODE = "HRMS_INVALID_SERVICE_DATES";
	public static final String HRMS_INVALID_SERVICE_DATES_MSG = "Service period (serviceFrom to serviceTo) of the employee is before DOB!";
	
	public static final String HRMS_INVALID_QUALIFICATION_CODE = "HRMS_INVALID_QUALIFICATION";
	public static final String HRMS_INVALID_QUALIFICATION_MSG = "Qualification of the employee is invalid!: ";
	
	public static final String HRMS_INVALID_EDUCATIONAL_STREAM_CODE = "HRMS_INVALID_EDUCATIONAL_STREAM";
	public static final String HRMS_INVALID_EDUCATIONAL_STREAM_MSG = "Education stream of the employee is invalid!: ";
	
	public static final String HRMS_INVALID_EDUCATIONAL_PASSING_YEAR_CODE = "HRMS_INVALID_EDUCATIONAL_PASSING_YEAR";
	public static final String HRMS_INVALID_EDUCATIONAL_PASSING_YEAR_MSG = "Education year of passing of the employee is before DOB!";
	
	public static final String HRMS_INVALID_DEPARTMENTAL_TEST_CODE = "HRMS_INVALID_DEPARTMENTAL_TEST";
	public static final String HRMS_INVALID_DEPARTMENTAL_TEST_MSG = "Departmental test of the employee is invalid!: ";
	
	public static final String HRMS_INVALID_DEPARTMENTAL_TEST_PASSING_YEAR_CODE = "HRMS_INVALID_DEPARTMENTAL_TEST_PASSING_YEAR";
	public static final String HRMS_INVALID_DEPARTMENTAL_TEST_PASSING_YEAR_MSG = "Departmental test passing year of the employee is before DOB!";
	
	public static final String HRMS_INVALID_DEACT_REQUEST_CODE = "HRMS_INVALID_DEACT_REQUEST";
	public static final String HRMS_INVALID_DEACT_REQUEST_MSG = "Employee is active should be set to false while deactivation!";
	
	public static final String HRMS_UPDATE_JURISDICTION_INCOSISTENT_CODE = "HRMS_UPDATE_JURISDICTION_INCOSISTENT";
	public static final String HRMS_UPDATE_JURISDICTION_INCOSISTENT_MSG = "Jurisdiction data in update request should contain all previous jurisdiction data";
	
	public static final String HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT_CODE = "HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT";
	public static final String HRMS_UPDATE_ASSIGNEMENT_INCOSISTENT_MSG = "Assignment data in update request should contain all previous assginment data ";
	
	public static final String HRMS_UPDATE_TESTS_INCOSISTENT_CODE = "HRMS_UPDATE_TESTS_INCOSISTENT";
	public static final String HRMS_UPDATE_TESTS_INCOSISTENT_MSG = "Tests data in update request should contain all previous Tests data";
	
	public static final String HRMS_UPDATE_EDUCATION_INCOSISTENT_CODE = "HRMS_UPDATE_EDUCATION_INCOSISTENT";
	public static final String HRMS_UPDATE_EDUCATION_INCOSISTENT_MSG = "Education data in update request should contain all previous education data";
	
	public static final String HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT_CODE = "HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT";
	public static final String HRMS_UPDATE_SERVICE_HISTORY_INCOSISTENT_MSG = "Service History data in update request should contain all previous service history data";
	
	public static final String HRMS_UPDATE_DOCUMENT_INCOSISTENT_CODE = "HRMS_UPDATE_DOCUMENT_INCOSISTENT";
	public static final String HRMS_UPDATE_DOCUMENT_INCOSISTENT_MSG = "Employee Document data in update request should contain all previous employee document data";
	
	public static final String HRMS_UPDATE_DEACT_DETAILS_INCOSISTENT_CODE = "HRMS_UPDATE_DEACT_DETAILS_INCOSISTENT";
	public static final String HRMS_UPDATE_DEACT_DETAILS_INCOSISTENT_MSG = "Employee Deactivation details data in update request should contain all previous employee deactivation data";
	
	public static final String HRMS_UPDATE_NULL_ID_CODE = "HRMS_UPDATE_NULL_ID";
	public static final String HRMS_UPDATE_NULL_ID_MSG = "Employee ID in update request should not be Null!";
	
	public static final String HRMS_UPDATE_NULL_CODE_CODE = "HRMS_UPDATE_NULL_CODE";
	public static final String HRMS_UPDATE_NULL_CODE_MSG = "Employee Code in update request should not be Null!";
	
	public static final String HRMS_UPDATE_NULL_UUID_CODE = "HRMS_UPDATE_NULL_UUID";
	public static final String HRMS_UPDATE_NULL_UUID_MSG = "Employee UUID in update request should not be Null!";
	
	public static final String HRMS_USER_CREATION_FAILED_CODE = "HRMS_USER_CREATION_FAILED";
	public static final String HRMS_USER_CREATION_FAILED_MSG = "User creation failed at the user service";
	
	public static final String HRMS_USER_UPDATION_FAILED_CODE = "HRMS_USER_UPDATION_FAILED";
	public static final String HRMS_USER_UPDATION_FAILED_MSG = "User update failed at the user service";
}
