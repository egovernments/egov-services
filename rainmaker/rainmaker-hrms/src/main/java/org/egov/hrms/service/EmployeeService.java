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
import org.apache.commons.lang.ArrayUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.config.ApplicationProperties;
import org.egov.hrms.config.PropertiesManager;
import org.egov.hrms.model.*;
import org.egov.hrms.web.contract.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Slf4j
@Service
public class EmployeeService {


	@Autowired
	private UserService userService;


	@Autowired
	private IdGenService idGenService;


	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ApplicationProperties applicationProperties;



	public Employee createAsync(EmployeeRequest employeeRequest)		{
		createUser(employeeRequest);
		enrichCreateRequest(employeeRequest);
//pushToTopic
		return employeeRequest.getEmployee();
	}

	private void createUser(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();

		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
		UserResponse userResponse = userService.createUser(userRequest);
		User user = userResponse.getUser().get(0);
		employeeHelper.populateDefaultDataForCreate(employeeRequest);

		employee.setId(user.getId());
		employee.setUuid(user.getUuid());
		employee.setUser(user);

	}

	private void enrichCreateRequest(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		requestInfo.setTs(null);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

		employee.getJurisdictions().forEach(jurisdiction -> {
			jurisdiction.setId(UUID.randomUUID().toString());
		});
		employee.getAssignments().forEach(assignment -> {
			assignment.setId(UUID.randomUUID().toString());
		});
		employee.getServiceHistory().forEach(serviceHistory -> {
			serviceHistory.setId(UUID.randomUUID().toString());
		});
		employee.getEducation().forEach(educationalQualification -> {
			educationalQualification.setId(UUID.randomUUID().toString());
		});
		employee.getTest().forEach(departmentalTest -> {
			departmentalTest.setId(UUID.randomUUID().toString());
		});
		employee.setCreatedBy(requestInfo.getUserInfo().getUserName());
		Instant instant = Instant.now();
		employee.setCreatedDate(instant.getEpochSecond());


//		create(employeeRequest);

	}


	public Employee updateAsync(EmployeeRequest employeeRequest)  {

		enrichUpdateRequest(employeeRequest)

		Employee employee = employeeRequest.getEmployee();

//		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
//
//		UserResponse userResponse = userService.updateUser(userRequest.getUser().getId(), userRequest);
//		User user = userResponse.getUser().get(0);
//		employee.setUser(user);
//
//		employeeHelper.populateDefaultDataForUpdate(employeeRequest);
//
//		AssignmentGetRequest assignmentGetRequest = AssignmentGetRequest.builder().tenantId(employee.getTenantId())
//				.build();
//		List<Assignment> assignments = assignmentService.getAssignments(employee.getId(), assignmentGetRequest);
//
//		List<Long> positionfromDB = assignments.stream().map(assignment -> assignment.getPosition())
//				.collect(Collectors.toList());
//		List<Long> positionFromRequest = employeeRequest.getEmployee().getAssignments().stream()
//				.map(assignment -> assignment.getPosition()).collect(Collectors.toList());
//		boolean isPositionModified = !(ArrayUtils.isEquals(positionfromDB, positionFromRequest));
//
//		List<Date> fromDateFromDB = assignments.stream().map(assignment -> assignment.getFromDate())
//				.collect(Collectors.toList());
//		List<Date> fromDateFromRequest = employeeRequest.getEmployee().getAssignments().stream()
//				.map(assignment -> assignment.getFromDate()).collect(Collectors.toList());
//		boolean isFromDateModified = !(ArrayUtils.isEquals(fromDateFromDB, fromDateFromRequest));
//
//		List<Date> toDateFromDB = assignments.stream().map(assignment -> assignment.getToDate())
//				.collect(Collectors.toList());
//		List<Date> toDateFromRequest = employeeRequest.getEmployee().getAssignments().stream()
//				.map(assignment -> assignment.getToDate()).collect(Collectors.toList());
//		boolean isToDateModified = !(ArrayUtils.isEquals(toDateFromDB, toDateFromRequest));
//
//		boolean isAssignmentDeleted = assignments.size() != employeeRequest.getEmployee().getAssignments().size();
//
//		if (isPositionModified || isFromDateModified || isToDateModified || isAssignmentDeleted) {
//			update(employeeRequest);
//		}
//
//		update(employeeRequest);
		return employee;
	}

	private void enrichUpdateRequest(EmployeeRequest employeeRequest) {
	}



}