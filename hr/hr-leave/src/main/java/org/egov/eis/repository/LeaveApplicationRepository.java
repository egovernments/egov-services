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

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.builder.LeaveApplicationQueryBuilder;
import org.egov.eis.repository.rowmapper.LeaveApplicationRowMapper;
import org.egov.eis.repository.rowmapper.LeaveSummaryRowMapper;
import org.egov.eis.service.HRStatusService;
import org.egov.eis.service.UserService;
import org.egov.eis.service.WorkFlowService;
import org.egov.eis.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class LeaveApplicationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LeaveApplicationRowMapper leaveApplicationRowMapper;

    @Autowired
    private LeaveSummaryRowMapper leaveSummaryRowMapper;

    @Autowired
    private LeaveApplicationQueryBuilder leaveApplicationQueryBuilder;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private HRStatusService hrStatusService;

    @Autowired
    private UserService userService;

    public List<LeaveApplication> findForCriteria(final LeaveApplicationGetRequest leaveApplicationGetRequest,
                                                  final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = leaveApplicationQueryBuilder.getQuery(leaveApplicationGetRequest,
                preparedStatementValues, requestInfo);
        final List<LeaveApplication> leaveApplications = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                leaveApplicationRowMapper);
        return leaveApplications;
    }

    public List<LeaveApplication> findForReportCriteria(final LeaveSearchRequest leaveSearchRequest,
                                                        final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = leaveApplicationQueryBuilder.getLeaveReportQuery(leaveSearchRequest,
                preparedStatementValues, requestInfo);
        final List<LeaveApplication> leaveApplications = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                leaveApplicationRowMapper);
        return leaveApplications;
    }

    public List<LeaveApplication> findForLeaveSummaryCriteria(final LeaveSearchRequest leaveSearchRequest,
                                                              final RequestInfo requestInfo) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = leaveApplicationQueryBuilder.getLeaveSummaryReportQuery(leaveSearchRequest,
                preparedStatementValues, requestInfo);
        final List<LeaveApplication> leaveApplications = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                leaveSummaryRowMapper);
        return leaveApplications;
    }

    public LeaveApplicationRequest saveLeaveApplication(final LeaveApplicationRequest leaveApplicationRequest) {
        ProcessInstance processInstance = new ProcessInstance();
        Long stateId = null;
        /*if (StringUtils.isEmpty(leaveApplicationRequest.getType()))
           processInstance = workFlowService.start(leaveApplicationRequest);
        if (processInstance.getId() != null)
            stateId = Long.valueOf(processInstance.getId());*/
        final String leaveApplicationInsertQuery = LeaveApplicationQueryBuilder.insertLeaveApplicationQuery();
        final Date now = new Date();
        final UserResponse userResponse = userService
                .findUserByUserNameAndTenantId(leaveApplicationRequest.getRequestInfo());
        for (LeaveApplication leaveApplication : leaveApplicationRequest.getLeaveApplication()) {
            String holiday = null;
            if(!leaveApplication.getHolidays().isEmpty())
                 holiday = leaveApplication.getHolidays().toString();
            leaveApplication.setStateId(stateId);
            final Object[] obj = new Object[]{leaveApplication.getApplicationNumber(), leaveApplication.getEmployee(),
                    leaveApplication.getLeaveType().getId(), leaveApplication.getFromDate(),
                    leaveApplication.getToDate(), leaveApplication.getCompensatoryForDate(),
                    leaveApplication.getLeaveDays(), leaveApplication.getAvailableDays(),
                    leaveApplication.getHalfdays(), leaveApplication.getFirstHalfleave(), leaveApplication.getReason(),
                    leaveApplication.getStatus(), leaveApplication.getLeaveGround(),leaveApplication.getStateId() , leaveApplication.getPrefixDate(),
                    leaveApplication.getSuffixDate(), holiday, leaveApplication.getEncashable(), 11,
                    now, 11, now, leaveApplication.getTenantId()};
            jdbcTemplate.update(leaveApplicationInsertQuery, obj);
        }
        return leaveApplicationRequest;
    }

    public LeaveApplicationRequest saveCompoffLeaveApplication(final LeaveApplicationRequest leaveApplicationRequest) {
        ProcessInstance processInstance = new ProcessInstance();
        Long stateId = null;
        if (StringUtils.isEmpty(leaveApplicationRequest.getType()))
            processInstance = workFlowService.start(leaveApplicationRequest);
        if (processInstance.getId() != null)
            stateId = Long.valueOf(processInstance.getId());
        final String leaveApplicationInsertQuery = LeaveApplicationQueryBuilder.insertCompoffLeaveApplicationQuery();
        final Date now = new Date();
        final UserResponse userResponse = userService
                .findUserByUserNameAndTenantId(leaveApplicationRequest.getRequestInfo());
        for (LeaveApplication leaveApplication : leaveApplicationRequest.getLeaveApplication()) {
            leaveApplication.setStateId(stateId);
            final Object[] obj = new Object[]{leaveApplication.getApplicationNumber(), leaveApplication.getEmployee(),
                    leaveApplication.getFromDate(), leaveApplication.getToDate(),
                    leaveApplication.getCompensatoryForDate(), leaveApplication.getLeaveDays(),
                    leaveApplication.getAvailableDays(), leaveApplication.getHalfdays(),
                    leaveApplication.getFirstHalfleave(), leaveApplication.getReason(), leaveApplication.getStatus(),
                    leaveApplication.getStateId(), userResponse.getUsers().get(0).getId(), now,
                    userResponse.getUsers().get(0).getId(), now, leaveApplication.getTenantId()};
            jdbcTemplate.update(leaveApplicationInsertQuery, obj);
        }
        return leaveApplicationRequest;
    }

    public LeaveApplication updateLeaveApplication(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final Task task = workFlowService.update(leaveApplicationRequest);
        final String leaveApplicationInsertQuery = LeaveApplicationQueryBuilder.updateLeaveApplicationQuery();
        final Date now = new Date();
        final LeaveApplication leaveApplication = leaveApplicationRequest.getLeaveApplication();
        final UserResponse userResponse = userService
                .findUserByUserNameAndTenantId(leaveApplicationRequest.getRequestInfo());
        leaveApplication.setStateId(Long.valueOf(task.getId()));
        leaveApplicationStatusChange(leaveApplication, leaveApplicationRequest.getRequestInfo());
        final Object[] obj = new Object[]{leaveApplication.getApplicationNumber(), leaveApplication.getEmployee(),
                leaveApplication.getLeaveType().getId(), leaveApplication.getFromDate(), leaveApplication.getToDate(),
                leaveApplication.getCompensatoryForDate(), leaveApplication.getLeaveDays(),
                leaveApplication.getAvailableDays(), leaveApplication.getHalfdays(),
                leaveApplication.getFirstHalfleave(), leaveApplication.getReason(), leaveApplication.getStatus(),
                Long.valueOf(task.getId()), userResponse.getUsers().get(0).getId(), now, leaveApplication.getId(),
                leaveApplication.getTenantId()};
        jdbcTemplate.update(leaveApplicationInsertQuery, obj);
        return leaveApplication;
    }

    private void leaveApplicationStatusChange(final LeaveApplication leaveApplication, final RequestInfo requestInfo) {
        final String workFlowAction = leaveApplication.getWorkflowDetails().getAction();
        if ("Approve".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(hrStatusService
                    .getHRStatuses(LeaveStatus.APPROVED.toString(), leaveApplication.getTenantId(), requestInfo).get(0)
                    .getId());
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(hrStatusService
                    .getHRStatuses(LeaveStatus.REJECTED.toString(), leaveApplication.getTenantId(), requestInfo).get(0)
                    .getId());
        else if ("Cancel".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(hrStatusService
                    .getHRStatuses(LeaveStatus.CANCELLED.toString(), leaveApplication.getTenantId(), requestInfo).get(0)
                    .getId());
        else if ("Submit".equalsIgnoreCase(workFlowAction))
            leaveApplication.setStatus(hrStatusService
                    .getHRStatuses(LeaveStatus.RESUBMITTED.toString(), leaveApplication.getTenantId(), requestInfo)
                    .get(0).getId());
    }

    public List<LeaveApplication> getLeaveApplicationForDateRange(LeaveApplication leaveApplication,
                                                                  final RequestInfo requestInfo) {
        final String leaveApplicationGetForDateRangeQuery = LeaveApplicationQueryBuilder
                .getLeaveApplicationForDateRangeQuery();
        final Object[] obj = new Object[]{leaveApplication.getFromDate(), leaveApplication.getToDate(),
                leaveApplication.getFromDate(), leaveApplication.getToDate(), leaveApplication.getEmployee(),
                hrStatusService
                        .getHRStatuses(LeaveStatus.CANCELLED.toString(), leaveApplication.getTenantId(), requestInfo)
                        .get(0).getId(),
                leaveApplication.getId() == null ? -1 : leaveApplication.getId(), leaveApplication.getTenantId()};
        final List<LeaveApplication> leaveApplications = jdbcTemplate.query(leaveApplicationGetForDateRangeQuery, obj,
                leaveApplicationRowMapper);
        return leaveApplications;
    }

    public LeaveApplication getLeaveApplicationForDate(Long employeeId, Date compensatoryDate, String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Date fromdate = null;
        preparedStatementValues.add(employeeId);
        preparedStatementValues.add(compensatoryDate);
        preparedStatementValues.add(tenantId);
        final List<LeaveApplication> leaveApplications = leaveApplicationQueryBuilder
                .getLeaveApplicationForCompensatoryDate(employeeId, compensatoryDate, fromdate, fromdate, tenantId);
        return leaveApplications.isEmpty() ? null : leaveApplications.get(0);
    }

    public LeaveApplication getLeaveApplicationForDateRange(Long employeeId, Date fromDate, Date toDate,
                                                            String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(employeeId);
        preparedStatementValues.add(fromDate);
        preparedStatementValues.add(toDate);
        preparedStatementValues.add(tenantId);
        final List<LeaveApplication> leaveApplications = leaveApplicationQueryBuilder
                .getLeaveApplicationForCompensatoryDate(employeeId, null, fromDate, toDate, tenantId);
        return leaveApplications.isEmpty() ? null : leaveApplications.get(0);
    }

}