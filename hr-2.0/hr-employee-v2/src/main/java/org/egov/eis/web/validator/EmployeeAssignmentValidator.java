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

package org.egov.eis.web.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.bulk.Department;
import org.egov.eis.service.DepartmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.errorhandler.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EmployeeAssignmentValidator implements Validator {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationService designationService;

	/**
	 * This Validator validates *just* Employee instances
	 */
	@Override
	public boolean supports(Class<?> paramClass) {
		return Employee.class.equals(paramClass);
	}

	@Override
	public void validate(Object targetObject, Errors errors) {
		if (!(targetObject instanceof Employee)) {
			return;
		}

		Employee employee = (Employee) targetObject;
		List<Assignment> assignments = employee.getAssignments();

		/*
		 * if (isPassportvalid(employee)) { throw new
		 * InvalidDataException("passportno",
		 * "Should provide a valid Passport Number", "null"); }
		 */

		List<Assignment> primaryAssignments = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");

		List<Boolean> primaryList = assignments.stream().map(assignment -> assignment.getIsPrimary())
				.collect(Collectors.toList());
		if (!primaryList.contains(true)) {
			errors.rejectValue("employee.assignments", "invalid",
					"No Primary Assignment Found In Request. Please Enter At Least One Primary Assignment");
		}

		// Used to mark primary assignments for conveying the index of
		// assignment with errors
		List<Integer> primaryMarker = new ArrayList<>();
		for (int index = 0; index < assignments.size(); index++) {

			if (assignments.get(index).getFromDate().before(employee.getDateOfAppointment())) {
				throw new InvalidDataException("Assignment from Date and Employee  Date Of Appointment",
						"Assignment from Date should be Greater Than employee Date Of Appointment", "null");
			}

			RequestInfo requestInfo = new RequestInfo();
			RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
			String tenantId = employee.getTenantId();
			String department = assignments.get(index).getDepartment();

			List<Department> departments = Collections.singletonList(
					departmentService.getDepartment(tenantId, department, requestInfoWrapper.getRequestInfo()));
			
			if (CollectionUtils.isEmpty(departments)) {
				throw new InvalidDataException("department",
						"the field {0} should have a valid value which exists in the system", "null");
			}
            String deptDesig = assignments.get(index).getDesignation();
			if (null != assignments.get(index).getDesignation()) {
				DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
                        .codes(Arrays.asList(assignments.get(index).getDesignation())).tenantId(tenantId).build();
				List<org.egov.eis.model.bulk.Designation> designations = designationService
						.getDesignations(designationGetRequest, tenantId, requestInfoWrapper);

				if (designations == null || designations.size() < 1) {
					throw new InvalidDataException("designations",
							"the field {0} should have a valid value which exists in the system", "null");
				}
				// get a list of primary assignments
				if (assignments.get(index).getIsPrimary() != null && assignments.get(index).getIsPrimary()) {
					primaryAssignments.add(assignments.get(index));
					primaryMarker.add(index);
				}
				// check if fromDates are less than toDate
				if (assignments.get(index).getFromDate().compareTo(assignments.get(index).getToDate()) > 0) {
					// FIXME : the message can be fromDate cannot be greater
					// than toDate
					errors.rejectValue("employee.assignments[" + index + "]", "invalid",
							"Assignment's From Date " + dateFormat.format(assignments.get(index).getFromDate())
									+ " Can Not Be Greater Than To Date "
									+ dateFormat.format(assignments.get(index).getToDate())
									+ ". Please Enter Correct Dates");
				}
			}

			// check if assignmentDates are overlapping for primary assignments
			for (int i = 0; i < primaryAssignments.size(); i++) {
				for (int j = i + 1; j < primaryAssignments.size(); j++) {
					Long ith_fromDate = primaryAssignments.get(i).getFromDate().getTime();
					Long jth_fromDate = primaryAssignments.get(j).getFromDate().getTime();
					Long ith_toDate = primaryAssignments.get(i).getToDate().getTime();
					Long jth_toDate = primaryAssignments.get(j).getToDate().getTime();

					if (ith_fromDate >= jth_fromDate && ith_fromDate <= jth_toDate) {
						errors.rejectValue("employee.assignments[" + primaryMarker.get(i) + "].fromDate", "invalid",
								"Primary Assignment From Date " + dateFormat.format(new Date(ith_fromDate))
										+ " Is Overlapping With Dates " + dateFormat.format(new Date(jth_fromDate))
										+ " & " + dateFormat.format(new Date(jth_toDate))
										+ " Of Other Primary Assignment." + " Please Enter Correct Dates");
					}

					if (ith_toDate >= jth_fromDate && ith_toDate <= jth_toDate) {
						errors.rejectValue("employee.assignments[" + primaryMarker.get(i) + "].toDate", "invalid",
								"Primary Assignment To Date " + dateFormat.format(new Date(ith_toDate))
										+ " Is Overlapping With Dates " + dateFormat.format(new Date(jth_fromDate))
										+ " & " + dateFormat.format(new Date(jth_toDate))
										+ " Of Other Primary Assignment." + " Please Enter Correct Dates");
					}

					if (jth_fromDate >= ith_fromDate && jth_fromDate <= ith_toDate) {
						errors.rejectValue("employee.assignments[" + primaryMarker.get(j) + "].fromDate", "invalid",
								"Primary Assignment From Date " + dateFormat.format(new Date(jth_fromDate))
										+ " Is Overlapping With Dates " + dateFormat.format(new Date(ith_fromDate))
										+ " & " + dateFormat.format(new Date(ith_toDate))
										+ " Of Other Primary Assignment." + " Please Enter Correct Dates");
					}

					if (jth_toDate >= ith_fromDate && jth_toDate <= ith_toDate) {
						errors.rejectValue("employee.assignments[" + primaryMarker.get(j) + "].toDate", "invalid",
								"Primary Assignment To Date " + dateFormat.format(new Date(jth_toDate))
										+ " Is Overlapping With Dates " + dateFormat.format(new Date(ith_fromDate))
										+ " & " + dateFormat.format(new Date(ith_toDate))
										+ " Of Other Primary Assignment." + " Please Enter Correct Dates");
					}
				}
			}
		}

	}

	public boolean isPassportvalid(Employee emp) {
        return ((emp.getPassportNo() != null && emp.getPassportNo() != "") && !(emp.getPassportNo().matches("^[a-zA-Z][0-9]{7}$")));
	}
}