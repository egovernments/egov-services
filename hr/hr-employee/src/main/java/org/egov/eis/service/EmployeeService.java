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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.eis.broker.EmployeeProducer;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.model.User;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.DepartmentalTestRepository;
import org.egov.eis.repository.EducationalQualificationRepository;
import org.egov.eis.repository.EmployeeDocumentsRepository;
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
	private EmployeeDocumentsRepository documentsRepository;
 
	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

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

	public static final String INSERT_PROBATION_QUERY = "SELECT id FROM egeis_departmentaltest WHERE employeeid = ?";

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
		populateDocumentsInRespectiveObjects(employee);
		
		return employee;
	}

	private void populateDocumentsInRespectiveObjects(Employee employee) {
		List<EmployeeDocument> documents = employeeDocumentsRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		for (EmployeeDocument document : documents) {

			DocumentReferenceType referenceType = DocumentReferenceType.valueOf(document.getReferenceType());
			switch (referenceType) {
			case EMPLOYEE_HEADER:
				employee.getDocuments().add(document.getDocument());
				break;
			case ASSIGNMENT:
				for (Assignment assignment : employee.getAssignments())
					if (assignment.getId().equals(document.getReferenceId()))
						assignment.getDocuments().add(document.getDocument());
				break;
			case SERVICE:
				for (ServiceHistory serviceHistory : employee.getServiceHistory())
					if (serviceHistory.getId().equals(document.getReferenceId()))
						serviceHistory.getDocuments().add(document.getDocument());
				break;
			case TECHNICAL:
				for (TechnicalQualification technicalQualification : employee.getTechnical())
					if (technicalQualification.getId().equals(document.getReferenceId()))
						technicalQualification.getDocuments().add(document.getDocument());
				break;
			case EDUCATION:
				for (EducationalQualification educationalQualification : employee.getEducation())
					if (educationalQualification.getId().equals(document.getReferenceId()))
						educationalQualification.getDocuments().add(document.getDocument());
				break;
			case TEST:
				for (DepartmentalTest departmentalTest : employee.getTest())
					if (departmentalTest.getId().equals(document.getReferenceId()))
						departmentalTest.getDocuments().add(document.getDocument());
				break;
			case REGULARISATION:
				for (Regularisation regularisation : employee.getRegularisation())
					if (regularisation.getId().equals(document.getReferenceId()))
						regularisation.getDocuments().add(document.getDocument());
				break;
			case PROBATION:
				for (Probation probation : employee.getProbation())
					if (probation.getId().equals(document.getReferenceId()))
						probation.getDocuments().add(document.getDocument());
				break;
			}
		}	
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

		String code = employeeHelper.getEmployeeCode(user.getId());
		Employee employee = employeeRequest.getEmployee();
		employee.setId(user.getId());
		employee.setCode(code);
		employee.setUser(user);

		try {
			employeeHelper.populateDefaultDataForCreate(employeeRequest);
		} catch (Exception e) {
			LOGGER.debug("Error occurred while populating data in objects", e);
			return errorHandler.getResponseEntityForUnexpectedErrors(employeeRequest.getRequestInfo());
		}

		System.out.println("create appointment date:" +employee.getDateOfAppointment());
		System.out.println("create from date:" +employee.getAssignments().get(0).getFromDate());
		
		String employeeRequestJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			employeeRequestJson = mapper.writeValueAsString(employeeRequest);
			LOGGER.info("employeeJson::" + employeeRequestJson);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting Employee to JSON", e);
			e.printStackTrace();
		}
		LocalDateTime time = LocalDateTime.ofInstant(
				employee.getAssignments().get(0).getFromDate().toInstant(), 
				ZoneId.systemDefault());
		
		System.out.printf("After Json Day:%d,hour:%d,minute:%d,seconds:%d",time.getDayOfMonth(),time.getHour(),time.getMinute(),time.getSecond());
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
		updateJurisdictions(employee);
		updateAssignments(employee);
		updateDepartmentalTests(employee);
		updateServiceHistories(employee);
		updateProbations(employee);
		updateRegularisations(employee);
		updateTechnicals(employee);
        updateEducations(employee);
        updateDocuments(employee);

	}

	/**
	 * Query db and get all documents in a list.
	 * Iterate all the input documents for the employee and check if this document is present in the 
	 * list of docuemnts made from db in the previous step. If present, do nothing; if absent, insert this record
	 * in db.
	 * Finally, delete all documents in db that are not in input list.
	 * @param employee
	 */
	private void updateDocuments(Employee employee) {
		List<EmployeeDocument> documentsFromDb = employeeDocumentsRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		System.out.println("docs from db" +documentsFromDb);
		System.out.println("docs from input" +employee.getDocuments());
		// updateDocumentsForEmployee(employee, documentsFromDb);
		if(employee.getDocuments() != null){
		updateDocuments(employee.getDocuments(), DocumentReferenceType.EMPLOYEE_HEADER, employee.getId(),
				documentsFromDb, employee.getId(), employee.getTenantId());
		}
		
		employee.getAssignments().forEach((assignment) -> {
			if(assignment.getDocuments() != null){
			//updateDocumentsForAssignment(assignment, documentsFromDb, employee.getId(), employee.getTenantId());
			updateDocuments(assignment.getDocuments(), DocumentReferenceType.ASSIGNMENT, assignment.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getProbation().forEach((probation) -> {
			if(probation.getDocuments() != null){
			updateDocuments(probation.getDocuments(), DocumentReferenceType.PROBATION, probation.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getRegularisation().forEach((regularisation) -> {
			if(regularisation.getDocuments() != null){
			updateDocuments(regularisation.getDocuments(), DocumentReferenceType.REGULARISATION, regularisation.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getTest().forEach((test) -> {
			if(test.getDocuments() != null){
			updateDocuments(test.getDocuments(), DocumentReferenceType.TEST, test.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getTechnical().forEach((technical) -> {
			if(technical.getDocuments() != null){
			updateDocuments(technical.getDocuments(), DocumentReferenceType.TECHNICAL, technical.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
		}
		});
		employee.getEducation().forEach((education) -> {
			if(education.getDocuments() != null){
			updateDocuments(education.getDocuments(), DocumentReferenceType.EDUCATION, education.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
	}
	
	private void updateDocuments(List<String> inputDocuments, DocumentReferenceType documentReferenceType,
			Long referenceId, List<EmployeeDocument> documentsFromDb, Long employeeId, String tenantId) {
		// insert all documents in input list but not in db
		for (String docUrl : inputDocuments) {
			if (!documentPresentInDB(documentsFromDb, docUrl, documentReferenceType)) {
				documentsRepository.save(employeeId, docUrl, 
						documentReferenceType.toString(), referenceId, tenantId);
			}
		}
		
		// delete all docs from db that are not in input list
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (DocumentReferenceType.valueOf(documentInDb.getReferenceType()) == documentReferenceType
				&& !inputDocuments.contains(documentInDb.getDocument())) {
					documentsRepository.delete(employeeId, documentInDb.getDocument(), 
							documentReferenceType.toString(), referenceId, tenantId);
			}
		}
	}
	
	private boolean documentPresentInDB(List<EmployeeDocument> documentsFromDb, String docUrl, DocumentReferenceType documentReferenceType) {
		boolean foundDocInDb = false;
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (DocumentReferenceType.valueOf(documentInDb.getReferenceType()) == documentReferenceType
					&& documentInDb.getDocument().equals(docUrl)) {
					foundDocInDb = true;
					break;
			}
		}
		return foundDocInDb;
	}

	private void updateEducations(Employee employee) {
		List<EducationalQualification> educations = educationalQualificationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getEducation().forEach((education) -> {
			System.out.println("present education" +education);
			if (educationNeedsInsert(education, educations)) {
				System.out.println("insert education");
				educationalQualificationRepository.insert(education, employee.getId());
			} else if (educationNeedsUpdate(education, educations)) {
				System.out.println("update education");
				education.setTenantId(employee.getTenantId());
				educationalQualificationRepository.update(education);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
     });
}

	private boolean educationNeedsUpdate(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations) {
			System.out.println("oldEducation-" +oldEducation);
			if (education.equals(oldEducation)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean educationNeedsInsert(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations) {
			if (education.getId().equals(oldEducation.getId()))
				return false;
		}
		return true;
	}

	private void updateTechnicals(Employee employee) {
		List<TechnicalQualification> technicals = technicalQualificationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getTechnical().forEach((technical) -> {
			System.out.println("present regularisation" +technical);
			if (technicalNeedsInsert(technical, technicals)) {
				System.out.println("insert technical");
				technicalQualificationRepository.insert(technical, employee.getId());
			} else if (technicalNeedsUpdate(technical, technicals)) {
				System.out.println("update technical");
				technical.setTenantId(employee.getTenantId());
				technicalQualificationRepository.update(technical);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
     });
		
	}

	private boolean technicalNeedsUpdate(TechnicalQualification technical, List<TechnicalQualification> technicals) {
		for (TechnicalQualification oldTechnical : technicals) {
			System.out.println("oldTechnical-" +oldTechnical);
			if (technical.equals(oldTechnical)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean technicalNeedsInsert(TechnicalQualification technical, List<TechnicalQualification> technicals) {
		for (TechnicalQualification oldTechnical : technicals) {
			if (technical.getId().equals(oldTechnical.getId()))
				return false;
		}
		return true;
	}

	private void updateRegularisations(Employee employee) {
		List<Regularisation> regularisations = regularisationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getRegularisation().forEach((regularisation) -> {
			System.out.println("present regularisation" +regularisation);
			if (regularisationNeedsInsert(regularisation, regularisations)) {
				System.out.println("insert regularisation");
				regularisationRepository.insert(regularisation, employee.getId());
			} else if (regularisationNeedsUpdate(regularisation, regularisations)) {
				System.out.println("update regularisation");
				regularisation.setTenantId(employee.getTenantId());
				regularisationRepository.update(regularisation);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
     });
		
	}

	private boolean regularisationNeedsUpdate(Regularisation regularisation, List<Regularisation> regularisations) {
		for (Regularisation oldRegularisation : regularisations) {
			System.out.println("oldRegularisation-" +oldRegularisation);
			if (regularisation.equals(oldRegularisation)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean regularisationNeedsInsert(Regularisation regularisation, List<Regularisation> regularisations) {
		for (Regularisation oldRegularisation : regularisations) {
			if (regularisation.getId().equals(oldRegularisation.getId()))
				return false;
		}
		return true;
	}

	private void updateProbations(Employee employee) {
		List<Probation> probations = probationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getProbation().forEach((probation) -> {
			if (probationNeedsInsert(probation, probations)) {
				probationRepository.insert(probation, employee.getId());
			} else if (probationNeedsUpdate(probation, probations)) {
				probation.setTenantId(employee.getTenantId());
				probationRepository.update(probation);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
     });
		
	}

	private boolean probationNeedsUpdate(Probation probation, List<Probation> probations) {
		for (Probation oldProbation : probations) {
			System.out.println("oldProbation-" +oldProbation);
			if (probation.equals(oldProbation)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean probationNeedsInsert(Probation probation, List<Probation> probations) {
		for (Probation oldProbation : probations) {
			if (probation.getId().equals(oldProbation.getId()))
				return false;
		}
		return true;
	}

	private void updateServiceHistories(Employee employee) {
		List<ServiceHistory> services = serviceHistoryRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getServiceHistory().forEach((service) -> {
			if (serviceHistoryNeedsInsert(service, services)) {
				serviceHistoryRepository.insert(service, employee.getId());
			} else if (serviceHistoryNeedsUpdate(service, services)) {
				service.setTenantId(employee.getTenantId());
				serviceHistoryRepository.update(service);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
     });	
	}

	private boolean serviceHistoryNeedsUpdate(ServiceHistory service, List<ServiceHistory> services) {
		for (ServiceHistory oldService : services) {
			System.out.println("oldService-" +oldService);
			if (service.equals(oldService)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean serviceHistoryNeedsInsert(ServiceHistory service, List<ServiceHistory> services) {
		for (ServiceHistory oldService : services) {
			if (service.getId().equals(oldService.getId()))
				return false;
		}
		return true;
	}

	private void updateDepartmentalTests(Employee employee) {
		List<DepartmentalTest> tests = departmentalTestRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());

		employee.getTest().forEach((test) -> {
			if (departmentalTestNeedsInsert(test, tests)) {
				departmentalTestRepository.insert(test, employee.getId());
			} else if (departmentalTestNeedsUpdate(test, tests)) {
				test.setTenantId(employee.getTenantId());
				departmentalTestRepository.update(test);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());

		});
}
	
	private boolean departmentalTestNeedsUpdate(DepartmentalTest test, List<DepartmentalTest> tests) {
		for (DepartmentalTest oldTest : tests) {
			System.out.println("oldtest-" +oldTest);
			if (test.equals(oldTest)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private boolean departmentalTestNeedsInsert(DepartmentalTest test, List<DepartmentalTest> tests) {
		for (DepartmentalTest oldTest : tests) {
			if (test.getId().equals(oldTest.getId()))
				return false;
		}
		return true;
	}


	private void updateAssignments(Employee employee) {
		List<Assignment> assignments = assignmentRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getAssignments().forEach((assignment) -> {
			if (assignmentNeedsInsert(assignment, assignments)) {
				assignmentRepository.insert(assignment, employee.getId());
			} else if (assignmentNeedsUpdate(assignment, assignments)) {
				assignment.setTenantId(employee.getTenantId());
				assignmentRepository.update(assignment);
			}
			// assignmentRepository.findAndDeleteInDBThatAreNotInList(employee.getAssignments());
			if (assignment.getHod() != null) {
				// FIXME HOD not handled for update
				hodDepartmentRepository.save(assignment, employee.getTenantId());
			}
		});
	}
	
	private boolean assignmentNeedsInsert(Assignment assignment, List<Assignment> assignments) {
		for (Assignment oldAssignment : assignments) {
			if (assignment.getId().equals(oldAssignment.getId()))
				return false;
		}
		return true;
	}

	private boolean assignmentNeedsUpdate(Assignment assignment, List<Assignment> assignments) {
		System.out.println("assignment-" +assignment);
		for (Assignment oldAssignment : assignments) {
			System.out.println("oldAssignment-" +oldAssignment);
			if (assignment.equals(oldAssignment)) {
				System.out.println("equal!!");
				return false;
			} else {
				System.out.println("not equal!!");
			}
		}
		return true;
	}

	private void updateJurisdictions(Employee employee) {
		employee.getJurisdictions().forEach((jurisdiction) -> {
			// if jurisdiction id already exists in table, we don't do anything. When absent, we insert the record.
			if (!employeeJurisdictionRepository.jurisdictionAlreadyExists(jurisdiction, employee.getId(), employee.getTenantId()))
				employeeJurisdictionRepository.insert(jurisdiction, employee.getId(), employee.getTenantId());
			
			// employeeJurisdictionRepository.findAndDeleteInDBThatAreNotInList(employee.getServiceHistory());
		});
	}




}