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
import org.egov.eis.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DataIntegrityValidatorForUpdate implements Validator {

	private enum DBTables {
		ASSIGNMENT("egeis_assignment"), HODDEPARTMENT("egeis_hoddepartment"), EDUCATIONALQUALIFICATION(
				"egeis_educationalqualification"), DEPARTMENTALTEST("egeis_departmentaltest"), PROBATION(
						"egeis_probation"), REGULARISATION("egeis_regularisation"), SERVICEHISTORY(
								"egeis_servicehistory"), TECHNICALQUALIFICATION("egeis_technicalqualification");

		String dbTableName;

		private DBTables(String dbTableName) {
			this.dbTableName = dbTableName;
		}

		@Override
		public String toString() {
			return dbTableName;
		}
	}

	@Autowired
	private EmployeeCommonValidator employeeCommonValidator;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	/**
	 * This Validator validates *just* Employee instances
	 */
	@Override
	public boolean supports(Class<?> paramClass) {
		return Employee.class.equals(paramClass);
	}

	// FIXME Table names and column names are hard-coded
	@Override
	public void validate(Object targetObject, Errors errors) {
		if (!(targetObject instanceof Employee))
			return;

		Employee employee = (Employee) targetObject;
		String tenantId = employee.getTenantId();

		Long employeeId = null;
		if (employee.getId() != null && employeeRepository.checkIfEmployeeExists(employee.getId(), tenantId)) {
			employeeId = employee.getId();
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

	// FIXME Validate data existence of Religion, Languages etc. for every data
	// in separate methods
	private void validateExternalAPIData() {

	}

	private void validateEmployee(Employee employee, Errors errors) {
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

		if ((employee.getPassportNo() != null) && duplicateExists("egeis_employee", "passportNo",
				employee.getPassportNo(), employee.getId(), employee.getTenantId())) {
			errors.rejectValue("employee.passportNo", "concurrent", "passportNo already exists");
		}

		if ((employee.getGpfNo() != null) && duplicateExists("egeis_employee", "gpfNo",
				employee.getGpfNo(), employee.getId(), employee.getTenantId())) {
			errors.rejectValue("employee.gpfNo", "concurrent", "gpfNo already exists");
		}
		
		

		/*if ((employee.getDocuments() != null) && !employee.getDocuments().isEmpty()
				&& employeeService.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
						getDocumentsAsCSVs(employee.getDocuments()))) {
			errors.rejectValue("employee.documents", "concurrent", "document(s) already exists");
		}*/
	}

	private void validateAssignments(List<Assignment> assignments, Long employeeId, String tenantId, Errors errors) {
		Map<Long, Integer> idsMap = new HashMap<>();
		for (int index = 0; index < assignments.size(); index++) {
			if (employeeId != null && assignments.get(index).getId() != null)
				idsMap.put(assignments.get(index).getId(), index);

			//employeeCommonValidator.validateDocumentsForNewAssignment(assignments.get(index), errors, index);
		}
		//if (!idsMap.isEmpty())
			// validateInvalidIdForAssignment(idsMap, employeeId, tenantId, errors);
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param idsMap
	 * @param employeeId
	 * @param tenantId
	 * @param errors
	 */
	private void validateInvalidIdForAssignment(Map<Long, Integer> idsMap, Long employeeId, String tenantId, Errors errors) {
		List<Long> idsFromDB = employeeRepository.getListOfIds(DBTables.ASSIGNMENT.toString(), employeeId, tenantId);

		idsFromDB.forEach((id) -> {
			if (!idsMap.containsKey(id))
				errors.rejectValue("employee.assignments[" + idsMap.get(id) + "].id", "doesn't exist",
						"assignments doesn't already exist for this employee");
		});
	}

	private void validateDepartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
			Errors errors) {
		if (tests != null && !tests.isEmpty()) {
			for (int index = 0; index < tests.size(); index++) {
				if (tests.get(index).getId() != null)
					validateInvalidIdForDepartmentalTest(tests.get(index).getId(), errors);
				/*if (tests.get(index).getDocuments() != null && !tests.get(index).getDocuments().isEmpty()
						&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
								getDocumentsAsCSVs(tests.get(index).getDocuments()))) {
					errors.rejectValue("employee.test[" + index + "].documents", "concurrent",
							"document(s) already exists");
				}*/
			}
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdForDepartmentalTest(Long id, Errors errors) {
		// TODO query table and populate error

	}

	private void validateEducationalQualification(List<EducationalQualification> educations, Long employeeId,
			String tenantId, Errors errors) {
		if (educations != null && !educations.isEmpty()) {
			for (int index = 0; index < educations.size(); index++) {
				if (educations.get(index).getId() != null)
					validateInvalidIdForEducationalQualification(educations.get(index).getId(), errors);
				/*if (educations.get(index).getDocuments() != null && !educations.get(index).getDocuments().isEmpty()
						&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
								getDocumentsAsCSVs(educations.get(index).getDocuments()))) {
					errors.rejectValue("employee.education[" + index + "].documents", "concurrent",
							"document(s) already exists");
				}*/
			}
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdForEducationalQualification(Long id, Errors errors) {
		// TODO query table and populate error

	}

	private void validateProbation(List<Probation> probations, Long employeeId, String tenantId, Errors errors) {
		if (probations != null && !probations.isEmpty()) {
			for (int index = 0; index < probations.size(); index++) {
				if (probations.get(index).getId() != null)
					validateInvalidIdForProbation(probations.get(index).getId(), errors);
				/*if (probations.get(index).getDocuments() != null && !probations.get(index).getDocuments().isEmpty()
						&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
								getDocumentsAsCSVs(probations.get(index).getDocuments()))) {
					errors.rejectValue("employee.probation[" + index + "].documents", "concurrent",
							"document(s) already exists");
				}*/
			}
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdForProbation(Long id, Errors errors) {
		// TODO query table and populate error

	}

	private void validateRegularisation(List<Regularisation> regularisations, Long employeeId, String tenantId,
			Errors errors) {
		if (regularisations != null && !regularisations.isEmpty()) {
			for (int index = 0; index < regularisations.size(); index++) {
				if (regularisations.get(index).getId() != null)
					validateInvalidIdForRegularisation(regularisations.get(index).getId(), errors);
				/*if (regularisations.get(index).getDocuments() != null
						&& !regularisations.get(index).getDocuments().isEmpty()
						&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
								getDocumentsAsCSVs(regularisations.get(index).getDocuments()))) {
					errors.rejectValue("employee.regularisation[" + index + "].documents", "concurrent",
							"document(s) already exists");
				}*/
			}
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdForRegularisation(Long id, Errors errors) {
		// TODO query table and populate error

	}

	private void validateServiceHistory(List<ServiceHistory> histories, Long employeeId, String tenantId,
			Errors errors) {
		if (histories != null && !histories.isEmpty()) {
			for (int index = 0; index < histories.size(); index++) {
				if (histories.get(index).getId() != null)
					validateInvalidIdForServiceHistory(histories.get(index).getId(), errors);
				/*if (histories.get(index).getDocuments() != null && !histories.get(index).getDocuments().isEmpty()
						&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
								getDocumentsAsCSVs(histories.get(index).getDocuments()))) {
					errors.rejectValue("employee.serviceHistory[" + index + "].documents", "concurrent",
							"document(s) already exists");
				}*/
			}
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdForServiceHistory(Long id, Errors errors) {
		// TODO query table and populate error

	}

	private void validateTechnicalQualification(List<TechnicalQualification> technicals, Long employeeId,
			String tenantId, Errors errors) {
		if (technicals == null || technicals.isEmpty())
			return;
		for (int index = 0; index < technicals.size(); index++) {
			if (technicals.get(index).getId() != null)
				validateInvalidIdforTechnicalQualifications(technicals.get(index).getId(), errors);
			/*if (technicals.get(index).getDocuments() != null && !technicals.get(index).getDocuments().isEmpty()
					&& employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV("egeis_employeeDocuments", "document",
							getDocumentsAsCSVs(technicals.get(index).getDocuments()))) {
				errors.rejectValue("employee.technical[" + index + "].documents", "concurrent",
						"document(s) already exists");
			}*/
		}
	}

	/**
	 * Ids should always be generated by sequence. Any id populated will be in
	 * general considered as an existing record. But what if someone passes an
	 * id that does no exist? This validation takes care of this by checking in
	 * the db for the existence of the record.
	 * 
	 * @param id
	 */
	private void validateInvalidIdforTechnicalQualifications(Long id, Errors errors) {
		// TODO query table and populate error

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