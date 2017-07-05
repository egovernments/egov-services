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

package org.egov.eis.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.model.Movement;
import org.egov.eis.model.enums.MovementStatus;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.repository.builder.MovementQueryBuilder;
import org.egov.eis.repository.rowmapper.MovementRowMapper;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.HRStatusService;
import org.egov.eis.service.UserService;
import org.egov.eis.service.WorkFlowService;
import org.egov.eis.web.contract.Assignment;
import org.egov.eis.web.contract.Employee;
import org.egov.eis.web.contract.MovementRequest;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.ProcessInstance;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.Task;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MovementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MovementRowMapper movementRowMapper;

    @Autowired
    private MovementQueryBuilder movementQueryBuilder;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private UserService userService;

    @Autowired
    private HRStatusService hrStatusService;

    @Autowired
    private EmployeeService employeeService;

    public List<Movement> findForCriteria(final MovementSearchRequest movementSearchRequest,
            final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = movementQueryBuilder.getQuery(movementSearchRequest, preparedStatementValues,
                requestInfo);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                movementRowMapper);
    }

    public MovementRequest saveMovement(final MovementRequest movementRequest) {
        ProcessInstance processInstance = new ProcessInstance();
        Long stateId = null;
        if (StringUtils.isEmpty(movementRequest.getType()))
            processInstance = workFlowService.start(movementRequest);
        if (processInstance.getId() != null)
            stateId = Long.valueOf(processInstance.getId());
        final String movementInsertQuery = MovementQueryBuilder.insertMovementQuery();
        final Date now = new Date();
        final UserResponse userResponse = userService.findUserByUserNameAndTenantId(
                movementRequest.getRequestInfo());
        for (final Movement movement : movementRequest.getMovement()) {
            movement.setStateId(stateId);
            final Object[] obj = new Object[] { movement.getEmployeeId(), movement.getTypeOfMovement().toString(),
                    movement.getCurrentAssignment(),
                    movement.getTransferType().toString(), movement.getPromotionBasis().getId(), movement.getRemarks(),
                    movement.getReason().getId(), movement.getEffectiveFrom(),
                    movement.getEnquiryPassedDate(), movement.getTransferedLocation(),
                    movement.getDepartmentAssigned(), movement.getDesignationAssigned(),
                    movement.getPositionAssigned(), movement.getFundAssigned(), movement.getFunctionAssigned(),
                    movement.getDocuments(), movement.getEmployeeAcceptance(),
                    movement.getStatus(), movement.getStateId(),
                    userResponse.getUsers().get(0).getId(), now,
                    userResponse.getUsers().get(0).getId(), now, movement.getTenantId() };
            jdbcTemplate.update(movementInsertQuery, obj);
        }
        return movementRequest;
    }

    public Movement updateMovement(final MovementRequest movementRequest) {
        final Task task = workFlowService.update(movementRequest);
        final String movementInsertQuery = MovementQueryBuilder.updateMovementQuery();
        final Date now = new Date();
        final Movement movement = movementRequest.getMovement().get(0);
        final UserResponse userResponse = userService.findUserByUserNameAndTenantId(
                movementRequest.getRequestInfo());
        movement.setStateId(Long.valueOf(task.getId()));
        movementStatusChange(movement, movementRequest.getRequestInfo());
        final Object[] obj = new Object[] { movement.getEmployeeId(), movement.getTypeOfMovement().toString(),
                movement.getCurrentAssignment(),
                movement.getTransferType().toString(), movement.getPromotionBasis().getId(), movement.getRemarks(),
                movement.getReason().getId(), movement.getEffectiveFrom(),
                movement.getEnquiryPassedDate(), movement.getTransferedLocation(),
                movement.getDepartmentAssigned(), movement.getDesignationAssigned(),
                movement.getPositionAssigned(), movement.getFundAssigned(), movement.getFunctionAssigned(),
                movement.getDocuments(), movement.getEmployeeAcceptance(),
                movement.getStatus(), movement.getStateId(),
                userResponse.getUsers().get(0).getId(), now,
                userResponse.getUsers().get(0).getId(), now,
                movement.getId(), movement.getTenantId() };
        jdbcTemplate.update(movementInsertQuery, obj);
        if (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION) && movement.getStatus()
                .equals(hrStatusService.getHRStatuses(MovementStatus.APPROVED.toString(), movement.getTenantId(),
                        movementRequest.getRequestInfo()).get(0).getId())
                && movement.getEmployeeAcceptance())
            promoteEmployee(movementRequest);
//        else if ((movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER)
//                || movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER_CUM_PROMOTION)) && movement.getStatus()
//                        .equals(hrStatusService.getHRStatuses(MovementStatus.APPROVED.toString(), movement.getTenantId(),
//                                movementRequest.getRequestInfo()).get(0).getId())
//                && movement.getEmployeeAcceptance())
//            transferEmployee(movementRequest);
        return movement;
    }

    private void promoteEmployee(final MovementRequest movementRequest) {
        final Employee employee = employeeService.getEmployee(movementRequest);
        final Movement movement = movementRequest.getMovement().get(0);
        final Date effectiveFromDate = movement.getEffectiveFrom();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(effectiveFromDate);
        calendar.add(Calendar.DATE, -1);
        final Date yesterday = calendar.getTime();
        for (final Assignment employeeAssignment : employee.getAssignments())
            if (employeeAssignment.getFromDate().before(effectiveFromDate)
                    && employeeAssignment.getToDate().after(effectiveFromDate))
                employeeAssignment.setToDate(yesterday);
        final Assignment assignment = new Assignment();
        assignment.setPosition(movement.getPositionAssigned());
        assignment.setFund(movement.getFundAssigned());
        assignment.setFunction(movement.getFunctionAssigned());
        assignment.setDepartment(movement.getDepartmentAssigned());
        assignment.setDesignation(movement.getDesignationAssigned());
        assignment.setIsPrimary(true);
        assignment.setFromDate(effectiveFromDate);
        assignment.setToDate(employee.getDateOfRetirement());
        assignment.setTenantId(movement.getTenantId());
        employee.getAssignments().add(assignment);
        employeeService.updateEmployee(employee, movement.getTenantId(),
                movementRequest.getRequestInfo());
    }

    private void movementStatusChange(final Movement movement, final RequestInfo requestInfo) {
        final String workFlowAction = movement.getWorkflowDetails().getAction();
        if ("Approve".equalsIgnoreCase(workFlowAction))
            movement
                    .setStatus(hrStatusService.getHRStatuses(MovementStatus.APPROVED.toString(), movement.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            movement
                    .setStatus(hrStatusService.getHRStatuses(MovementStatus.REJECTED.toString(), movement.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Cancel".equalsIgnoreCase(workFlowAction))
            movement
                    .setStatus(hrStatusService.getHRStatuses(MovementStatus.CANCELLED.toString(), movement.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Submit".equalsIgnoreCase(workFlowAction))
            movement
                    .setStatus(hrStatusService.getHRStatuses(MovementStatus.RESUBMITTED.toString(), movement.getTenantId(),
                            requestInfo).get(0).getId());
    }
}