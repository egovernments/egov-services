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
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.User;
import org.egov.hrms.producer.HRMSProducer;
import org.egov.hrms.utils.ResponseInfoFactory;
import org.egov.hrms.web.contract.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Service
public class EmployeeService {

	@Value("${kafka.topics.save.service}")
	private String saveTopic;

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

	public EmployeeResponse create(EmployeeRequest employeeRequest) {
		log.info("Service: Create Employee");
		log.info(employeeRequest.toString());
		RequestInfo requestInfo = employeeRequest.getRequestInfo();

		employeeRequest.getEmployees().stream().forEach(employee -> {
			createUser(employee, requestInfo);
			enrichCreateRequest(employee, requestInfo);

		});
		hrmsProducer.push(saveTopic, employeeRequest);

		return generateResponse(employeeRequest);
	}

	private void createUser(Employee employee, RequestInfo requestInfo) {

		UserRequest request = UserRequest.builder().requestInfo(requestInfo).user(employee.getUser()).build();
		UserResponse response = userService.createUser(request);
		User user = response.getUser().get(0);
		employee.setId(user.getId());
		employee.setUuid(user.getUuid());
		employee.setUser(user);

	}

	private void enrichCreateRequest(Employee employee, RequestInfo requestInfo) {
		requestInfo.setTs(null);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

		employee.getJurisdictions().stream().forEach(jurisdiction -> {
			jurisdiction.setId(UUID.randomUUID().toString());
		});
		employee.getAssignments().stream().forEach(assignment -> {
			assignment.setId(UUID.randomUUID().toString());
		});
		employee.getServiceHistory().stream().forEach(serviceHistory -> {
			serviceHistory.setId(UUID.randomUUID().toString());
		});
		employee.getEducation().stream().forEach(educationalQualification -> {
			educationalQualification.setId(UUID.randomUUID().toString());
		});
		employee.getTests().stream().forEach(departmentalTest -> {
			departmentalTest.setId(UUID.randomUUID().toString());
		});
		employee.getDocuments().stream().forEach(document -> {
			document.setId(UUID.randomUUID().toString());
		});

		employee.getAuditDetails().setCreatedBy(requestInfo.getUserInfo().getUserName());
		Instant instant = Instant.now();
		employee.getAuditDetails().setCreatedDate(instant.getEpochSecond());

	}

	public EmployeeResponse update(EmployeeRequest employeeRequest) {
		log.info("Service: Update Employee");
		RequestInfo requestInfo = employeeRequest.getRequestInfo();

		employeeRequest.getEmployees().stream().forEach(employee -> {
			enrichUpdateRequest(employee, requestInfo);
			updateUser(employee, requestInfo);

			// pushToTopic

		});
		return generateResponse(employeeRequest);
	}

	private void updateUser(Employee employee, RequestInfo requestInfo) {
		UserRequest request = UserRequest.builder().requestInfo(requestInfo).user(employee.getUser()).build();
		UserResponse response = userService.createUser(request);
		User user = response.getUser().get(0);
		employee.setUser(user);

	}

	private void enrichUpdateRequest(Employee employee, RequestInfo requestInfo) {
	}

	private EmployeeResponse generateResponse(EmployeeRequest employeeRequest) {
		return EmployeeResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(employeeRequest.getRequestInfo(), true))
				.employees(employeeRequest.getEmployees()).build();
	}

}