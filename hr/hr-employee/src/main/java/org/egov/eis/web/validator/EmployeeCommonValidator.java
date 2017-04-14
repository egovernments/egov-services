package org.egov.eis.web.validator;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.Map;

import org.egov.eis.model.Employee;
import org.egov.eis.model.enums.EntityType;
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
	
	public void validateDocumentsForNewEntity(List<String> docs, EntityType docType, String tenantId, Errors errors, int index) {
		if (isEmpty(docs))
			return;
		if (employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
						"egeis_employeeDocuments", "document", getDocumentsAsCSVs(docs), tenantId)) {
			errors.rejectValue("employee." + docType.toString().toLowerCase() + "[" + index + "].documents", "concurrent",
					"document(s) already exists");
		}
	}
	
	public void validateEntityId(Map<Long, Integer> idsMap, EntityType entityType, Long employeeId,
			String tenantId, Errors errors) {
		List<Long> idsFromDB = employeeRepository.getListOfIds(entityType.getDbTable(), employeeId, tenantId);
		idsMap.keySet().forEach((id) -> {
			if (!idsFromDB.contains(id))
				errors.rejectValue("employee." + entityType.getValue().toLowerCase() + "[" + idsMap.get(id) + "].id", "doesn't exist",
						" " +entityType.getValue().toLowerCase() + " doesn't exist for this employee");
		});
	}
	
	private String getDocumentsAsCSVs(List<String> documents) {
		return "'" + String.join("','", documents) + "'";
	}
}
