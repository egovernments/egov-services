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

package org.egov.eis.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.eis.broker.EmployeeProducer;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.DepartmentalTestRepository;
import org.egov.eis.repository.EducationalQualificationRepository;
import org.egov.eis.repository.EmployeeJurisdictionRepository;
import org.egov.eis.repository.EmployeeLanguageRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.HODDepartmentRepository;
import org.egov.eis.repository.ProbationRepository;
import org.egov.eis.repository.RegularisationRepository;
import org.egov.eis.repository.ServiceHistoryRepository;
import org.egov.eis.repository.TechnicalQualificationRepository;
import org.egov.eis.service.helper.EmployeeUserMapper;
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private HODDepartmentRepository hodDepartmentRepository;

	@Autowired
	private ServiceHistoryRepository serviceHistoryRepository;

	@Autowired
	private ProbationRepository probationRepository;

	@Autowired
	private RegularisationRepository regularisationRepository;

	@Autowired
	private TechnicalQualificationRepository technicalQualificationRepository;

	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;

	@Autowired
	private DepartmentalTestRepository departmentalTestRepository;

	@Autowired
	private EmployeeJurisdictionRepository employeeJurisdictionRepository;

	@Autowired
	private EmployeeLanguageRepository employeeLanguageRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeUserMapper employeeUserMapper;
	
	@Autowired
	EmployeeProducer employeeProducer;
	
	@Autowired
	SmartValidator validator;

	@Value("${kafka.topics.employee.savedb.name}")
	String employeeSaveTopic;
	
	@Value("${kafka.topics.employee.savedb.key}")
	String employeeSaveKey;

	public List<EmployeeInfo> getEmployees(EmployeeGetRequest employeeGetRequest, RequestInfo requestInfo) {
		List<EmployeeInfo> employeeInfoList = employeeRepository.findForCriteria(employeeGetRequest);

		List<Long> ids = employeeInfoList.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());

		List<User> usersList = userService.getUsers(ids, employeeGetRequest.getTenantId(), requestInfo);
		employeeUserMapper.mapUsersWithEmployees(employeeInfoList, usersList);

		return employeeInfoList;
	}

	public void createEmployee(EmployeeRequest employeeRequest) {
		UserRequest userRequest = getUserRequest(employeeRequest);
//		Long id = userService.createUser(userRequest);
//		String code = getEmployeeCode(id);
		Employee employee = employeeRequest.getEmployee();
//		employee.setId(id);
//		employee.setCode(code);

		String employeeRequestJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			employeeRequestJson = mapper.writeValueAsString(employeeRequest);
			LOGGER.info("employeeJson::" + employeeRequestJson);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting Employee to JSON", e);
			e.printStackTrace();
		}
		try {
			employeeProducer.sendMessage(employeeSaveTopic, employeeSaveKey, employeeRequestJson);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getEmployeeCode(Long id) {
		return "EMP" + id;
	}

	private UserRequest getUserRequest(EmployeeRequest employeeRequest) {
		UserRequest userRequest = new UserRequest();
		userRequest.setRequestInfo(employeeRequest.getRequestInfo());
		User user = employeeRequest.getEmployee().getUser();
		user.setTenantId(employeeRequest.getEmployee().getTenantId());
		userRequest.setUser(user);

		return userRequest;
	}

	public void saveEmployee(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();

		employeeRepository.save(employee);
		employeeJurisdictionRepository.save(employee);
		employeeLanguageRepository.save(employee);
		for (int i = 0; i < employee.getAssignments().size(); i++) {
			employee.getAssignments().get(i).setId(new Date().getTime() + i);
		};
		assignmentRepository.save(employee);
		employee.getAssignments().forEach((assignment) -> {
			hodDepartmentRepository.save(assignment);
		});
		serviceHistoryRepository.save(employee);
		probationRepository.save(employee);
		regularisationRepository.save(employee);
		technicalQualificationRepository.save(employee);
		educationalQualificationRepository.save(employee);
		departmentalTestRepository.save(employee);
	}

	public void validate(EmployeeRequest employeeRequest, String method, BindingResult bindingResult) {
		try {
			switch (method) {
			case "create":
				validator.validate(employeeRequest.getEmployee(), bindingResult);
				break;
			default:
				validator.validate(employeeRequest.getRequestInfo(), bindingResult);
			}
		} catch (IllegalArgumentException e) {
			bindingResult.addError(new ObjectError("Missing data", e.getMessage()));
		}
	}
}