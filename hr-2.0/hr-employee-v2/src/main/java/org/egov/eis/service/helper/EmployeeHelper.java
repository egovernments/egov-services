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

package org.egov.eis.service.helper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.APRDetail;
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
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHelper {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	private enum Sequences {
		ASSIGNMENT("seq_egeis_assignment"),
		DEPARTMENTAL_TEST("seq_egeis_departmentalTest"),
		EDUCATIONAL_QUALIFICATION("seq_egeis_educationalQualification"),
		HOD_DEPARTMENT("seq_egeis_hodDepartment"),
		PROBATION("seq_egeis_probation"),
		REGULARISATION("seq_egeis_regularisation"),
		SERVICE_HISTORY("seq_egeis_serviceHistory"),
		TECHNICAL_QUALIFICATION("seq_egeis_technicalQualification"),
		APR_DETAILS("seq_egeis_aprDetails");

		String sequenceName;

		private Sequences(String sequenceName) {
			this.sequenceName = sequenceName;
		}

		@Override
		public String toString() {
			return sequenceName;
		}
	}

	public UserRequest getUserRequest(EmployeeRequest employeeRequest) {
		UserRequest userRequest = new UserRequest();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		userRequest.setRequestInfo(requestInfo);
		User user = employeeRequest.getEmployee().getUser();
		user.setTenantId(employeeRequest.getEmployee().getTenantId());
		userRequest.setUser(user);

		return userRequest;
	}

	public void populateDefaultDataForCreate(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		Long requesterId = requestInfo.getUserInfo().getId();
		String tenantId = employeeRequest.getEmployee().getTenantId();

		populateDefaultDataForNewEmployee(employee, requesterId, tenantId);

		employee.getAssignments().forEach((assignment) -> {
			populateDefaultDataForNewAssignment(assignment, requesterId, tenantId);
		});
		if (!isEmpty(employee.getEducation())) {
			employee.getEducation().forEach((education) -> {
				populateDefaultDataForNewEducation(education, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getTest())) {
			employee.getTest().forEach((test) -> {
				populateDefaultDataForNewTest(test, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getProbation())) {
			employee.getProbation().forEach((probation) -> {
				populateDefaultDataForNewProbation(probation, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getRegularisation())) {
			employee.getRegularisation().forEach((regularisation) -> {
				populateDefaultDataForNewRegularisation(regularisation, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getServiceHistory())) {
			mapAssignmentReferenceToServiceHistory(employeeRequest);
			employee.getServiceHistory().forEach((serviceHistory) -> {
				populateDefaultDataForNewServiceHistory(serviceHistory, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getTechnical())) {
			employee.getTechnical().forEach((technical) -> {
				populateDefaultDataForNewTechnical(technical, requesterId, tenantId);
			});
		}
		if (!isEmpty(employee.getAprDetails())) {
			employee.getAprDetails().forEach((aprDetail) -> {
				populateDefaultDataForNewAPRDetail(aprDetail, requesterId, tenantId);
			});
		}
	}

	public void populateDefaultDataForUpdate(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		Long requesterId = requestInfo.getUserInfo().getId();
		// FIXME : Check - will allow us to set updated tenantId for every new object, if requester tenantId is updated
		String tenantId = employeeRequest.getEmployee().getTenantId();

		employee.setLastModifiedBy(requesterId);
		employee.setLastModifiedDate((new Date()));

		employee.getAssignments().forEach((assignment) -> {
			if (isEmpty(assignment.getId()))
				populateDefaultDataForNewAssignment(assignment, requesterId, tenantId);
			else
				populateDefaultDataForUpdateAssignment(assignment, requesterId, tenantId);
		});
		if (!isEmpty(employee.getEducation())) {
			employee.getEducation().forEach((education) -> {
				if (isEmpty(education.getId()))
					populateDefaultDataForNewEducation(education, requesterId, tenantId);
				else {
					education.setLastModifiedBy(requesterId);
					education.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getTest())) {
			employee.getTest().forEach((test) -> {
				if (isEmpty(test.getId()))
					populateDefaultDataForNewTest(test, requesterId, tenantId);
				else {
					test.setLastModifiedBy(requesterId);
					test.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getProbation())) {
			employee.getProbation().forEach((probation) -> {
				if (isEmpty(probation.getId()))
					populateDefaultDataForNewProbation(probation, requesterId, tenantId);
				else {
					probation.setLastModifiedBy(requesterId);
					probation.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getRegularisation())) {
			employee.getRegularisation().forEach((regularisation) -> {
				if (isEmpty(regularisation.getId()))
					populateDefaultDataForNewRegularisation(regularisation, requesterId, tenantId);
				else {
					regularisation.setLastModifiedBy(requesterId);
					regularisation.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getServiceHistory())) {
			mapAssignmentReferenceToServiceHistory(employeeRequest);
			employee.getServiceHistory().forEach((serviceHistory) -> {
				if (isEmpty(serviceHistory.getId()))
					populateDefaultDataForNewServiceHistory(serviceHistory, requesterId, tenantId);
				else {
					serviceHistory.setLastModifiedBy(requesterId);
					serviceHistory.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getTechnical())) {
			employee.getTechnical().forEach((technical) -> {
				if (isEmpty(technical.getId()))
					populateDefaultDataForNewTechnical(technical, requesterId, tenantId);
				else {
					technical.setLastModifiedBy(requesterId);
					technical.setLastModifiedDate((new Date()));
				}
			});
		}
		if (!isEmpty(employee.getAprDetails())) {
			employee.getAprDetails().forEach((aprDetail) -> {
				if (isEmpty(aprDetail.getId()))
					populateDefaultDataForNewAPRDetail(aprDetail, requesterId, tenantId);
				else {
					aprDetail.setLastModifiedBy(requesterId);
					aprDetail.setLastModifiedDate((new Date()));
				}
			});
		}
	}

	private void populateDefaultDataForNewEmployee(Employee employee, Long requesterId, String tenantId) {
		employee.setTenantId(tenantId);
		employee.setCreatedBy(requesterId);
		employee.setCreatedDate(new Date());
		employee.setLastModifiedBy(requesterId);
		employee.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewAPRDetail(APRDetail aprDetail, Long requesterId, String tenantId) {
		aprDetail.setId(employeeRepository.generateSequence(Sequences.APR_DETAILS.toString()));
		aprDetail.setTenantId(tenantId);
		aprDetail.setCreatedBy(requesterId);
		aprDetail.setCreatedDate(new Date());
		aprDetail.setLastModifiedBy(requesterId);
		aprDetail.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewTechnical(TechnicalQualification technical, Long requesterId,
													String tenantId) {
		technical.setId(employeeRepository.generateSequence(Sequences.TECHNICAL_QUALIFICATION.toString()));
		technical.setTenantId(tenantId);
		technical.setCreatedBy(requesterId);
		technical.setCreatedDate(new Date());
		technical.setLastModifiedBy(requesterId);
		technical.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewServiceHistory(ServiceHistory serviceHistory, Long requesterId,
														 String tenantId) {
		serviceHistory.setId(employeeRepository.generateSequence(Sequences.SERVICE_HISTORY.toString()));
		serviceHistory.setTenantId(tenantId);
		serviceHistory.setCreatedBy(requesterId);
		serviceHistory.setCreatedDate(new Date());
		serviceHistory.setLastModifiedBy(requesterId);
		serviceHistory.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewRegularisation(Regularisation regularisation, Long requesterId,
														 String tenantId) {
		regularisation.setId(employeeRepository.generateSequence(Sequences.REGULARISATION.toString()));
		regularisation.setTenantId(tenantId);
		regularisation.setCreatedBy(requesterId);
		regularisation.setCreatedDate(new Date());
		regularisation.setLastModifiedBy(requesterId);
		regularisation.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewProbation(Probation probation, Long requesterId, String tenantId) {
		probation.setId(employeeRepository.generateSequence(Sequences.PROBATION.toString()));
		probation.setTenantId(tenantId);
		probation.setCreatedBy(requesterId);
		probation.setCreatedDate(new Date());
		probation.setLastModifiedBy(requesterId);
		probation.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewTest(DepartmentalTest test, Long requesterId, String tenantId) {
		test.setId(employeeRepository.generateSequence(Sequences.DEPARTMENTAL_TEST.toString()));
		test.setTenantId(tenantId);
		test.setCreatedBy(requesterId);
		test.setCreatedDate(new Date());
		test.setLastModifiedBy(requesterId);
		test.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewEducation(EducationalQualification education, Long requesterId,
													String tenantId) {
		education.setId(employeeRepository.generateSequence(Sequences.EDUCATIONAL_QUALIFICATION.toString()));
		education.setTenantId(tenantId);
		education.setCreatedBy(requesterId);
		education.setCreatedDate(new Date());
		education.setLastModifiedBy(requesterId);
		education.setLastModifiedDate((new Date()));
	}

	public void populateDefaultDataForNewAssignment(Assignment assignment, Long requesterId, String tenantId) {
		assignment.setId(employeeRepository.generateSequence(Sequences.ASSIGNMENT.toString()));
		assignment.setTenantId(tenantId);
		assignment.setCreatedBy(requesterId);
		assignment.setCreatedDate(new Date());
		assignment.setLastModifiedBy(requesterId);
		assignment.setLastModifiedDate((new Date()));
		if (!isEmpty(assignment.getHod())) {
			assignment.getHod().forEach((hod) -> {
				hod.setId(employeeRepository.generateSequence(Sequences.HOD_DEPARTMENT.toString()));
				hod.setTenantId(tenantId);
			});
		}
	}

	public void populateDefaultDataForUpdateAssignment(Assignment assignment, Long requesterId, String tenantId) {
		assignment.setLastModifiedBy(requesterId);
		assignment.setLastModifiedDate((new Date()));
		// trimTimePartFromDateFields(assignment);
		if (isEmpty(assignment.getDocuments())) {
			assignment.setDocuments(new ArrayList<>());
		}
		if (!isEmpty(assignment.getHod())) {
			assignment.getHod().forEach((hod) -> {
				if (isEmpty(hod.getId()))
					hod.setId(employeeRepository.generateSequence(Sequences.HOD_DEPARTMENT.toString()));
				hod.setTenantId(tenantId);
			});
		}
	}

	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public void mapDocumentsWithEmployees(List<EmployeeInfo> employeeInfoList,
										  List<EmployeeDocument> employeeDocuments) {
		Map<Long, List<String>> employeeIdDocumentsMap = new HashMap<Long, List<String>>();

		for (EmployeeDocument employeeDocument : employeeDocuments) {
			if (employeeIdDocumentsMap.containsKey(employeeDocument.getEmployeeId())) {
				employeeIdDocumentsMap.get(employeeDocument.getEmployeeId()).add(employeeDocument.getDocument());
			} else {
				List<String> documentList = new ArrayList<>();
				documentList.add(employeeDocument.getDocument());
				employeeIdDocumentsMap.put(employeeDocument.getEmployeeId(), documentList);
			}
		}

		for (EmployeeInfo employeeInfo : employeeInfoList) {
			if (employeeIdDocumentsMap.containsKey(employeeInfo.getId())) {
				List<String> documents = employeeIdDocumentsMap.get(employeeInfo.getId());
				employeeInfo.setDocuments(documents);
			}
		}
	}

	public void mapAssignmentReferenceToServiceHistory(EmployeeRequest employeeRequest){
		List<Assignment> assignments = employeeRequest.getEmployee().getAssignments().stream().filter(assignment -> assignment.getIsPrimary().equals(true)).collect(Collectors.toList());

		assignments.stream().forEach(assign -> {
			employeeRequest.getEmployee().getServiceHistory().stream().filter(history -> history.getIsAssignmentBased().equals(true)).forEach(srvcHstry -> {
				if (srvcHstry.getServiceFrom().equals(assign.getFromDate()) && srvcHstry.getServiceTo().equals(assign.getToDate())) {
					srvcHstry.setAssignmentId(assign.getId());
				}
			});

		});

	}
}