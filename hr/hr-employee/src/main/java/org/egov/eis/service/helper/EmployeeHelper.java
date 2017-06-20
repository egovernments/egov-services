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

import org.egov.eis.model.*;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EmployeeHelper {

	@Autowired
	private EmployeeRepository employeeRepository;

	private enum Sequences {
		ASSIGNMENT("seq_egeis_assignment"),
		DEPARTMENTALTEST("seq_egeis_departmentaltest"),
		EDUCATIONALQUALIFICATION("seq_egeis_educationalqualification"),
		HODDEPARTMENT("seq_egeis_hoddepartment"),
		PROBATION("seq_egeis_probation"),
		REGULARISATION("seq_egeis_regularisation"),
		SERVICEHISTORY("seq_egeis_servicehistory"),
		TECHNICALQUALIFICATION("seq_egeis_technicalqualification");

		String sequenceName;

		private Sequences(String sequenceName) {
			this.sequenceName = sequenceName;
		}

		@Override
		public String toString() {
			return sequenceName;
		}
	}

	public String getEmployeeCode(Long id) {
		return "EMP" + id;
	}

	public UserRequest getUserRequest(EmployeeRequest employeeRequest) {
		UserRequest userRequest = new UserRequest();
		userRequest.setRequestInfo(employeeRequest.getRequestInfo());
		User user = employeeRequest.getEmployee().getUser();
		// FIXME : password hard-coded
		user.setPassword("12345678");
		user.setTenantId(employeeRequest.getEmployee().getTenantId());
		userRequest.setUser(user);

		return userRequest;
	}

	public void populateDefaultDataForCreate(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		String requesterId = requestInfo.getRequesterId();
		String tenantId = employee.getTenantId();

		populateDefaultDataForNewEmployee(employee, requesterId);

		employee.getAssignments().forEach((assignment) -> {
			populateDefaultDataForNewAssignment(assignment, requesterId, tenantId);
		});
		if (employee.getEducation() != null) {
			employee.getEducation().forEach((education) -> {
				populateDefaultDataForNewEducation(education, requesterId, tenantId);
			});
		}
		if (employee.getTest() != null) {
			employee.getTest().forEach((test) -> {
				populateDefaultDataForNewTest(test, requesterId, tenantId);
			});
		}
		if (employee.getProbation() != null) {
			employee.getProbation().forEach((probation) -> {
				populateDefaultDataForNewProbation(probation, requesterId, tenantId);
			});
		}
		if (employee.getRegularisation() != null) {
			employee.getRegularisation().forEach((regularisation) -> {
				populateDefaultDataForNewRegularisation(regularisation, requesterId, tenantId);
			});
		}
		if (employee.getServiceHistory() != null) {
			employee.getServiceHistory().forEach((serviceHistory) -> {
				populateDefaultDataForNewServiceHistory(serviceHistory, requesterId, tenantId);
			});
		}
		if (employee.getTechnical() != null) {
			employee.getTechnical().forEach((technical) -> {
				populateDefaultDataForNewTechnical(technical, requesterId, tenantId);
			});
		}
	}

	public void populateDefaultDataForUpdate(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		String requesterId = requestInfo.getRequesterId();
		// FIXME : Check - will allow us to set updated tenantId for every object, if employee tenantId is updated
		String tenantId = employee.getTenantId();

		employee.setLastModifiedBy(Long.parseLong(requesterId));
		employee.setLastModifiedDate((new Date()));

		employee.getAssignments().forEach((assignment) -> {
			if (assignment.getId() == null)
				populateDefaultDataForNewAssignment(assignment, requesterId, tenantId);
			else
				populateDefaultDataForUpdateAssignment(assignment, requesterId, tenantId);
		});
		if (employee.getEducation() != null) {
			employee.getEducation().forEach((education) -> {
				if (education.getId() == null)
					populateDefaultDataForNewEducation(education, requesterId, tenantId);
				else {
					education.setLastModifiedBy(Long.parseLong(requesterId));
					education.setLastModifiedDate((new Date()));
				}
			});
		}
		if (employee.getTest() != null) {
			employee.getTest().forEach((test) -> {
				if (test.getId() == null)
					populateDefaultDataForNewTest(test, requesterId, tenantId);
				else {
					test.setLastModifiedBy(Long.parseLong(requesterId));
					test.setLastModifiedDate((new Date()));
				}
			});
		}
		if (employee.getProbation() != null) {
			employee.getProbation().forEach((probation) -> {
				if (probation.getId() == null)
					populateDefaultDataForNewProbation(probation, requesterId, tenantId);
				else {
					probation.setLastModifiedBy(Long.parseLong(requesterId));
					probation.setLastModifiedDate((new Date()));
				}
			});
		}
		if (employee.getRegularisation() != null) {
			employee.getRegularisation().forEach((regularisation) -> {
				if (regularisation.getId() == null)
					populateDefaultDataForNewRegularisation(regularisation, requesterId, tenantId);
				else {
					regularisation.setLastModifiedBy(Long.parseLong(requesterId));
					regularisation.setLastModifiedDate((new Date()));
				}
			});
		}
		if (employee.getServiceHistory() != null) {
			employee.getServiceHistory().forEach((serviceHistory) -> {
				if (serviceHistory.getId() == null)
					populateDefaultDataForNewServiceHistory(serviceHistory, requesterId, tenantId);
				else {
					serviceHistory.setLastModifiedBy(Long.parseLong(requesterId));
					serviceHistory.setLastModifiedDate((new Date()));
				}
			});
		}
		if (employee.getTechnical() != null) {
			employee.getTechnical().forEach((technical) -> {
				if (technical.getId() == null)
					populateDefaultDataForNewTechnical(technical, requesterId, tenantId);
				else {
					technical.setLastModifiedBy(Long.parseLong(requesterId));
					technical.setLastModifiedDate((new Date()));
				}
			});
		}
	}

	private void populateDefaultDataForNewEmployee(Employee employee, String requesterId) {
		employee.setCreatedBy(Long.parseLong(requesterId));
		employee.setCreatedDate(new Date());
		employee.setLastModifiedBy(Long.parseLong(requesterId));
		employee.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewTechnical(TechnicalQualification technical, String requesterId,
			String tenantId) {
		technical.setId(employeeRepository.generateSequence(Sequences.TECHNICALQUALIFICATION.toString()));
		technical.setTenantId(tenantId);
		technical.setCreatedBy(Long.parseLong(requesterId));
		technical.setCreatedDate(new Date());
		technical.setLastModifiedBy(Long.parseLong(requesterId));
		technical.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewServiceHistory(ServiceHistory serviceHistory, String requesterId,
			String tenantId) {
		serviceHistory.setId(employeeRepository.generateSequence(Sequences.SERVICEHISTORY.toString()));
		serviceHistory.setTenantId(tenantId);
		serviceHistory.setCreatedBy(Long.parseLong(requesterId));
		serviceHistory.setCreatedDate(new Date());
		serviceHistory.setLastModifiedBy(Long.parseLong(requesterId));
		serviceHistory.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewRegularisation(Regularisation regularisation, String requesterId,
			String tenantId) {
		regularisation.setId(employeeRepository.generateSequence(Sequences.REGULARISATION.toString()));
		regularisation.setTenantId(tenantId);
		regularisation.setCreatedBy(Long.parseLong(requesterId));
		regularisation.setCreatedDate(new Date());
		regularisation.setLastModifiedBy(Long.parseLong(requesterId));
		regularisation.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewProbation(Probation probation, String requesterId, String tenantId) {
		probation.setId(employeeRepository.generateSequence(Sequences.PROBATION.toString()));
		probation.setTenantId(tenantId);
		probation.setCreatedBy(Long.parseLong(requesterId));
		probation.setCreatedDate(new Date());
		probation.setLastModifiedBy(Long.parseLong(requesterId));
		probation.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewTest(DepartmentalTest test, String requesterId, String tenantId) {
		test.setId(employeeRepository.generateSequence(Sequences.DEPARTMENTALTEST.toString()));
		test.setTenantId(tenantId);
		test.setCreatedBy(Long.parseLong(requesterId));
		test.setCreatedDate(new Date());
		test.setLastModifiedBy(Long.parseLong(requesterId));
		test.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewEducation(EducationalQualification education, String requesterId,
			String tenantId) {
		education.setId(employeeRepository.generateSequence(Sequences.EDUCATIONALQUALIFICATION.toString()));
		education.setTenantId(tenantId);
		education.setCreatedBy(Long.parseLong(requesterId));
		education.setCreatedDate(new Date());
		education.setLastModifiedBy(Long.parseLong(requesterId));
		education.setLastModifiedDate((new Date()));
	}

	private void populateDefaultDataForNewAssignment(Assignment assignment, String requesterId, String tenantId) {
		assignment.setId(employeeRepository.generateSequence(Sequences.ASSIGNMENT.toString()));
		assignment.setTenantId(tenantId);
		assignment.setCreatedBy(Long.parseLong(requesterId));
		assignment.setCreatedDate(new Date());
		assignment.setLastModifiedBy(Long.parseLong(requesterId));
		assignment.setLastModifiedDate((new Date()));
		if (assignment.getHod() != null) {
			assignment.getHod().forEach((hod) -> {
				hod.setId(employeeRepository.generateSequence(Sequences.HODDEPARTMENT.toString()));
				hod.setTenantId(tenantId);
			});
		}
	}

	private void populateDefaultDataForUpdateAssignment(Assignment assignment, String requesterId, String tenantId) {
		assignment.setLastModifiedBy(Long.parseLong(requesterId));
		assignment.setLastModifiedDate((new Date()));
		// trimTimePartFromDateFields(assignment);
		if (assignment.getDocuments() == null) {
			assignment.setDocuments(new ArrayList<>());
		}
		if (assignment.getHod() != null) {
			assignment.getHod().forEach((hod) -> {
				if (hod.getId() == null)
					hod.setId(employeeRepository.generateSequence(Sequences.HODDEPARTMENT.toString()));
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
}