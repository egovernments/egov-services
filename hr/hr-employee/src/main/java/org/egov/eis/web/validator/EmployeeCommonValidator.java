package org.egov.eis.web.validator;

import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Has all validations that are common to create and update employee
 * @author ibm
 *
 */
@Component
public class EmployeeCommonValidator {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public void validateEmployee(Employee employee, Errors errors) {
		if (employee.getRetirementAge() != null && employee.getRetirementAge() > 100)
			errors.rejectValue("employee.retirementAge", "invalid", "Invalid retirementAge");

		if ((employee.getDateOfAppointment() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfAppointment().after(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfAppointment", "invalid", "Invalid dateOfAppointment");
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
		}
		if ((employee.getDateOfResignation() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfResignation().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfResignation", "invalid", "Invalid dateOfResignation");
		}
		if ((employee.getDateOfTermination() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfTermination().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfTermination", "invalid", "Invalid dateOfTermination");
		}
		if ((employee.getDateOfRetirement() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfRetirement().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfRetirement", "invalid", "Invalid dateOfRetirement");
		}
	}
	
	public void validateDocumentsForNewAssignment(Assignment assignment, Errors errors, int index) {
		if (assignment.getDocuments() != null && !assignment.getDocuments().isEmpty()
				&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
						"egeis_employeeDocuments", "document", getDocumentsAsCSVs(assignment.getDocuments()))) {
			errors.rejectValue("employee.assignments[" + index + "].documents", "concurrent",
					"document(s) already exists");
		}
	}
	
	private String getDocumentsAsCSVs(List<String> documents) {
		return "'" + String.join("','", documents) + "'";
	}
	
	/**
	 * Checks if the given string is present in db for the given column and given table.
	 */
	public Boolean duplicateExists(String table, String column, String value, Long id, String tenantId) {
		Long idFromDb = employeeRepository.getId(table, column, value, tenantId);
		if (idFromDb == 0 || (id != null && id.equals(idFromDb)))
			return false;
		return true;
	}

}
