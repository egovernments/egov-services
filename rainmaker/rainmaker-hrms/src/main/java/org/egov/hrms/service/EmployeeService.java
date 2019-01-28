/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.hrms.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.config.PropertiesManager;
import org.egov.hrms.model.AuditDetails;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.enums.UserType;
import org.egov.hrms.producer.HRMSProducer;
import org.egov.hrms.repository.EmployeeRepository;
import org.egov.hrms.utils.HRMSUtils;
import org.egov.hrms.utils.ResponseInfoFactory;
import org.egov.hrms.web.contract.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
public class EmployeeService {


	@Autowired
	private UserService userService;

	@Autowired
	private IdGenService idGenService;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private HRMSProducer hrmsProducer;
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private HRMSUtils hrmsUtils;
	
	@Autowired
	private NotificationService notificationService;

	public EmployeeResponse create(EmployeeRequest employeeRequest) {
		log.info("Service: Create Employee");
		log.info(employeeRequest.toString());
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		employeeRequest.getEmployees().stream().forEach(employee -> {
			createUser(employee, requestInfo);
			enrichCreateRequest(employee, requestInfo);
		});
		hrmsProducer.push(propertiesManager.getSaveEmployeeTopic(), employeeRequest);
		notificationService.sendNotification(employeeRequest);
		return generateResponse(employeeRequest);
	}
	
	public EmployeeResponse search(EmployeeSearchCriteria criteria, RequestInfo requestInfo) {
		List<Employee> employees = repository.fetchEmployees(criteria, requestInfo);
		List<String> uuids = employees.stream().map(Employee :: getUuid).collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(uuids)){
			UserResponse userResponse = userService.getUser(requestInfo, uuids);
			if(!CollectionUtils.isEmpty(userResponse.getUser())) {
				Map<String, User> mapOfUsers = userResponse.getUser().parallelStream()
						.collect(Collectors.toMap(User :: getUuid, Function.identity()));
				employees.parallelStream().forEach(employee -> employee.setUser(mapOfUsers.get(employee.getUuid())));
			}else{
				log.info("Users couldn't be fetched!");
			}
		}
		return EmployeeResponse.builder().responseInfo(factory.createResponseInfoFromRequestInfo(requestInfo, true))
				.employees(employees).build();
	}
	
	

	private void createUser(Employee employee, RequestInfo requestInfo) {
		List<String> pwdParams = new ArrayList<>();
		pwdParams.add(employee.getCode()); pwdParams.add(employee.getUser().getMobileNumber());
		pwdParams.add(employee.getTenantId()); pwdParams.add(employee.getUser().getName()); pwdParams.add(employee.getUser().getDob().toString());
		employee.getUser().setPassword(hrmsUtils.generatePassword(pwdParams));
		employee.getUser().setUserName(employee.getCode());
		UserRequest request = UserRequest.builder().requestInfo(requestInfo).user(employee.getUser()).build();
		try {
			UserResponse response = userService.createUser(request);
			User user = response.getUser().get(0);
			employee.setId(user.getId());
			employee.setUuid(user.getUuid());
			employee.setUser(user);
		}catch(Exception e) {
			log.error("Exception while creating user: ",e);
			throw new CustomException("HRMS_USER_CREATION_FAILED", "User creation failed due to error: "+e.getMessage());
		}

	}

	private void enrichCreateRequest(Employee employee, RequestInfo requestInfo) {

		Instant instant = Instant.now();
		AuditDetails auditDetails = AuditDetails.builder()
				.createdBy(requestInfo.getUserInfo().getUserName())
				.createdDate(instant.getEpochSecond())
				.build();


		employee.setCode(idGenService.getId());

		employee.getJurisdictions().stream().forEach(jurisdiction -> {
			jurisdiction.setId(UUID.randomUUID().toString());
			jurisdiction.setAuditDetails(auditDetails);
		});
		employee.getAssignments().stream().forEach(assignment -> {
			assignment.setId(UUID.randomUUID().toString());
			assignment.setAuditDetails(auditDetails);
		});
		if(!CollectionUtils.isEmpty(employee.getServiceHistory())) {
			employee.getServiceHistory().stream().forEach(serviceHistory -> {
				serviceHistory.setId(UUID.randomUUID().toString());
				serviceHistory.setAuditDetails(auditDetails);
			});
		}
		if(!CollectionUtils.isEmpty(employee.getEducation())) {
			employee.getEducation().stream().forEach(educationalQualification -> {
				educationalQualification.setId(UUID.randomUUID().toString());
				educationalQualification.setAuditDetails(auditDetails);
			});
		}
		if(!CollectionUtils.isEmpty(employee.getTests())) {
			employee.getTests().stream().forEach(departmentalTest -> {
				departmentalTest.setId(UUID.randomUUID().toString());
				departmentalTest.setAuditDetails(auditDetails);
			});
		}
		if(!CollectionUtils.isEmpty(employee.getDocuments())) {
			employee.getDocuments().stream().forEach(document -> {
				document.setId(UUID.randomUUID().toString());
				document.setAuditDetails(auditDetails);
			});
		}

		employee.setAuditDetails(auditDetails);
		employee.setActive(true);
		employee.getUser().setType(UserType.EMPLOYEE.toString());

	}

	public EmployeeResponse update(EmployeeRequest employeeRequest) {
		log.info("Service: Update Employee");
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		List <String> uuidList= new ArrayList<>();
		for(Employee employee: employeeRequest.getEmployees()) {
			uuidList.add(employee.getUuid());
		}
		EmployeeResponse existingEmployeeResponse = search(EmployeeSearchCriteria.builder().uuids(uuidList).build(),requestInfo);
		List <Employee> existingEmployees = existingEmployeeResponse.getEmployees();
		employeeRequest.getEmployees().stream().forEach(employee -> {
			enrichUpdateRequest(employee, requestInfo, existingEmployees);
			//updateUser(employee, requestInfo);
		});
		hrmsProducer.push(propertiesManager.getUpdateEmployeeTopic(), employeeRequest);

		return generateResponse(employeeRequest);
	}

	private void enrichUpdateRequest(Employee employee, RequestInfo requestInfo, List<Employee> existingEmployeesData) {
		Instant instant = Instant.now();
		AuditDetails auditDetails = AuditDetails.builder()
				.createdBy(requestInfo.getUserInfo().getUserName())
				.createdDate(instant.getEpochSecond())
				.build();
		Employee existingEmpData = existingEmployeesData.stream().filter(existingEmployee -> existingEmployee.getUuid()==employee.getUuid()).findFirst().get();

		employee.getJurisdictions().stream().forEach(jurisdiction -> {

			if(jurisdiction.getId()==null) {
				jurisdiction.setId(UUID.randomUUID().toString());
				jurisdiction.setAuditDetails(auditDetails);
			}else{
				if(!existingEmpData.getJurisdictions().stream()
						.filter(jurisdictionData ->jurisdictionData.getId()==jurisdiction.getId() )
						.findFirst()
						.equals(jurisdiction)){
					jurisdiction.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					jurisdiction.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getAssignments().stream().forEach(assignment -> {
			if(assignment.getId()==null) {
				assignment.setId(UUID.randomUUID().toString());
				assignment.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(assignmentData -> assignmentData.getId()==assignment.getId())
						.findFirst()
						.equals(assignment)){
					assignment.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					assignment.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getServiceHistory().stream().forEach(serviceHistory -> {
			if(serviceHistory.getId()==null) {
				serviceHistory.setId(UUID.randomUUID().toString());
				serviceHistory.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(serviceHistoryData -> serviceHistoryData.getId()==serviceHistory.getId())
						.findFirst()
						.equals(serviceHistory)){
					serviceHistory.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					serviceHistory.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getEducation().stream().forEach(educationalQualification -> {
			if(educationalQualification.getId()==null) {
				educationalQualification.setId(UUID.randomUUID().toString());
				educationalQualification.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(educationalQualificationData -> educationalQualificationData.getId()==educationalQualification.getId())
						.findFirst()
						.equals(educationalQualification)){
					educationalQualification.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					educationalQualification.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getTests().stream().forEach(departmentalTest -> {
			if(departmentalTest.getId()==null) {
				departmentalTest.setId(UUID.randomUUID().toString());
				departmentalTest.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(departmentalTestData -> departmentalTestData.getId()==departmentalTest.getId())
						.findFirst()
						.equals(departmentalTest)){
					departmentalTest.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					departmentalTest.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getDocuments().stream().forEach(document -> {
			if(document.getId()==null) {
				document.setId(UUID.randomUUID().toString());
				document.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(documentData -> documentData.getId()==document.getId())
						.findFirst()
						.equals(document)){
					document.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					document.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
		employee.getDeactivationDetails().stream().forEach(deactivationDetails -> {
			if(deactivationDetails.getId()==null) {
				deactivationDetails.setId(UUID.randomUUID().toString());
				deactivationDetails.setAuditDetails(auditDetails);
			}else {
				if(!existingEmpData.getAssignments().stream()
						.filter(deactivationDetailsData -> deactivationDetailsData.getId()==deactivationDetails.getId())
						.findFirst()
						.equals(deactivationDetails)){
					deactivationDetails.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUserName());
					deactivationDetails.getAuditDetails().setLastModifiedDate(instant.getEpochSecond());
				}
			}
		});
	}

	private EmployeeResponse generateResponse(EmployeeRequest employeeRequest) {
		return EmployeeResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(employeeRequest.getRequestInfo(), true))
				.employees(employeeRequest.getEmployees()).build();
	}

}