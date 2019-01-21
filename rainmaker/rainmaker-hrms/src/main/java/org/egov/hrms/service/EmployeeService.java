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
import org.egov.hrms.model.bulk.Department;
import org.egov.hrms.model.bulk.Designation;
import org.egov.hrms.model.enums.BloodGroup;
import org.egov.hrms.repository.*;
import org.egov.hrms.service.exception.EmployeeIdNotFoundException;
import org.egov.hrms.service.exception.IdGenerationException;
import org.egov.hrms.service.exception.UserException;
import org.egov.hrms.service.helper.EmployeeHelper;
import org.egov.hrms.service.helper.EmployeeUserMapper;
import org.egov.hrms.utils.SMSUtils;
import org.egov.hrms.web.contract.*;
import org.egov.hrms.web.errorhandler.ErrorHandler;
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
	private AssignmentService assignmentService;

	@Autowired
	private ServiceHistoryService serviceHistoryService;


	@Autowired
	private RegularisationService regularisationService;


	@Autowired
	private EducationalQualificationService educationalQualificationService;

	@Autowired
	private DepartmentalTestService departmentalTestService;


	@Autowired
	private EmployeeJurisdictionService employeeJurisdictionService;

	@Autowired
	private EmployeeDocumentsService employeeDocumentsService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;


	@Autowired
	private ServiceHistoryRepository serviceHistoryRepository;


	@Autowired
	private RegularisationRepository regularisationRepository;


	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;

	@Autowired
	private DepartmentalTestRepository departmentalTestRepository;


	@Autowired
	private EmployeeJurisdictionRepository employeeJurisdictionRepository;


	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeHelper employeeHelper;

	@Autowired
	private EmployeeUserMapper employeeUserMapper;

	@Autowired
	private EmployeeDataSyncService employeeDataSyncService;

	@Autowired
	private HRMastersService hrMastersService;

	@Autowired
	private IdGenService idGenService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ApplicationProperties applicationProperties;

	public List<EmployeeInfo> getEmployees(EmployeeCriteria empCriteria, RequestInfo requestInfo) {
		List<User> usersList = null;
		List<Long> ids = null;
		List<Long> idSearchCriteria = isEmpty(empCriteria.getId()) ? null : empCriteria.getId();
		// If roleCodes or userName is present, get users first, as there will
		// be very limited results
		if (!isEmpty(empCriteria.getRoleCodes()) || !isEmpty(empCriteria.getUserName())) {
			usersList = userService.getUsers(empCriteria, requestInfo);
			log.debug("usersList returned by UsersService is :: " + usersList);
			if (isEmpty(usersList))
				return Collections.EMPTY_LIST;
			ids = usersList.stream().map(user -> user.getId()).collect(Collectors.toList());
			log.debug("User ids are :: " + ids);
			empCriteria.setId(ids);
		}

		List<EmployeeInfo> empInfoList = employeeRepository.findForCriteria(empCriteria);

		if (isEmpty(empInfoList)) {
			return Collections.EMPTY_LIST;
		}

		// If roleCodes or userName is not present, get employees first
		if (isEmpty(empCriteria.getRoleCodes()) || isEmpty(empCriteria.getUserName())) {
			ids = empInfoList.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
			log.debug("Employee ids are :: " + ids);
			empCriteria.setId(ids);
			usersList = userService.getUsers(empCriteria, requestInfo);
			log.debug("usersList returned by UsersService is :: " + usersList);
		}
		empInfoList = employeeUserMapper.mapUsersWithEmployees(empInfoList, usersList);

		if (!isEmpty(ids)) {
			List<EmployeeDocument> employeeDocuments = employeeRepository.getDocumentsForListOfEmployeeIds(ids,
					empCriteria.getTenantId());
			employeeHelper.mapDocumentsWithEmployees(empInfoList, employeeDocuments);
 		}

		empCriteria.setId(idSearchCriteria);
		return empInfoList;
	}

	public Map<String, Object> getPaginatedEmployees(EmployeeCriteria empCriteria, RequestInfo requestInfo)
			throws CloneNotSupportedException {
		List<EmployeeInfo> employeeInfos = getEmployees(empCriteria, requestInfo);

		if (isEmpty(employeeInfos))
			return getResponseForNoRecords(empCriteria.getPageSize(), empCriteria.getPageNumber());

		return getResponseForExistingRecords(empCriteria.getPageSize(), empCriteria.getPageNumber(),
				employeeInfos.size(), empCriteria, employeeInfos);
	}

	private Map<String, Object> getResponseForExistingRecords(Integer pageSize, Integer pageNumber,
			Integer recordsFetched, EmployeeCriteria empCriteria, List<EmployeeInfo> empInfoList) {
		pageSize = isEmpty(pageSize) ? Integer.parseInt(applicationProperties.empSearchPageSizeDefault()) : pageSize;
		pageNumber = isEmpty(pageNumber) ? 1 : pageNumber;

		Integer totalDBRecords = employeeRepository.getTotalDBRecords(empCriteria);
		Integer totalpages = (int) Math.ceil((double) totalDBRecords / pageSize);

		Pagination page = Pagination.builder().totalResults(recordsFetched).totalPages(totalpages)
				.currentPage(pageNumber).pageNumber(pageNumber).pageSize(pageSize).build();

		return new LinkedHashMap<String, Object>() {
			{
				put("Employee", empInfoList);
				put("Page", page);
			}
		};
	}

	private Map<String, Object> getResponseForNoRecords(Integer pageSize, Integer pageNumber) {
		Pagination page = Pagination.builder().totalResults(0).totalPages(0).currentPage(0).pageNumber(pageNumber)
				.pageSize(pageSize).build();
		return new LinkedHashMap<String, Object>() {
			{
				put("Employee", Collections.EMPTY_LIST);

				put("Page", page);
			}
		};
	}

	public Employee getEmployee(Long employeeId, String tenantId, RequestInfo requestInfo) {
		Employee employee = employeeRepository.findById(employeeId, tenantId);
		List<Long> ids = new ArrayList<>();
		ids.add(employeeId);
		if (isEmpty(employee))
			throw new EmployeeIdNotFoundException(employeeId);

		EmployeeCriteria employeeCriteria = EmployeeCriteria.builder().id(ids).tenantId(tenantId).build();


		employee.setAssignments(assignmentRepository.findByEmployeeId(employeeId, tenantId));
		employee.setServiceHistory(serviceHistoryRepository.findByEmployeeId(employeeId, tenantId));
		employee.setJurisdictions(employeeJurisdictionRepository.findByEmployeeId(employeeId, tenantId));
		employee.setEducation(educationalQualificationRepository.findByEmployeeId(employeeId, tenantId));
		employee.setTest(departmentalTestRepository.findByEmployeeId(employeeId, tenantId));
		employeeDocumentsService.populateDocumentsInRespectiveObjects(employee);

		log.debug("After Employee Search: " + employee);

		return employee;
	}

	private String getEmployeeCode(String tenantId, RequestInfo requestInfo) throws IdGenerationException {
		String idGenEmpCodeName = propertiesManager.getIdGenServiceEmpCodeName();
		String idGenEmpCodeFormat = propertiesManager.getIdGenServiceEmpCodeFormat();
		return idGenService.generate(tenantId, idGenEmpCodeName, idGenEmpCodeFormat, requestInfo);
	}

	public Employee createAsync(EmployeeRequest employeeRequest)
			throws UserException, IdGenerationException {
		// FIXME : Setting ts as null in RequestInfo as hr is following
		// common-contracts with ts as Date
		// & ID Generation Service is following ts as epoch

//		System.err.println("createAsync  requestInfoWrapper" + requestInfoWrapper);
		// RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();

//		Map<String, List<String>> hrConfigurations = hrMastersService.getHRConfigurations(employee.getTenantId(),
//				requestInfoWrapper);
//
//		if (hrConfigurations.get("Autogenerate_employeecode").get(0).equalsIgnoreCase("Y")) {
//			employee.setCode(getEmployeeCode(employee.getTenantId(), requestInfo));
//		}
//
//		if (hrConfigurations.get("Autogenerate_username").get(0).equalsIgnoreCase("Y")) {
//			userRequest.getUser().setUserName(employee.getCode());
//		}

		// FIXME : Fix a common standard for date formats in User Service.

//		SMSUtils smsUtils = new SMSUtils();
//		smsUtils.processSMS(user);
		enrichCreateRequest(employeeRequest);
//pushToTopic
		return employeeRequest.getEmployee();
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
		Instant instant = Instant.now()
		employee.setCreatedDate(instant.getEpochSecond());




		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
		UserResponse userResponse = userService.createUser(userRequest);
		User user = userResponse.getUser().get(0);

		employee.setId(user.getId());
		employee.setUuid(user.getUuid());
		employee.setUser(user);

		employeeHelper.populateDefaultDataForCreate(employeeRequest);

//		create(employeeRequest);

	}

	@Transactional
	public void create(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();


		employeeRepository.save(employee);
		if(!CollectionUtils.isEmpty(employeeRequest.getEmployee().getJurisdictions())) {
			employeeJurisdictionRepository.save(employee);
		}
		assignmentRepository.save(employeeRequest);
		if (!isEmpty(employee.getServiceHistory())) {
			serviceHistoryRepository.save(employeeRequest);
		}
		if (!isEmpty(employee.getEducation())) {
			educationalQualificationRepository.save(employeeRequest);
		}
		if (!isEmpty(employee.getTest())) {
			departmentalTestRepository.save(employeeRequest);
		}
		kafkaTemplate.send(propertiesManager.getFinanceEmployeeTopic(),
				propertiesManager.getFinanceEmployeeKey(), employeeRequest);
		/*
		 * EmployeeSync employeeSync =
		 * EmployeeSync.builder().code(employee.getCode()).tenantId(employee.
		 * getTenantId()).signature(employee.getUser().getSignature()).userName(
		 * employee.getUser().getUserName()).build(); EmployeeSyncRequest
		 * employeeSyncRequest =
		 * EmployeeSyncRequest.builder().employeeSync(employeeSync).build(); if
		 * (propertiesManager.getDataSyncEmployeeRequired())
		 * employeeDataSyncService.createDataSync(employeeSyncRequest);
		 */
	}

	public Employee updateAsync(EmployeeRequest employeeRequest) throws UserException {

		enrichUpdateRequest(employeeRequest)

		Employee employee = employeeRequest.getEmployee();

		UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);

		UserResponse userResponse = userService.updateUser(userRequest.getUser().getId(), userRequest);
		User user = userResponse.getUser().get(0);
		employee.setUser(user);

		employeeHelper.populateDefaultDataForUpdate(employeeRequest);

		AssignmentGetRequest assignmentGetRequest = AssignmentGetRequest.builder().tenantId(employee.getTenantId())
				.build();
		List<Assignment> assignments = assignmentService.getAssignments(employee.getId(), assignmentGetRequest);

		List<Long> positionfromDB = assignments.stream().map(assignment -> assignment.getPosition())
				.collect(Collectors.toList());
		List<Long> positionFromRequest = employeeRequest.getEmployee().getAssignments().stream()
				.map(assignment -> assignment.getPosition()).collect(Collectors.toList());
		boolean isPositionModified = !(ArrayUtils.isEquals(positionfromDB, positionFromRequest));

		List<Date> fromDateFromDB = assignments.stream().map(assignment -> assignment.getFromDate())
				.collect(Collectors.toList());
		List<Date> fromDateFromRequest = employeeRequest.getEmployee().getAssignments().stream()
				.map(assignment -> assignment.getFromDate()).collect(Collectors.toList());
		boolean isFromDateModified = !(ArrayUtils.isEquals(fromDateFromDB, fromDateFromRequest));

		List<Date> toDateFromDB = assignments.stream().map(assignment -> assignment.getToDate())
				.collect(Collectors.toList());
		List<Date> toDateFromRequest = employeeRequest.getEmployee().getAssignments().stream()
				.map(assignment -> assignment.getToDate()).collect(Collectors.toList());
		boolean isToDateModified = !(ArrayUtils.isEquals(toDateFromDB, toDateFromRequest));

		boolean isAssignmentDeleted = assignments.size() != employeeRequest.getEmployee().getAssignments().size();

		if (isPositionModified || isFromDateModified || isToDateModified || isAssignmentDeleted) {
			update(employeeRequest);
		}

		update(employeeRequest);
		return employee;
	}

	private void enrichUpdateRequest(EmployeeRequest employeeRequest) {
	}

	public Employee updateEmployee(EmployeeRequest employeeRequest) throws UserException {
		Employee employee = getEmployee(employeeRequest.getEmployee().getId(),
				employeeRequest.getEmployee().getTenantId(), employeeRequest.getRequestInfo());
		Employee employeeReq = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		Long requesterId = requestInfo.getUserInfo().getId();
		String tenantId = employeeRequest.getEmployee().getTenantId();
		employeeRequest.getEmployee().getAssignments().forEach((assignment) -> {
			if (isEmpty(assignment.getId()))
				employeeHelper.populateDefaultDataForNewAssignment(assignment, requesterId, tenantId);
			else
				employeeHelper.populateDefaultDataForUpdateAssignment(assignment, requesterId, tenantId);
		});
		employee.getAssignments().clear();
		employee.getAssignments().addAll(employeeReq.getAssignments());
		EmployeeRequest employeeRequestForUpdate = new EmployeeRequest();
		employeeRequestForUpdate.setEmployee(employee);
		employeeRequestForUpdate.setRequestInfo(requestInfo);
		update(employeeRequestForUpdate);
		return employee;
	}

	@Transactional
	public void update(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		employeeRepository.update(employee);
		employeeJurisdictionService.update(employee);
		assignmentService.update(employee);
		departmentalTestService.update(employee);
		serviceHistoryService.update(employee);
		regularisationService.update(employee);
		educationalQualificationService.update(employee);
		employeeDocumentsService.update(employee);

		/*
		 * EmployeeSync employeeSync =
		 * EmployeeSync.builder().code(employee.getCode()).tenantId(employee.
		 * getTenantId()).signature(employee.getUser().getSignature()).userName(
		 * employee.getUser().getUserName()).build(); EmployeeSyncRequest
		 * employeeSyncRequest =
		 * EmployeeSyncRequest.builder().employeeSync(employeeSync).build(); if
		 * (propertiesManager.getDataSyncEmployeeRequired())
		 * employeeDataSyncService.createDataSync(employeeSyncRequest);
		 */
	}


	public void setServiceHistoryDetails(EmployeeRequest employeeRequest, Boolean isUpdate) {

		List<ServiceHistory> serviceHistories = employeeRequest.getEmployee().getServiceHistory();
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder()
				.requestInfo(employeeRequest.getRequestInfo()).build();
		String[] tenant = employeeRequest.getEmployee().getTenantId().split("\\.");
		String tenantId = tenant.length > 1 ? tenant[tenant.length - 1] : tenant[0];
		ServiceHistory serviceHistory = new ServiceHistory();
		List<Assignment> assignmentList = new ArrayList<>();
		List<Assignment> removedAssignment = new ArrayList<>();

		List<Assignment> empAssignment;

		assignmentList.addAll(employeeRequest.getEmployee().getAssignments());
		if (isUpdate && !serviceHistories.isEmpty()) {

			empAssignment = assignmentList.stream().filter(assignment -> assignment.getIsPrimary().equals(true))
					.collect(Collectors.toList());

			empAssignment.stream().forEach(assign -> {
				List<Department> departments = Collections
						.singletonList(departmentService.getDepartment(employeeRequest.getEmployee().getTenantId(),
								assign.getDepartment(), requestInfoWrapper.getRequestInfo()));
				DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
						.codes(Arrays.asList(assign.getDesignation()))
						.tenantId(employeeRequest.getEmployee().getTenantId()).build();
				List<Designation> designations = designationService.getDesignations(
						designationGetRequest, employeeRequest.getEmployee().getTenantId(), requestInfoWrapper);

				serviceHistories.stream()
						.filter(history -> history.getIsAssignmentBased().equals(true)
								&& history.getAssignmentId() != null && !history.getAssignmentId().equals(""))
						.forEach(srvcHstry -> {
							if ((assign.getId() != null && !assign.getId().equals(""))
									&& assign.getId().equals(srvcHstry.getAssignmentId())) {
								srvcHstry.setDepartment(departments.get(0).getCode());
								srvcHstry.setDesignation(designations.get(0).getCode());
								srvcHstry.setPosition(assign.getPosition());
								srvcHstry.setCity(tenantId);
								srvcHstry.setServiceFrom(assign.getFromDate());
								srvcHstry.setServiceTo(assign.getToDate());
								srvcHstry.setIsAssignmentBased(true);
								removedAssignment.add(assign);
							}
						});
			});

			assignmentList.removeAll(removedAssignment);
			empAssignment = assignmentList.stream().filter(assignment -> assignment.getIsPrimary().equals(true))
					.collect(Collectors.toList());

			empAssignment.stream().forEach(assign -> {
				List<Department> departments = Collections
						.singletonList(departmentService.getDepartment(employeeRequest.getEmployee().getTenantId(),
								assign.getDepartment(), requestInfoWrapper.getRequestInfo()));
				DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
						.codes(Arrays.asList(assign.getDesignation()))
						.tenantId(employeeRequest.getEmployee().getTenantId()).build();
				List<Designation> designations = designationService.getDesignations(
						designationGetRequest, employeeRequest.getEmployee().getTenantId(), requestInfoWrapper);
				if (assign.getId() != null && !assign.getId().equals("")) {
					serviceHistory.setAssignmentId(assign.getId());
				}
				serviceHistory.setServiceInfo("Sevice entry changes for assignment");
				serviceHistory.setCity(tenantId);
				serviceHistory.setServiceFrom(assign.getFromDate());
				serviceHistory.setServiceTo(assign.getToDate());
				serviceHistory.setDepartment(departments.get(0).getCode());
				serviceHistory.setDesignation(designations.get(0).getCode());
				serviceHistory.setPosition(assign.getPosition());
				serviceHistory.setIsAssignmentBased(true);
				serviceHistory.setTenantId(employeeRequest.getEmployee().getTenantId());
				serviceHistories.add(serviceHistory);
			});
			employeeRequest.getEmployee().setServiceHistory(serviceHistories);
		} else {
			setServiceHistoryFromAssignment(employeeRequest, assignmentList);
		}
	}

	public void setServiceHistoryFromAssignment(EmployeeRequest employeeRequest, List<Assignment> assignmentList) {
		List<ServiceHistory> serviceHistories = employeeRequest.getEmployee().getServiceHistory();
		List<Assignment> empAssignment = assignmentList.stream()
				.filter(assignment -> assignment.getIsPrimary().equals(true)).collect(Collectors.toList());

		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder()
				.requestInfo(employeeRequest.getRequestInfo()).build();
		ServiceHistory serviceHistory = new ServiceHistory();
		String[] tenant = employeeRequest.getEmployee().getTenantId().split("\\.");
		String tenantId = tenant.length > 1 ? tenant[tenant.length - 1] : tenant[0];
		List<Assignment> removedAssignment = new ArrayList<>();
		empAssignment.stream().forEach(assign -> {
			List<Department> departments = Collections
					.singletonList(departmentService.getDepartment(employeeRequest.getEmployee().getTenantId(),
							assign.getDepartment(), requestInfoWrapper.getRequestInfo()));
			DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
					.codes(Arrays.asList(assign.getDesignation())).tenantId(employeeRequest.getEmployee().getTenantId())
					.build();
			List<Designation> designations = designationService.getDesignations(
					designationGetRequest, employeeRequest.getEmployee().getTenantId(), requestInfoWrapper);

			if (assign.getId() != null && !assign.getId().equals("")) {
				serviceHistory.setAssignmentId(assign.getId());
			}
			serviceHistory.setServiceInfo("Sevice entry changes for assignment");
			serviceHistory.setCity(tenantId);
			serviceHistory.setServiceFrom(assign.getFromDate());
			serviceHistory.setServiceTo(assign.getToDate());
			serviceHistory.setDepartment(departments.get(0).getCode());
			serviceHistory.setDesignation(designations.get(0).getCode());
			serviceHistory.setPosition(assign.getPosition());
			serviceHistory.setIsAssignmentBased(true);
			serviceHistory.setTenantId(employeeRequest.getEmployee().getTenantId());
			serviceHistories.add(serviceHistory);
			removedAssignment.add(assign);
		});
		assignmentList.removeAll(removedAssignment);
		employeeRequest.getEmployee().setServiceHistory(serviceHistories);
	}

}