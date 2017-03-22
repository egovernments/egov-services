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
	 * This Validator validates *just* Person instances
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