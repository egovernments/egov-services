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

package org.egov.hrms.web.validator;

import org.egov.hrms.model.Assignment;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EmployeeRepository;
import org.egov.hrms.repository.NonVacantPositionsRepository;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Has all validations that are common to create and update employee
 */
@Component
public abstract class EmployeeCommonValidator {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NonVacantPositionsRepository nonVacantPositionsRepository;

    protected void validateEmployee(EmployeeRequest employeeRequest, Errors errors) {
        Employee employee = employeeRequest.getEmployee();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = null;

        if (employee.getUser() != null && employee.getUser().getDob() != null) {
            try {
                dateOfBirth = dateFormat.parse(employee.getUser().getDob());
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }


        if (employee.getDateOfAppointment() != null && dateOfBirth != null
                && employee.getDateOfAppointment().before(dateOfBirth))
            errors.rejectValue("employee.user.dob", "invalid",
                    "Date Of Appointment Should Be Greater Than Date Of Birth. Please Enter Correct Dates.");

        if (employee.getRetirementAge() != null && employee.getRetirementAge() > 100)
            errors.rejectValue("employee.retirementAge", "invalid",
                    "Retirement Age Should Be Less Than 100. Please Enter The Correct Retirement Age.");

        if ((employee.getDateOfAppointment() != null && employee.getDateOfJoining() != null)
                && (employee.getDateOfAppointment().after(employee.getDateOfJoining())))
            errors.rejectValue("employee.dateOfAppointment", "invalid",
                    "Date Of Joining Should Be Greater Than Date Of Appointment. Please Enter Correct Dates.");

        if ((employee.getDateOfResignation() != null && employee.getDateOfJoining() != null)
                && (employee.getDateOfResignation().compareTo(employee.getDateOfJoining()) <= 0))
            errors.rejectValue("employee.dateOfResignation", "invalid",
                    "Date Of Resignation Should Be Greater Than Date Of Joining. Please Enter Correct Dates.");

        if ((employee.getDateOfTermination() != null && employee.getDateOfJoining() != null)
                && (employee.getDateOfTermination().compareTo(employee.getDateOfJoining()) <= 0))
            errors.rejectValue("employee.dateOfTermination", "invalid",
                    "Date Of Termination Should Be Greater Than Date Of Joining. Please Enter Correct Dates.");

        if ((employee.getDateOfRetirement() != null && employee.getDateOfJoining() != null)
                && (employee.getDateOfRetirement().compareTo(employee.getDateOfJoining()) <= 0))
            errors.rejectValue("employee.dateOfRetirement", "invalid",
                    "Date Of Retirement Should Be Greater Than Date Of Joining. Please Enter Correct Dates.");
    }

    void validateEntityId(Map<Long, Integer> idsMap, EntityType entityType, Long employeeId,
                          String tenantId, Errors errors) {
        List<Long> idsFromDB = employeeRepository.getListOfIds(entityType.getDbTable(), employeeId, tenantId);
        idsMap.keySet().forEach((id) -> {
            if (!idsFromDB.contains(id))
                errors.rejectValue("employee." + entityType.getContractFieldName() + "[" + idsMap.get(id) + "].id",
                        "invalid", entityType.getEntityName() + " Doesn't Exist For This Employee." +
                                " Please Enter Correct " + entityType.getEntityName() + ".");
        });
    }

    void validatePrimaryPositions(List<Assignment> assignments, Long employeeId, String tenantId, Errors errors,
                                  String requestType) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getIsPrimary()) {
                if (nonVacantPositionsRepository.checkIfPositionIsOccupied(assignments.get(i), employeeId, tenantId, requestType)) {
                    Date fromDate = assignments.get(i).getFromDate();
                    Date toDate = assignments.get(i).getToDate();
                    errors.rejectValue("employee.assignments[" + i + "].position", "invalid",
                            "Primary Position Already Assigned Between The Given Dates " + dateFormat.format(fromDate)
                                    + " & " + dateFormat.format(toDate) + ". Please Enter Correct Data.");
                }
            }
        }
    }
}
