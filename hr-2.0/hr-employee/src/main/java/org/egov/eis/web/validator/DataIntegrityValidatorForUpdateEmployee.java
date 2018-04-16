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

import org.egov.eis.model.*;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class DataIntegrityValidatorForUpdateEmployee extends EmployeeCommonValidator implements Validator {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public boolean supports(Class<?> paramClass) {
        return EmployeeRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        if (!(targetObject instanceof EmployeeRequest))
            return;

        EmployeeRequest employeeRequest = (EmployeeRequest) targetObject;
        Employee employee = employeeRequest.getEmployee();
        Long employeeId = employee.getId();
        String tenantId = employee.getTenantId();

        // FIXME employee.getId == null or empty then throw error employee id is required
        if (isEmpty(employeeId)) {
            errors.rejectValue("employee.id", "invalid",
                    "Employee Id Is Not Provided. Please Enter Employee ID");
            return;
        }

        if (!employeeRepository.checkIfEmployeeExists(employeeId, tenantId)) {
            // FIXME throw error employee id does not exist
            errors.rejectValue("employee.id", "invalid",
                    "Employee With Given Id Does Not Exist In The System. Please Enter Correct Id");
            return;
        }

        validateEmployee(employeeRequest, errors);
        validateAssignments(employee.getAssignments(), employeeId, tenantId, errors);
        validateDepartmentalTest(employee.getTest(), employeeId, tenantId, errors);
        validateEducationalQualification(employee.getEducation(), employeeId, tenantId, errors);
        validateProbation(employee.getProbation(), employeeId, tenantId, errors);
        validateRegularisation(employee.getRegularisation(), employeeId, tenantId, errors);
        validateServiceHistory(employee.getServiceHistory(), employeeId, tenantId, errors);
        validateTechnicalQualification(employee.getTechnical(), employeeId, tenantId, errors);
        validateAPRDetails(employee.getAprDetails(), employeeId, tenantId, errors);
    }

    // TODO
    private void validateExternalAPIData() {
    }

    public void validateEmployee(EmployeeRequest employeeRequest, Errors errors) {
        Employee employee = employeeRequest.getEmployee();
        // FIXME call common validator.validateEmployee
        super.validateEmployee(employeeRequest, errors);

        // FIXME : check only for different employees
        if (checkIfColumnValueIsSameInDB("egeis_employee", "code",
                employee.getCode(), employee.getId(), employee.getTenantId())) {
            errors.rejectValue("employee.code", "invalid",
                    "Employee Code Can't Be Changed. Please Enter Same Employee Code.");
        }

        if ((!StringUtils.isEmpty(employee.getPassportNo())) && duplicateExists("egeis_employee", "passportNo",
                employee.getPassportNo(), employee.getId(), employee.getTenantId())) {
            errors.rejectValue("employee.passportNo", "invalid",
                    "Passport Number Already Exists In System. Please Enter Correct Passport Number.");
        }

        if ((!StringUtils.isEmpty(employee.getGpfNo())) && duplicateExists("egeis_employee", "gpfNo", employee.getGpfNo(),
                employee.getId(), employee.getTenantId())) {
            errors.rejectValue("employee.gpfNo", "invalid",
                    "GPF Number Already Exists In System. Please Enter Correct GPF Number.");
        }

        if (employee.getPassportNo() != null && employee.getPassportNo().equals(""))
            employee.setPassportNo(null);
        if (employee.getGpfNo() != null && employee.getGpfNo().equals(""))
            employee.setGpfNo(null);
    }

    private void validateAssignments(List<Assignment> assignments, Long employeeId, String tenantId, Errors errors) {
        validateIdsForAssignment(assignments, employeeId, tenantId, errors);
        validatePrimaryPositions(assignments, employeeId, tenantId, errors, "update");
        for (int index = 0; index < assignments.size(); index++) {
            // validateDocumentsForNewAssignment(assignments.get(index), errors, index);
            if (assignments.get(index).getIsPrimary()
                    && assignmentRepository.assignmentsOverlapping(assignments.get(index), employeeId, tenantId)) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");
                String fromDate = dateFormat.format(assignments.get(index).getFromDate());
                String toDate = dateFormat.format(assignments.get(index).getToDate());
                errors.rejectValue("employee.assignments[" + index + "]", "invalid",
                        "Employee Is Already Having A Primary Assignment Between "
                                + fromDate + " & " + toDate + ". Please Enter Different Dates.");
            }
        }
    }

    private void validateIdsForAssignment(List<Assignment> assignments, Long employeeId, String tenantId,
                                          Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < assignments.size(); index++) {
            if (assignments.get(index).getId() != null) // FIXME check if long gets default value of 0L
                idsMap.put(assignments.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.ASSIGNMENT, employeeId, tenantId, errors);
    }

    private void validateDepartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
                                          Errors errors) {
        if (isEmpty(tests))
            return;
        validateIdsForDepartmentalTest(tests, employeeId, tenantId, errors);
    }

    private void validateIdsForDepartmentalTest(List<DepartmentalTest> tests, Long employeeId, String tenantId,
                                                Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < tests.size(); index++) {
            if (tests.get(index).getId() != null)
                idsMap.put(tests.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.TEST, employeeId, tenantId, errors);

    }

    private void validateEducationalQualification(List<EducationalQualification> educations, Long employeeId,
                                                  String tenantId, Errors errors) {
        if (isEmpty(educations))
            return;
        validateIdsForEducationalQualification(educations, employeeId, tenantId, errors);
    }

    private void validateIdsForEducationalQualification(List<EducationalQualification> educations, Long employeeId,
                                                        String tenantId, Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < educations.size(); index++) {
            if (educations.get(index).getId() != null)
                idsMap.put(educations.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.EDUCATION, employeeId, tenantId, errors);
    }

    private void validateProbation(List<Probation> probations, Long employeeId, String tenantId, Errors errors) {
        if (isEmpty(probations))
            return;
        validateIdsForProbation(probations, employeeId, tenantId, errors);
    }

    private void validateIdsForProbation(List<Probation> probations, Long employeeId, String tenantId, Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < probations.size(); index++) {
            if (probations.get(index).getId() != null)
                idsMap.put(probations.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.PROBATION, employeeId, tenantId, errors);
    }

    private void validateRegularisation(List<Regularisation> regularisations, Long employeeId, String tenantId,
                                        Errors errors) {
        if (isEmpty(regularisations))
            return;
        validateIdsForRegularisation(regularisations, employeeId, tenantId, errors);
    }

    private void validateIdsForRegularisation(List<Regularisation> regularisations, Long employeeId, String tenantId,
                                              Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < regularisations.size(); index++) {
            if (regularisations.get(index).getId() != null)
                idsMap.put(regularisations.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.REGULARISATION, employeeId, tenantId, errors);
    }

    private void validateServiceHistory(List<ServiceHistory> serviceHistories, Long employeeId, String tenantId,
                                        Errors errors) {
        if (isEmpty(serviceHistories))
            return;
        validateIdsForServiceHistory(serviceHistories, employeeId, tenantId, errors);
    }

    private void validateIdsForServiceHistory(List<ServiceHistory> serviceHistories, Long employeeId, String tenantId,
                                              Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < serviceHistories.size(); index++) {
            if (serviceHistories.get(index).getId() != null)
                idsMap.put(serviceHistories.get(index).getId(), index);

        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.SERVICE, employeeId, tenantId, errors);
    }

    private void validateTechnicalQualification(List<TechnicalQualification> technicals, Long employeeId,
                                                String tenantId, Errors errors) {
        if (isEmpty(technicals))
            return;
        validateIdsForTechnicalQualification(technicals, employeeId, tenantId, errors);
    }

    private void validateIdsForTechnicalQualification(List<TechnicalQualification> technicals, Long employeeId,
                                                      String tenantId, Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < technicals.size(); index++) {
            if (technicals.get(index).getId() != null)
                idsMap.put(technicals.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.TECHNICAL, employeeId, tenantId, errors);
    }

    private void validateAPRDetails(List<APRDetail> aprDetails, Long employeeId, String tenantId, Errors errors) {
        if (isEmpty(aprDetails))
            return;
        validateIdsForAPRDetails(aprDetails, employeeId, tenantId, errors);
    }

    private void validateIdsForAPRDetails(List<APRDetail> aprDetails, Long employeeId, String tenantId, Errors errors) {
        Map<Long, Integer> idsMap = new HashMap<>();
        for (int index = 0; index < aprDetails.size(); index++) {
            if (aprDetails.get(index).getId() != null)
                idsMap.put(aprDetails.get(index).getId(), index);
        }
        if (!idsMap.isEmpty())
            validateEntityId(idsMap, EntityType.APR_DETAILS, employeeId, tenantId, errors);
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
    private Boolean duplicateExists(String table, String column, String value, Long id, String tenantId) {
        Long idFromDb = employeeRepository.getId(table, column, value, tenantId);
        if (idFromDb == 0 || id.equals(idFromDb))
            return false;
        return true;
    }

    private Boolean checkIfColumnValueIsSameInDB(String table, String column, String value, Long id, String tenantId) {
        Long idFromDb = employeeRepository.getId(table, column, value, tenantId);
        if (id.equals(idFromDb))
            return false;
        return true;
    }
}