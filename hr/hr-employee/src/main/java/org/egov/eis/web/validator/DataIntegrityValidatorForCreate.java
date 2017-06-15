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

import org.egov.eis.model.Employee;
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

	@Override
	public void validate(Object targetObject, Errors errors) {
		if (!(targetObject instanceof Employee))
			return;

		Employee employee = (Employee) targetObject;
		validateEmployee(employee, errors);
		validatePrimaryPositions(employee.getAssignments(), employee.getId(), employee.getTenantId(), errors, "create");
	}

	// FIXME Validate data existence of Religion, Languages etc. for every data in separate methods
	private void validateExternalAPIData() {
	}

	protected void validateEmployee(Employee employee, Errors errors) {
		super.validateEmployee(employee, errors);

		if ((employee.getCode() != null) && duplicateExists("egeis_employee", "code",
				employee.getCode(), employee.getTenantId())) {
			errors.rejectValue("employee.code", "invalid",
					"Employee Code Already Exists In System. Please Send The Different Employee Code.");
		}

		if ((employee.getPassportNo() != null) && duplicateExists("egeis_employee", "passportNo",
				employee.getPassportNo(), employee.getTenantId())) {
			errors.rejectValue("employee.passportNo", "invalid",
					"Passport Number Already Exists In System. Please Send The Correct Passport Number.");
		}

		if ((employee.getGpfNo() != null) && duplicateExists("egeis_employee", "gpfNo",
				employee.getGpfNo(), employee.getTenantId())) {
			errors.rejectValue("employee.gpfNo", "invalid",
					"GPF Number Already Exists In System. Please Send The Correct GPF Number.");
		}
	}

	/**
	 * Checks if the given string is present in db for the given column and given table.
	 */
	Boolean duplicateExists(String table, String column, String value, String tenantId) {
		return employeeRepository.duplicateExists(table, column, value, tenantId);
	}
}