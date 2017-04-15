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

import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DataIntegrityValidatorForCreate extends EmployeeCommonValidator implements Validator {
	
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
		validateEmployee(employee, errors);
		validateDocuments(employee, errors);
	}

	protected void populateDocumentErrors(List<EmployeeDocument> inputDocuments,
			List<EmployeeDocument> employeeDocumentsFromDb, Employee employee, Errors errors) {
		for (EmployeeDocument inputDocument : inputDocuments) {
			for (EmployeeDocument documentInDb : employeeDocumentsFromDb) {
				if (inputDocument.getDocument().equals(documentInDb.getDocument())) {
					List<Integer> index = getIndex(employee, EntityType.valueOf(inputDocument.getReferenceType()), inputDocument.getDocument());
					System.out.println("index=" + index);
					errors.rejectValue(getDocErrorMsg(index, EntityType.valueOf(inputDocument.getReferenceType())), "concurrent", "document(s) already exists");
				}
			}
		}	
	}

	// FIXME Validate data existence of Religion, Languages etc. for every data
	// in separate methods
	private void validateExternalAPIData() {

	}

	protected void validateEmployee(Employee employee, Errors errors) {
		super.validateEmployee(employee, errors);

		if ((employee.getCode() != null) && duplicateExists("egeis_employee", "code",
				employee.getCode(), employee.getTenantId())) {
			errors.rejectValue("employee.code", "concurrent", "employee code already exists");
		}

		if ((employee.getPassportNo() != null) && duplicateExists("egeis_employee", "passportNo",
				employee.getPassportNo(), employee.getTenantId())) {
			errors.rejectValue("employee.passportNo", "concurrent", "passportNo already exists");
		}

		if ((employee.getGpfNo() != null) && duplicateExists("egeis_employee", "gpfNo",
				employee.getGpfNo(), employee.getTenantId())) {
			errors.rejectValue("employee.gpfNo", "concurrent", "gpfNo already exists");
		}

	}

	/*private void validateDepartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
			Errors errors) {
		if (isEmpty(tests))
			return;
		for (int index = 0; index < tests.size(); index++) {
			employeeCommonValidator.validateDocumentsForNewEntity(tests.get(index).getDocuments(), 
					EntityType.TEST, tenantId, errors, index);
		}
	}*/
	
	/*private String getDocumentsAsCSVs(List<String> documents) {
		return "'" + String.join("','", documents) + "'";
	}*/
	
	/**
	 * Checks if the given string is present in db for the given column and given table.
	 */
	public Boolean duplicateExists(String table, String column, String value, String tenantId) {
		return employeeRepository.duplicateExists(table, column, value, tenantId);
	}
}