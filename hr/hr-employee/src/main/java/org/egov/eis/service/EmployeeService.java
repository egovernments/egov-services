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

import java.util.List;
import java.util.stream.Collectors;

import org.egov.eis.broker.EmployeeProducer;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
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
import org.egov.eis.service.helper.EmployeeHelper;
import org.egov.eis.service.helper.EmployeeUserMapper;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.UserResponse;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.ErrorResponse;
import org.egov.eis.web.errorhandler.UserErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private AssignmentService assignmentService;

	@Autowired
	private ServiceHistoryService serviceHistoryService;

	@Autowired
	private ProbationService probationService;

	@Autowired
	private RegularisationService regularisationService;

	@Autowired
	private TechnicalQualificationService technicalQualificationService;

	@Autowired
	private EducationalQualificationService educationalQualificationService;

	@Autowired
	private DepartmentalTestService departmentalTestService;

	@Autowired
	private EmployeeJurisdictionService employeeJurisdictionService;

	@Autowired
	private EmployeeLanguageService employeeLanguageService;
 
	@Autowired
	private EmployeeDocumentsService employeeDocumentsService;
	
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
	private EmployeeHelper employeeHelper;

	@Autowired
	private EmployeeUserMapper employeeUserMapper;

	@Autowired
	private EmployeeProducer employeeProducer;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	PropertiesManager propertiesManager;	

	public List<EmployeeInfo> getEmployees(EmployeeCriteria employeeCriteria, RequestInfo requestInfo) {
		List<EmployeeInfo> employeeInfoList = employeeRepository.findForCriteria(employeeCriteria);

		List<Long> ids = employeeInfoList.stream().map(employeeInfo -> employeeInfo.getId())
				.collect(Collectors.toList());

		List<User> usersList = userService.getUsers(ids, employeeCriteria.getTenantId(), requestInfo);
		LOGGER.debug("userService: " + usersList);
		employeeUserMapper.mapUsersWithEmployees(employeeInfoList, usersList);

		if (!ids.isEmpty()) {
			List<EmployeeDocument> employeeDocuments = employeeRepository.getDocumentsForListOfEmployeeIds(ids);
			employeeHelper.mapDocumentsWithEmployees(employeeInfoList, employeeDocuments);
		}

		return employeeInfoList;
	}
	
	public Employee getEmployee(Long employeeId, String tenantId, RequestInfo requestInfo) {
		Employee employee = employeeRepository.findById(employeeId, tenantId);
		employee.setLanguagesKnown(employeeLanguageRepository.findByEmployeeId(employeeId, tenantId));
		employee.setAssignments(assignmentRepository.findByEmployeeId(employeeId, tenantId));
		employee.setServiceHistory(serviceHistoryRepository.findByEmployeeId(employeeId, tenantId));
		employee.setProbation(probationRepository.findByEmployeeId(employeeId, tenantId));
		employee.setJurisdictions(employeeJurisdictionRepository.findByEmployeeId(employeeId, tenantId));
		employee.setRegularisation(regularisationRepository.findByEmployeeId(employeeId, tenantId));
		employee.setTechnical(technicalQualificationRepository.findByEmployeeId(employeeId, tenantId));
		employee.setEducation(educationalQualificationRepository.findByEmployeeId(employeeId, tenantId));
		employee.setTest(departmentalTestRepository.findByEmployeeId(employeeId, tenantId));
		employeeDocumentsService.populateDocumentsInRespectiveObjects(employee);
		
		return employee;
	}


	public ResponseEntity<?> createAsync(EmployeeRequest employeeRequest) {
		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);

		ResponseEntity<?> responseEntity = null;

		// FIXME : User service is expecting & sending dates in multiple
		// formats. Fix a common standard
		try {
			responseEntity = userService.createUser(userRequest);
		
		} catch (Exception e) {
			LOGGER.debug("Error occurred while creating user", e);
			return errorHandler.getResponseEntityForUnknownUserDBUpdationError(employeeRequest.getRequestInfo());
		}

		try {
			if (responseEntity.getBody().getClass().equals(UserErrorResponse.class)
					|| responseEntity.getBody().getClass().equals(ErrorResponse.class)) {
				return responseEntity;
			}
		} catch (Exception e) {
			LOGGER.debug("Error occurred while creating user", e);
			return errorHandler.getResponseEntityForUnknownUserDBUpdationError(employeeRequest.getRequestInfo());
		}

		UserResponse userResponse = (UserResponse) responseEntity.getBody();
		User user = userResponse.getUser().get(0);

		Employee employee = employeeRequest.getEmployee();
		employee.setId(user.getId());
		employee.setUser(user);

		try {
			employeeHelper.populateDefaultDataForCreate(employeeRequest);
		} catch (Exception e) {
			LOGGER.debug("Error occurred while populating data in objects", e);
			return errorHandler.getResponseEntityForUnexpectedErrors(employeeRequest.getRequestInfo());
		}

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
			employeeProducer.sendMessage(propertiesManager.getSaveEmployeeTopic(),
					propertiesManager.getEmployeeSaveKey(), employeeRequestJson);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return employeeHelper.getSuccessResponseForCreate(employee, employeeRequest.getRequestInfo());
	}

	public void create(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		employeeRepository.save(employee);
		employeeJurisdictionRepository.save(employee);
		if (employee.getLanguagesKnown() != null) {
			employeeLanguageRepository.save(employee);
		}
		assignmentRepository.save(employeeRequest);
		employee.getAssignments().forEach((assignment) -> {
			if (assignment.getHod() != null) {
				hodDepartmentRepository.save(assignment, employee.getTenantId());
			}
		});
		if (employee.getServiceHistory() != null) {
			serviceHistoryRepository.save(employeeRequest);
		}
		if (employee.getProbation() != null) {
			probationRepository.save(employeeRequest);
		}
		if (employee.getRegularisation() != null) {
			regularisationRepository.save(employeeRequest);
		}
		if (employee.getTechnical() != null) {
			technicalQualificationRepository.save(employeeRequest);
		}
		if (employee.getEducation() != null) {
			educationalQualificationRepository.save(employeeRequest);
		}
		if (employee.getTest() != null) {
			departmentalTestRepository.save(employeeRequest);
		}
	}

	public ResponseEntity<?> updateAsync(EmployeeRequest employeeRequest) {

		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
		Employee employee = employeeRequest.getEmployee();
		ResponseEntity<?> responseEntity = null;

	/*	try {
			responseEntity = userService.updateUser(userRequest.getUser().getId(), userRequest);
		} catch (Exception e) {
			LOGGER.debug("Error occurred while updating user", e);
			return errorHandler.getResponseEntityForUnknownUserDBUpdationError(employeeRequest.getRequestInfo());
		}

		if (responseEntity.getBody().getClass().equals(UserErrorResponse.class)
				|| responseEntity.getBody().getClass().equals(ErrorResponse.class)) {
			return responseEntity;
		}

		UserResponse userResponse = (UserResponse) responseEntity.getBody();
		User user = userResponse.getUser().get(0);
		employee.setUser(user);
*/
		try {
			employeeHelper.populateDefaultDataForUpdate(employeeRequest);
		} catch (Exception e) {
			LOGGER.debug("Error occurred while populating data in objects", e);
			return errorHandler.getResponseEntityForUnexpectedErrors(employeeRequest.getRequestInfo());
		}

		String employeeUpdateRequestJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			employeeUpdateRequestJson = mapper.writeValueAsString(employeeRequest);
			LOGGER.info("employeeJson update::" + employeeUpdateRequestJson);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting Employee to JSON during update", e);
			e.printStackTrace();
		}
	
		try {
			employeeProducer.sendMessage(propertiesManager.getUpdateEmployeeTopic(),
					propertiesManager.getEmployeeSaveKey(), employeeUpdateRequestJson);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return employeeHelper.getSuccessResponseForCreate(employee, employeeRequest.getRequestInfo());
	}

	public void update(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		employeeRepository.update(employee);
		employeeLanguageService.update(employee);
		employeeJurisdictionService.update(employee);
		assignmentService.update(employee);
		departmentalTestService.update(employee);
		serviceHistoryService.update(employee);
		probationService.update(employee);
		regularisationService.update(employee);
		technicalQualificationService.update(employee);
		educationalQualificationService.update(employee);
		employeeDocumentsService.update(employee);
	}	
}