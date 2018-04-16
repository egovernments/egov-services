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

import java.util.List;

import org.egov.eis.model.Nominee;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.NomineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class NomineeValidator {

    @Autowired
    private NomineeRepository nomineeRepository;

    /**
     * Validates whether each & every nominee is unique in the request payload or not
     * Also validates the unicity of each nominee against the db
     * Note: Unicity is checked against the fields - employeeId, name, gender, dateOfBirth, relationship, tenantId
     *
     * @param nominees
     * @param errors
     * @return void
     */
    public void validateNominees(List<Nominee> nominees, boolean isUpdate, Errors errors) {
        for(int i = 0; i < nominees.size(); i++) {
            for (int j = i + 1; j < nominees.size(); j++) {
                if (i != j && nominees.get(i).equals(nominees.get(j))) {
                    errors.rejectValue("nominees[" + i + "]", "invalid",
                            "Duplicate Nominees " + nominees.get(i).getName() + " & "
                                    + nominees.get(j).getName() + " Present In Request. Please Enter The Correct Data.");
                }
            }
            if(!nomineeRepository.checkIfUnique(nominees.get(i), isUpdate)) {
                errors.rejectValue("nominees[" + i + "]", "invalid",
                        "Nominee " + nominees.get(i).getName() + " Already Exists In System. Please Enter Different Nominee.");
            }
        }
    }

    /**
     * Validates whether the nominating employee is existing in the system or not based on its ID
     *
     * @param nominees
     * @param errors
     * @return void
     */
    protected void validateNomineesNominatingEmployees(List<Nominee> nominees, Errors errors) {
        for (int i = 0; i < nominees.size(); i++) {
            if (isEmpty(nominees.get(i).getEmployee().getId())) {
                errors.rejectValue("nominees[" + i + "].employee.id", "invalid",
                        "Nominating Employee Id Can't Be Left Empty. Please Enter Correct Id");
            }
            if (!nomineeRepository.ifExists(EntityType.EMPLOYEE_HEADER.getDbTable(), nominees.get(i).getEmployee().getId(),
                    nominees.get(i).getTenantId())) {
                errors.rejectValue("nominees[" + i + "].employee.id", "invalid",
                        "Nominating Employee Id Doesn't Exists. Please Enter Correct Id");
            }
        }
    }
}