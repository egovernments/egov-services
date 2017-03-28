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

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAssignmentValidator implements Validator {

	/**
	 * This Validator validates *just* Employee instances
	 */
	@Override
	public boolean supports(Class<?> paramClass) {
		return Employee.class.equals(paramClass);
	}

	@Override
	public void validate(Object targetObject, Errors errors) {
		if (!(targetObject instanceof Employee))
			return;

		Employee employee = (Employee) targetObject;
		List<Assignment> assignments = employee.getAssignments();

		List<Assignment> primaryAssignments = new ArrayList<>();

		// Used to mark primary assignments for conveying the index of assignment with errors
		List<Integer> primaryMarker = new ArrayList<>();
		for(int index = 0; index < assignments.size(); index++) {
			// get a list of primary assignments
			if (assignments.get(index).getIsPrimary() != null && assignments.get(index).getIsPrimary()) {
				primaryAssignments.add(assignments.get(index));
				primaryMarker.add(index);
			}
			// check if fromDates are less than toDate
			if (assignments.get(index).getFromDate().compareTo(assignments.get(index).getToDate()) > 0) {
				// FIXME : the message can be fromDate cannot be greater than toDate
				errors.rejectValue("employee.assignments[" + index + "].fromDate", "invalid", "Invalid fromDate");
				errors.rejectValue("employee.assignments[" + index + "].toDate", "invalid", "Invalid toDate");
			}
		};

		// check if assignmentDates are overlapping for primary assignments
		for (int i = 0; i < primaryAssignments.size(); i++) {
			for (int j = i + 1; j < primaryAssignments.size(); j++) {
				if ((((primaryAssignments.get(i).getFromDate().compareTo(primaryAssignments.get(j).getFromDate()) >= 0)
						&& (primaryAssignments.get(i).getFromDate().compareTo(primaryAssignments.get(j).getToDate()) <= 0))
					|| ((primaryAssignments.get(i).getToDate().compareTo(primaryAssignments.get(j).getFromDate()) >= 0)
						&& (primaryAssignments.get(i).getToDate().compareTo(primaryAssignments.get(j).getToDate()) <= 0)))
					|| (((primaryAssignments.get(j).getFromDate().compareTo(primaryAssignments.get(i).getFromDate()) >= 0)
						&& (primaryAssignments.get(j).getFromDate().compareTo(primaryAssignments.get(i).getToDate()) <= 0))
					|| ((primaryAssignments.get(j).getToDate().compareTo(primaryAssignments.get(i).getFromDate()) >= 0)
						&& (primaryAssignments.get(j).getToDate().compareTo(primaryAssignments.get(i).getToDate()) <= 0)))) {
					errors.rejectValue("employee.assignments[" + primaryMarker.get(i) + "].fromDate", "overlapping",
						"Assignment Dates Overlapping");
					errors.rejectValue("employee.assignments[" + primaryMarker.get(i) + "].toDate", "overlapping",
						"Assignment Dates Overlapping");
					errors.rejectValue("employee.assignments[" + primaryMarker.get(j) + "].fromDate", "overlapping",
						"Assignment Dates Overlapping");
					errors.rejectValue("employee.assignments[" + primaryMarker.get(j) + "].toDate", "overlapping",
						"Assignment Dates Overlapping");
				}
			}
		}
	}
}