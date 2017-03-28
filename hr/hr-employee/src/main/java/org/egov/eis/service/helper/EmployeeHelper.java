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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.EmployeeResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHelper {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

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
		user.setTenantId(employeeRequest.getEmployee().getTenantId());
		userRequest.setUser(user);

		return userRequest;
	}

	public void populateData(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();

		employee.getAssignments().forEach((assignment) -> {
			assignment.setId(employeeRepository.generateSequence(Sequences.ASSIGNMENT.toString()));
			assignment.setTenantId(employee.getTenantId());
			assignment.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			assignment.setCreatedDate(new Date());
			assignment.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			assignment.setLastModifiedDate((new Date()));
			if (assignment.getHod() != null) {
				assignment.getHod().forEach((hod) -> {
					hod.setId(employeeRepository.generateSequence(Sequences.HODDEPARTMENT.toString()));
					hod.setTenantId(employee.getTenantId());
				});
			}
		});
		if (employee.getEducation() != null) {
			employee.getEducation().forEach((eduction) -> {
				eduction.setId(employeeRepository.generateSequence(Sequences.EDUCATIONALQUALIFICATION.toString()));
				eduction.setTenantId(employee.getTenantId());
				eduction.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				eduction.setCreatedDate(new Date());
				eduction.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				eduction.setLastModifiedDate((new Date()));
			});
		}
		if (employee.getTest() != null) {
			employee.getTest().forEach((test) -> {
				test.setId(employeeRepository.generateSequence(Sequences.DEPARTMENTALTEST.toString()));
				test.setTenantId(employee.getTenantId());
				test.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				test.setCreatedDate(new Date());
				test.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				test.setLastModifiedDate((new Date()));
			});
		}
		if (employee.getProbation() != null) {
			employee.getProbation().forEach((probation) -> {
				probation.setId(employeeRepository.generateSequence(Sequences.PROBATION.toString()));
				probation.setTenantId(employee.getTenantId());
				probation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				probation.setCreatedDate(new Date());
				probation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				probation.setLastModifiedDate((new Date()));
			});
		}
		if (employee.getRegularisation() != null) {
			employee.getRegularisation().forEach((regularisation) -> {
				regularisation.setId(employeeRepository.generateSequence(Sequences.REGULARISATION.toString()));
				regularisation.setTenantId(employee.getTenantId());
				regularisation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				regularisation.setCreatedDate(new Date());
				regularisation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				regularisation.setLastModifiedDate((new Date()));
			});
		}
		if (employee.getServiceHistory() != null) {
			employee.getServiceHistory().forEach((serviceHistory) -> {
				serviceHistory.setId(employeeRepository.generateSequence(Sequences.SERVICEHISTORY.toString()));
				serviceHistory.setTenantId(employee.getTenantId());
				serviceHistory.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				serviceHistory.setCreatedDate(new Date());
				serviceHistory.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				serviceHistory.setLastModifiedDate((new Date()));
			});
		}
		if (employee.getTechnical() != null) {
			employee.getTechnical().forEach((technical) -> {
				technical.setId(employeeRepository.generateSequence(Sequences.TECHNICALQUALIFICATION.toString()));
				technical.setTenantId(employee.getTenantId());
				technical.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
				technical.setCreatedDate(new Date());
				technical.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
				technical.setLastModifiedDate((new Date()));
			});
		}
	}

	public void mapDocumentsWithEmployees(List<EmployeeInfo> employeeInfoList,
			List<EmployeeDocument> employeeDocuments) {
		Map<Long, List<String>> employeeIdDocumentsMap = new HashMap<Long, List<String>>();

		for (EmployeeDocument employeeDocument : employeeDocuments) {
			if(employeeIdDocumentsMap.containsKey(employeeDocument.getEmployeeId())) {
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

	/**
	 * Populate EmployeeResponse object & returns ResponseEntity of type
	 * EmployeeResponse containing ResponseInfo & Employee objects
	 * 
	 * @param employee
	 * @param requestInfo
	 * @param headers
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(Employee employee, RequestInfo requestInfo) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		employeeResponse.setEmployee(employee);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		employeeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<EmployeeResponse>(employeeResponse, HttpStatus.OK);
	}
}