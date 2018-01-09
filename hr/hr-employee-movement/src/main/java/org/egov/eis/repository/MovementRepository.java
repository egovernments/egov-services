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

package org.egov.eis.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Document;
import org.egov.eis.model.Movement;
import org.egov.eis.model.enums.MovementStatus;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.repository.builder.MovementQueryBuilder;
import org.egov.eis.repository.rowmapper.MovementRowMapper;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.UserService;
import org.egov.eis.service.WorkFlowService;
import org.egov.eis.web.contract.Assignment;
import org.egov.eis.web.contract.BankBranchContract;
import org.egov.eis.web.contract.BankContract;
import org.egov.eis.web.contract.Category;
import org.egov.eis.web.contract.Community;
import org.egov.eis.web.contract.DepartmentalTest;
import org.egov.eis.web.contract.Designation;
import org.egov.eis.web.contract.EducationalQualification;
import org.egov.eis.web.contract.Employee;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.EmployeeType;
import org.egov.eis.web.contract.Group;
import org.egov.eis.web.contract.HRStatus;
import org.egov.eis.web.contract.Language;
import org.egov.eis.web.contract.MovementRequest;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.Probation;
import org.egov.eis.web.contract.ProcessInstance;
import org.egov.eis.web.contract.RecruitmentMode;
import org.egov.eis.web.contract.RecruitmentQuota;
import org.egov.eis.web.contract.RecruitmentType;
import org.egov.eis.web.contract.Regularisation;
import org.egov.eis.web.contract.Religion;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.Role;
import org.egov.eis.web.contract.ServiceHistory;
import org.egov.eis.web.contract.Task;
import org.egov.eis.web.contract.TechnicalQualification;
import org.egov.eis.web.contract.User;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MovementRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MovementRowMapper movementRowMapper;

	@Autowired
	private MovementQueryBuilder movementQueryBuilder;

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private MovementDocumentsRepository documentsRepository;

	public List<Movement> findForCriteria(final MovementSearchRequest movementSearchRequest,
			final RequestInfo requestInfo) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = movementQueryBuilder.getQuery(movementSearchRequest, preparedStatementValues,
				requestInfo);
		final List<Movement> movements = new ArrayList<>();
		for (final Movement movement : movements) {
			final List<Document> documents = documentsRepository.findByMovementId(movement.getId(),
					movement.getTenantId());
			for (final Document document : documents)
				movement.getDocuments().add(document.getDocument());
		}
		return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), movementRowMapper);
	}

	public MovementRequest saveMovement(final MovementRequest movementRequest) {
		ProcessInstance processInstance = new ProcessInstance();
		Long stateId = null;
		if (StringUtils.isEmpty(movementRequest.getType()))
			processInstance = workFlowService.start(movementRequest);
		if (processInstance.getId() != null)
			stateId = Long.valueOf(processInstance.getId());
		final Date now = new Date();
		final UserResponse userResponse = userService.findUserByUserNameAndTenantId(movementRequest.getRequestInfo());
		for (final Movement movement : movementRequest.getMovement()) {
			movement.setStateId(stateId);
			final Object[] obj = new Object[] { movement.getEmployeeId(), movement.getTypeOfMovement().toString(),
					movement.getCurrentAssignment(), movement.getTransferType().toString(),
					movement.getPromotionBasis().getId(), movement.getRemarks(), movement.getReason().getId(),
					movement.getEffectiveFrom(), movement.getEnquiryPassedDate(), movement.getTransferedLocation(),
					movement.getDepartmentAssigned(), movement.getDesignationAssigned(), movement.getPositionAssigned(),
					movement.getFundAssigned(), movement.getFunctionAssigned(), movement.getEmployeeAcceptance(),
					movement.getStatus(), movement.getStateId(), userResponse.getUsers().get(0).getId(), now,
					userResponse.getUsers().get(0).getId(), now, movement.getTenantId() };
			if (movement.getDocuments() != null && !movement.getDocuments().isEmpty())
				documentsRepository.save(movement.getId(), movement.getDocuments(), movement.getTenantId());
			jdbcTemplate.update(MovementQueryBuilder.insertMovementQuery(), obj);
		}
		return movementRequest;
	}

	public Movement updateMovement(final MovementRequest movementRequest) {
		final Task task = workFlowService.update(movementRequest);
		final Date now = new Date();
		final Movement movement = movementRequest.getMovement().get(0);
		final UserResponse userResponse = userService.findUserByUserNameAndTenantId(movementRequest.getRequestInfo());
		movement.setStateId(Long.valueOf(task.getId()));
		movementStatusChange(movement, movementRequest.getRequestInfo());
		final Object[] obj = new Object[] { movement.getEmployeeId(), movement.getTypeOfMovement().toString(),
				movement.getCurrentAssignment(), movement.getTransferType().toString(),
				movement.getPromotionBasis().getId(), movement.getRemarks(), movement.getReason().getId(),
				movement.getEffectiveFrom(), movement.getEnquiryPassedDate(), movement.getTransferedLocation(),
				movement.getDepartmentAssigned(), movement.getDesignationAssigned(), movement.getPositionAssigned(),
				movement.getFundAssigned(), movement.getFunctionAssigned(), movement.getEmployeeAcceptance(),
				movement.getStatus(), movement.getStateId(), userResponse.getUsers().get(0).getId(), now,
				userResponse.getUsers().get(0).getId(), now, movement.getId(), movement.getTenantId() };
		jdbcTemplate.update(MovementQueryBuilder.updateMovementQuery(), obj);
		if (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION)
				&& movement.getStatus()
						.equals(employeeService.getHRStatuses(propertiesManager.getHrMastersServiceStatusesKey(),
								MovementStatus.APPROVED.toString(), null, movement.getTenantId(),
								movementRequest.getRequestInfo()).get(0).getId())
				&& movement.getEmployeeAcceptance())
			try {
				promoteEmployee(movementRequest);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		else if ((movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER)
				|| movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER_CUM_PROMOTION))
				&& movement.getStatus()
						.equals(employeeService.getHRStatuses(propertiesManager.getHrMastersServiceStatusesKey(),
								MovementStatus.APPROVED.toString(), null, movement.getTenantId(),
								movementRequest.getRequestInfo()).get(0).getId())
				&& movement.getEmployeeAcceptance())
			try {
				promoteEmployee(movementRequest);
				// transferEmployee(movementRequest);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return movement;
	}

	private void promoteEmployee(final MovementRequest movementRequest) throws ParseException {
		final EmployeeInfo employeeInfo = employeeService.getEmployee(movementRequest);
		final Movement movement = movementRequest.getMovement().get(0);
		final Date effectiveFromDate = movement.getEffectiveFrom();
		Date effectiveToDate = new Date();
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat inputDOB = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
		calendar.setTime(effectiveFromDate);
		calendar.add(Calendar.DATE, -1);
		final Date yesterday = calendar.getTime();
		final Employee employee = new Employee();

		if (employeeInfo.getDateOfRetirement() != null && !employeeInfo.getDateOfRetirement().equals(""))
			effectiveToDate = employeeInfo.getDateOfRetirement();
		else {
			Calendar c = Calendar.getInstance();
			c.setTime(effectiveToDate);
			c.add(Calendar.YEAR, 60);
			effectiveToDate = c.getTime();
		}
		employee.setId(employeeInfo.getId());
		employee.setCode(employeeInfo.getCode());
		employee.setDateOfAppointment(employeeInfo.getDateOfAppointment());
		employee.setEmployeeStatus(employeeInfo.getEmployeeStatus());
		employee.setEmployeeType(employeeInfo.getEmployeeType());
		employee.setMaritalStatus(employeeInfo.getMaritalStatus());
		employee.setTenantId(employeeInfo.getTenantId());
		employee.getAssignments().addAll(employeeInfo.getAssignments());

		for (final Assignment employeeAssignment : employee.getAssignments()) {
			if (employeeAssignment.getFromDate().before(effectiveFromDate)
					&& employeeAssignment.getToDate().after(effectiveFromDate))
				employeeAssignment.setToDate(yesterday);
			employeeAssignment.setIsPrimary(false);
		}
		if (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION)
				&& movement.getStatus()
						.equals(employeeService.getHRStatuses(propertiesManager.getHrMastersServiceStatusesKey(),
								MovementStatus.APPROVED.toString(), null, movement.getTenantId(),
								movementRequest.getRequestInfo()).get(0).getId())
				&& movement.getEmployeeAcceptance()) {
			final Assignment assignment = new Assignment();
			assignment.setPosition(movement.getPositionAssigned());
			assignment.setFund(movement.getFundAssigned());
			assignment.setFunction(movement.getFunctionAssigned());
			assignment.setDepartment(movement.getDepartmentAssigned());
			assignment.setDesignation(movement.getDesignationAssigned());
			assignment.setIsPrimary(true);
			assignment.setFromDate(effectiveFromDate);
			assignment.setToDate(effectiveToDate);
			assignment.setTenantId(movement.getTenantId());
			employee.getAssignments().add(assignment);
		}

		employee.getJurisdictions().addAll(employeeInfo.getJurisdictions());

		final User user = new User();
		user.setId(employeeInfo.getId());
		user.setName(employeeInfo.getName());
		user.setDob(output.format(inputDOB.parse(employeeInfo.getDob())));
		user.setActive(employeeInfo.getActive());
		user.setGender(employeeInfo.getGender());
		user.setTenantId(employeeInfo.getTenantId());
		employee.setUser(user);
		employeeService.updateEmployee(employee, movement.getTenantId(), movementRequest.getRequestInfo());
	}

	private void transferEmployee(final MovementRequest movementRequest) throws ParseException {
		final EmployeeInfo employeeInfo = employeeService.getEmployee(movementRequest);
		Employee employee = new Employee();
		final Movement movement = movementRequest.getMovement().get(0);
		final SimpleDateFormat inputDOB = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
		final String source = movement.getTenantId();
		final String destination = movement.getTransferedLocation();
		final RequestInfo sourceRequestInfo = movementRequest.getRequestInfo();
		final RequestInfo destinationRequestInfo = movementRequest.getRequestInfo();
		destinationRequestInfo.getUserInfo().setTenantId(destination);
		final Date effectiveFromDate = movement.getEffectiveFrom();
		employee.setId(null);
		employee.getUser().setId(null);
		employee.getUser().setTenantId(destination);
		if (employee.getUser().getDob() != null)
			employee.getUser().setDob(output.format(inputDOB.parse(employee.getUser().getDob())));
		for (final Role role : employee.getUser().getRoles())
			role.setId(null);
		for (final ServiceHistory history : employee.getServiceHistory()) {
			history.setTenantId(destination);
			history.setId(null);
		}
		for (final Probation probation : employee.getProbation()) {
			probation.setId(null);
			probation.setTenantId(destination);
			final List<Designation> designations = employeeService.getDesignations(null, probation.getDesignation(),
					destination, sourceRequestInfo);
			if (!designations.isEmpty())
				probation.setDesignation(employeeService
						.getDesignations(designations.get(0).getCode(), null, destination, destinationRequestInfo)
						.get(0).getId());
		}
		for (final Regularisation regularisation : employee.getRegularisation()) {
			regularisation.setId(null);
			regularisation.setTenantId(destination);
			final List<Designation> designations = employeeService.getDesignations(null,
					regularisation.getDesignation(), destination, sourceRequestInfo);
			if (!designations.isEmpty())
				regularisation.setDesignation(employeeService
						.getDesignations(designations.get(0).getCode(), null, destination, destinationRequestInfo)
						.get(0).getId());
		}
		for (final TechnicalQualification technicalQualification : employee.getTechnical()) {
			technicalQualification.setId(null);
			technicalQualification.setTenantId(destination);
		}
		for (final EducationalQualification educationalQualification : employee.getEducation()) {
			educationalQualification.setId(null);
			educationalQualification.setTenantId(destination);
		}
		for (final DepartmentalTest departmentalTest : employee.getTest()) {
			departmentalTest.setId(null);
			departmentalTest.setTenantId(destination);
		}
		employee.getAssignments().clear();
		final Assignment assignment = new Assignment();
		assignment.setPosition(movement.getPositionAssigned());
		assignment.setFund(movement.getFundAssigned());
		assignment.setFunction(movement.getFunctionAssigned());
		assignment.setDepartment(movement.getDepartmentAssigned());
		assignment.setDesignation(movement.getDesignationAssigned());
		assignment.setIsPrimary(true);
		assignment.setFromDate(effectiveFromDate);
		assignment.setToDate(employee.getDateOfRetirement());
		assignment.setTenantId(destination);
		employee.getAssignments().add(assignment);

		final List<HRStatus> hrStatus = employeeService.getHRStatuses(
				propertiesManager.getHrMastersServiceStatusesEmployeesKey(), null, employee.getEmployeeStatus(), source,
				sourceRequestInfo);
		if (!hrStatus.isEmpty()) {
			final Long destinationHRStatusId = employeeService
					.getHRStatuses(propertiesManager.getHrMastersServiceStatusesEmployeesKey(),
							hrStatus.get(0).getCode(), null, destination, destinationRequestInfo)
					.get(0).getId();
			employee.setEmployeeStatus(destinationHRStatusId);
		}

		final List<RecruitmentMode> recruitmentModes = employeeService.getRecruitmentModes(null,
				employee.getRecruitmentMode(), source, sourceRequestInfo);
		if (!recruitmentModes.isEmpty())
			employee.setRecruitmentMode(employeeService
					.getRecruitmentModes(recruitmentModes.get(0).getName(), null, destination, destinationRequestInfo)
					.get(0).getId());

		final List<RecruitmentType> recruitmentTypes = employeeService.getRecruitmentTypes(null,
				employee.getRecruitmentType(), destination, sourceRequestInfo);
		if (!recruitmentTypes.isEmpty())
			employee.setRecruitmentType(employeeService
					.getRecruitmentTypes(recruitmentTypes.get(0).getName(), null, destination, destinationRequestInfo)
					.get(0).getId());

		final List<RecruitmentQuota> recruitmentQuotas = employeeService.getRecruitmentQuotas(null,
				employee.getRecruitmentQuota(), destination, sourceRequestInfo);
		if (!recruitmentQuotas.isEmpty())
			employee.setRecruitmentQuota(employeeService
					.getRecruitmentQuotas(recruitmentQuotas.get(0).getName(), null, destination, destinationRequestInfo)
					.get(0).getId());

		final List<EmployeeType> employeeTypes = employeeService.getEmployeeTypes(null, employee.getEmployeeType(),
				destination, sourceRequestInfo);
		if (!employeeTypes.isEmpty())
			employee.setEmployeeType(employeeService
					.getEmployeeTypes(employeeTypes.get(0).getName(), null, destination, destinationRequestInfo).get(0)
					.getId());

		final List<Language> motherTongues = employeeService.getLanguages(null, employee.getMotherTongue(), destination,
				sourceRequestInfo);
		if (!motherTongues.isEmpty())
			employee.setMotherTongue(employeeService
					.getLanguages(motherTongues.get(0).getName(), null, destination, destinationRequestInfo).get(0)
					.getId());

		final List<Religion> religions = employeeService.getReligions(null, employee.getReligion(), destination,
				sourceRequestInfo);
		if (!religions.isEmpty())
			employee.setReligion(
					employeeService.getReligions(religions.get(0).getName(), null, destination, destinationRequestInfo)
							.get(0).getId());

		final List<Community> communities = employeeService.getCommunities(null, employee.getCommunity(), destination,
				sourceRequestInfo);
		if (!communities.isEmpty())
			employee.setCommunity(employeeService
					.getCommunities(communities.get(0).getName(), null, destination, destinationRequestInfo).get(0)
					.getId());

		final List<Category> categories = employeeService.getCategories(null, employee.getCategory(), destination,
				sourceRequestInfo);
		if (!categories.isEmpty())
			employee.setCategory(employeeService
					.getCategories(categories.get(0).getName(), null, destination, destinationRequestInfo).get(0)
					.getId());

		final List<Long> languagesKnown = new ArrayList<>();
		for (final Long language : employee.getLanguagesKnown()) {
			final List<Language> languages = employeeService.getLanguages(null, language, destination,
					sourceRequestInfo);
			if (!languages.isEmpty())
				languagesKnown.add(employeeService
						.getLanguages(languages.get(0).getName(), null, destination, sourceRequestInfo).get(0).getId());
		}
		employee.setLanguagesKnown(languagesKnown);

		final List<BankContract> bankContracts = employeeService.getBanks(null, employee.getBank(), destination,
				sourceRequestInfo);
		if (!bankContracts.isEmpty())
			employee.setBank(
					employeeService.getBanks(bankContracts.get(0).getCode(), null, destination, destinationRequestInfo)
							.get(0).getId());

		final List<BankBranchContract> bankBranchContracts = employeeService.getBankBranches(null,
				employee.getBankBranch(), destination, sourceRequestInfo);
		if (!bankBranchContracts.isEmpty())
			employee.setBankBranch(employeeService
					.getBankBranches(bankBranchContracts.get(0).getCode(), null, destination, destinationRequestInfo)
					.get(0).getId());

		final List<Group> groups = employeeService.getGroups(null, employee.getGroup(), destination, sourceRequestInfo);
		if (!groups.isEmpty())
			employee.setGroup(employeeService
					.getGroups(groups.get(0).getName(), null, destination, destinationRequestInfo).get(0).getId());

		employeeService.createEmployee(employee, destination, destinationRequestInfo);
	}

	private void movementStatusChange(final Movement movement, final RequestInfo requestInfo) {
		final String workFlowAction = movement.getWorkflowDetails().getAction();
		final String objectName = propertiesManager.getHrMastersServiceStatusesKey();
		if ("Approve".equalsIgnoreCase(workFlowAction))
			movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.APPROVED.toString(), null,
					movement.getTenantId(), requestInfo).get(0).getId());
		else if ("Reject".equalsIgnoreCase(workFlowAction))
			movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.REJECTED.toString(), null,
					movement.getTenantId(), requestInfo).get(0).getId());
		else if ("Cancel".equalsIgnoreCase(workFlowAction))
			movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.CANCELLED.toString(), null,
					movement.getTenantId(), requestInfo).get(0).getId());
		else if ("Submit".equalsIgnoreCase(workFlowAction))
			movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.RESUBMITTED.toString(), null,
					movement.getTenantId(), requestInfo).get(0).getId());
	}

	public List<Movement> findForExistingMovement(final Movement movement, final RequestInfo requestInfo) {
		final String objectName = propertiesManager.getHrMastersServiceStatusesKey();
		String status = "";
		Long movementStatusId = employeeService.getHRStatuses(objectName, MovementStatus.CANCELLED.toString(), null,
				movement.getTenantId(), requestInfo).get(0).getId();
		status = movementStatusId.toString();
		movementStatusId = employeeService.getHRStatuses(objectName, MovementStatus.APPROVED.toString(), null,
				movement.getTenantId(), requestInfo).get(0).getId();
		if (movementStatusId != null)
			status = status + "," + movementStatusId.toString();

		final List<Object> preparedStatementValues = new ArrayList<>();
		MovementSearchRequest movementSearchRequest = new MovementSearchRequest();

		movementSearchRequest.setTenantId(movement.getTenantId());
		movementSearchRequest.setEmployeeId(movement.getEmployeeId());
		movementSearchRequest.setTypeOfmovement(movement.getTypeOfMovement().toString());
		final String queryStr = movementQueryBuilder.getQuery(movementSearchRequest, preparedStatementValues,
				requestInfo);
		return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), movementRowMapper);
	}
}