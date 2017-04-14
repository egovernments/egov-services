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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DataIntegrityValidatorForUpdate implements Validator {

	@Autowired
	private EmployeeCommonValidator employeeCommonValidator;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public boolean supports(Class<?> paramClass) {
		return Employee.class.equals(paramClass);
	}

	@Override
	public void validate(Object targetObject, Errors errors) {
		if (!(targetObject instanceof Employee))
			return;

		Employee employee = (Employee) targetObject;
		Long employeeId = employee.getId();
		String tenantId = employee.getTenantId();

		// FIXME employee.getId == null or empty then throw error employee id is
		// required
		if (isEmpty(employeeId)) {
			errors.rejectValue("employee.id", "no value", "provide employee id for update");
			return;
		}
		
		if (!employeeRepository.checkIfEmployeeExists(employeeId, tenantId)) {
			// FIXME throw error employee id does not exist
			errors.rejectValue("employee.id", "no value present", "employee id doesn't exist");
			return;
		}

		validateEmployee(employee, errors);
		validateAssignments(employee.getAssignments(), employeeId, tenantId, errors);
		validateDepartmentalTest(employee.getTest(), employeeId, tenantId, errors);
		validateEducationalQualification(employee.getEducation(), employeeId, tenantId, errors);
		validateProbation(employee.getProbation(), employeeId, tenantId, errors);
		validateRegularisation(employee.getRegularisation(), employeeId, tenantId, errors);
		validateServiceHistory(employee.getServiceHistory(), employeeId, tenantId, errors);
		validateTechnicalQualification(employee.getTechnical(), employeeId, tenantId, errors);
	}

	private void validateExternalAPIData() {
	}

	private void validateEmployee(Employee employee, Errors errors) {
		// FIXME call common validator.validateEmployee
		employeeCommonValidator.validateEmployee(employee, errors);

		if ((employee.getPassportNo() != null) && duplicateExists("egeis_employee", "passportNo",
				employee.getPassportNo(), employee.getId(), employee.getTenantId())) {
			errors.rejectValue("employee.passportNo", "concurrent", "passportNo already exists");
		}

		if ((employee.getGpfNo() != null) && duplicateExists("egeis_employee", "gpfNo", employee.getGpfNo(),
				employee.getId(), employee.getTenantId())) {
			errors.rejectValue("employee.gpfNo", "concurrent", "gpfNo already exists");
		}

		// check if documents dont belong to any other employee/referencetype. return error

		/*if (employee.getDocuments() != null && !employee.getDocuments().isEmpty()
			&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSVForUpdate("egeis_employeeDocuments", "document",
						getDocumentsAsCSVs(employee.getDocuments()), EntityType.EMPLOYEE_HEADER, employee.getId(), employee.getTenantId())) {
			errors.rejectValue("employee.documents", "concurrent", "document(s) already exists");
		}
*/
		/*
		 * if ((employee.getDocuments() != null) &&
		 * !employee.getDocuments().isEmpty() &&
		 * employeeService.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(employee.getDocuments()))) {
		 * errors.rejectValue("employee.documents", "concurrent",
		 * "document(s) already exists"); }
		 */
	}

	private void validateAssignments(List<Assignment> assignments, Long employeeId, String tenantId, Errors errors) {
		validateIdsForAssignment(assignments, employeeId, tenantId, errors);
		
		for (int index = 0; index < assignments.size(); index++) {
			// employeeCommonValidator.validateDocumentsForNewAssignment(assignments.get(index),
			// errors, index);
		}
	}

	private void validateIdsForAssignment(List<Assignment> assignments, Long employeeId, String tenantId, Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < assignments.size(); index++) {
			if (assignments.get(index).getId() != null) // FIXME check if long gets default value of 0L 
				idsMap.put(assignments.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.ASSIGNMENT, employeeId, tenantId, errors);	
	}

	private void validateDepartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
			Errors errors) {
		if (isEmpty(tests))
			return;
		validateIdsForDepaartmentalTest(tests, employeeId, tenantId, errors);
	
		/*
		 * if (tests.get(index).getDocuments() != null &&
		 * !tests.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(tests.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.test[" + index + "].documents",
		 * "concurrent", "document(s) already exists"); }
		 */
		
	}

	private void validateIdsForDepaartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
			Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < tests.size(); index++) {
			if (tests.get(index).getId() != null)
				idsMap.put(tests.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.TEST, employeeId, tenantId, errors);		
	}

	private void validateEducationalQualification(List<EducationalQualification> educations, Long employeeId,
			String tenantId, Errors errors) {
		if (isEmpty(educations))
			return;
		validateIdsForEducationalQualification(educations, employeeId, tenantId, errors);
	
		/*
		 * if (educations.get(index).getDocuments() != null &&
		 * !educations.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(educations.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.education[" + index + "].documents",
		 * "concurrent", "document(s) already exists"); }
		 */
	}

	private void validateIdsForEducationalQualification(List<EducationalQualification> educations, Long employeeId,
			String tenantId, Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < educations.size(); index++) {
			if (educations.get(index).getId() != null)
				idsMap.put(educations.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.EDUCATION, employeeId, tenantId, errors);		
	}

	private void validateProbation(List<Probation> probations, Long employeeId, String tenantId, Errors errors) {
		if (isEmpty(probations))
			return;
		validateIdsForProbation(probations, employeeId, tenantId, errors);
		/*
		 * if (probations.get(index).getDocuments() != null &&
		 * !probations.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(probations.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.probation[" + index + "].documents",
		 * "concurrent", "document(s) already exists"); }
		 */
	}

	private void validateIdsForProbation(List<Probation> probations, Long employeeId, String tenantId, Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < probations.size(); index++) {
			if (probations.get(index).getId() != null)
				idsMap.put(probations.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.PROBATION, employeeId, tenantId, errors);		
	}

	private void validateRegularisation(List<Regularisation> regularisations, Long employeeId, String tenantId,
			Errors errors) {
		if (isEmpty(regularisations))
			return;
		validateIdsForRegularisation(regularisations, employeeId, tenantId, errors);
		/*
		 * if (regularisations.get(index).getDocuments() != null &&
		 * !regularisations.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(regularisations.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.regularisation[" + index +
		 * "].documents", "concurrent", "document(s) already exists"); }
		 */
	}

	private void validateIdsForRegularisation(List<Regularisation> regularisations, Long employeeId, String tenantId,
			Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < regularisations.size(); index++) {
			if (regularisations.get(index).getId() != null)
				idsMap.put(regularisations.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.REGULARISATION, employeeId, tenantId, errors);		
	}

	private void validateServiceHistory(List<ServiceHistory> serviceHistories, Long employeeId, String tenantId,
			Errors errors) {
		if (isEmpty(serviceHistories))
			return;
		validateIdsForServiceHistory(serviceHistories, employeeId, tenantId, errors);
		/*
		 * if (histories.get(index).getDocuments() != null &&
		 * !histories.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(histories.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.serviceHistory[" + index +
		 * "].documents", "concurrent", "document(s) already exists"); }
		 */
	}

	private void validateIdsForServiceHistory(List<ServiceHistory> serviceHistories, Long employeeId, String tenantId,
			Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < serviceHistories.size(); index++) {
			if (serviceHistories.get(index).getId() != null)
				idsMap.put(serviceHistories.get(index).getId(), index);
		
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.SERVICE, employeeId, tenantId, errors);		
	}

	private void validateTechnicalQualification(List<TechnicalQualification> technicals, Long employeeId,
			String tenantId, Errors errors) {
		if (isEmpty(technicals))
			return;
		validateIdsForTechnicalQualification(technicals, employeeId, tenantId, errors);
		/*
		 * if (technicals.get(index).getDocuments() != null &&
		 * !technicals.get(index).getDocuments().isEmpty() &&
		 * employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
		 * "egeis_employeeDocuments", "document",
		 * getDocumentsAsCSVs(technicals.get(index).getDocuments()))) {
		 * errors.rejectValue("employee.technical[" + index + "].documents",
		 * "concurrent", "document(s) already exists"); }
		 */
	}

	private void validateIdsForTechnicalQualification(List<TechnicalQualification> technicals, Long employeeId,
			String tenantId, Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < technicals.size(); index++) {
			if (technicals.get(index).getId() != null)
				idsMap.put(technicals.get(index).getId(), index);
		}
		if (!idsMap.isEmpty())
			employeeCommonValidator.validateEntityId(idsMap, EntityType.TECHNICAL, employeeId, tenantId, errors);		
	}

	private String getDocumentsAsCSVs(List<String> documents) {
		return "'" + String.join("','", documents) + "'";
	}

	/**
	 * Checks if the given string is present in db for the given column and
	 * given table.
	 * 
	 * @param table
	 * @param column
	 * @param value
	 * @param id
	 * @return
	 */
	public Boolean duplicateExists(String table, String column, String value, Long id, String tenantId) {
		Long idFromDb = employeeRepository.getId(table, column, value, tenantId);
		if (idFromDb == 0 || id.equals(idFromDb))
			return false;
		return true;
	}
}