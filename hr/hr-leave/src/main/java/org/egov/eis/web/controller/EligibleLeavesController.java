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

package org.egov.eis.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.LeaveAllotment;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.LeaveOpeningBalance;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.service.LeaveAllotmentService;
import org.egov.eis.service.LeaveApplicationService;
import org.egov.eis.service.LeaveOpeningBalanceService;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/eligibleleaves")
public class EligibleLeavesController {

    private static final Logger logger = LoggerFactory.getLogger(LeaveAllotmentController.class);

    @Autowired
    private LeaveAllotmentService leaveAllotmentService;

    @Autowired
    private LeaveOpeningBalanceService leaveOpeningBalanceService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
                                    final BindingResult requestBodyBindingResult, @RequestParam(name = "tenantId", required = true) final String tenantId,
                                    @RequestParam(name = "leaveType", required = true) final Long leaveType,
                                    @RequestParam(name = "designationId", required = false) final Long designationId,
                                    @RequestParam(name = "employeeid", required = true) final Long employeeid,
                                    @RequestParam(name = "asOnDate", required = true) final String asOnDate,
                                    @RequestParam(name = "sort", required = false) final String sort,
                                    @RequestParam(name = "pageSize", required = false) final Integer pageSize,
                                    @RequestParam(name = "pageNumber", required = false) final Integer pageNumber) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        LocalDate yearStartDate = null, asondate = null;
        LocalDate dateOfAppointment = null;
        if (asOnDate != null && !asOnDate.isEmpty()) {
            asondate = LocalDate.parse(asOnDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            yearStartDate = LocalDate.parse("01/01/" + asondate.getYear(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        final List<EmployeeInfo> employees = employeeRepository.getEmployeeById(requestInfo, tenantId, employeeid);


        if (employees.size() > 0 && employees.get(0).getDateOfAppointment() != null) {
            dateOfAppointment = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employees.get(0).getDateOfAppointment()));
        }


        if (dateOfAppointment != null && dateOfAppointment.isAfter(yearStartDate))
            yearStartDate = dateOfAppointment;

        Float openingBalanceValue = 0f, allotmentValue = 0f, proratedAllotmentValue = 0f, applicationValue = 0f;

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        final LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest = new LeaveOpeningBalanceGetRequest();

        leaveOpeningBalanceGetRequest.getEmployee().add(employeeid);

        leaveOpeningBalanceGetRequest.getLeaveType().add(leaveType);

        leaveOpeningBalanceGetRequest.setTenantId(tenantId);

        leaveOpeningBalanceGetRequest.setYear(asondate != null ? asondate.getYear() : null);

        List<LeaveOpeningBalance> leaveOpeningBalancesList = null;
        try {
            leaveOpeningBalancesList = leaveOpeningBalanceService
                    .getLeaveOpeningBalances(leaveOpeningBalanceGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + leaveOpeningBalanceGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        if (leaveOpeningBalancesList != null && !leaveOpeningBalancesList.isEmpty())
            openingBalanceValue = leaveOpeningBalancesList.get(0).getNoOfDays();

        final LeaveAllotmentGetRequest leaveAllotmentGetRequest = new LeaveAllotmentGetRequest();

        leaveAllotmentGetRequest.getDesignationId().add(designationId);

        leaveAllotmentGetRequest.getLeaveType().add(leaveType);

        leaveAllotmentGetRequest.setTenantId(tenantId);

        List<LeaveAllotment> leaveAllotmentsList = null;

        try {
            leaveAllotmentsList = leaveAllotmentService.getLeaveAllotments(leaveAllotmentGetRequest);
            if (leaveAllotmentGetRequest.getDesignationId() != null
                    && !leaveAllotmentGetRequest.getDesignationId().isEmpty()
                    && (leaveAllotmentsList != null && leaveAllotmentsList.isEmpty()
                    || leaveAllotmentsList == null)) {
                leaveAllotmentGetRequest.setDesignationId(null);
                leaveAllotmentsList = leaveAllotmentService.getLeaveAllotments(leaveAllotmentGetRequest);
            }
        } catch (final Exception exception) {
            logger.error("Error while processing request " + leaveAllotmentGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        if (leaveAllotmentsList != null && !leaveAllotmentsList.isEmpty())
            allotmentValue = leaveAllotmentsList.get(0).getNoOfDays();

        if (allotmentValue != null)
            proratedAllotmentValue = allotmentValue / 356
                    * Duration.between(yearStartDate.atTime(0, 0), asondate.atTime(0, 0)).toDays();

        final LeaveApplicationGetRequest leaveApplicationGetRequest = new LeaveApplicationGetRequest();

        leaveApplicationGetRequest.getEmployee().add(employeeid);

        leaveApplicationGetRequest.setLeaveType(leaveType);

        leaveApplicationGetRequest.setStatus(LeaveStatus.APPROVED.toString());

        leaveApplicationGetRequest.setTenantId(tenantId);

        leaveApplicationGetRequest
                .setFromDate(Date.from(yearStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        leaveApplicationGetRequest.setToDate(Date.from(asondate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<LeaveApplication> leaveApplicationsList = null;
        try {
            leaveApplicationsList = leaveApplicationService.getLeaveApplications(leaveApplicationGetRequest, requestInfo);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + leaveApplicationGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        if (leaveApplicationsList != null && !leaveApplicationsList.isEmpty())
            applicationValue = leaveApplicationsList.get(0).getLeaveDays();

        final EligibleLeave eligibleLeave = new EligibleLeave();

        eligibleLeave.setAsOnDate(asOnDate);

        eligibleLeave.setEmployee(employeeid);

        eligibleLeave.setLeaveType(leaveType);

        if (dateOfAppointment != null && dateOfAppointment.isAfter(asondate))
            eligibleLeave.setNoOfDays(0f);
        else
            eligibleLeave.setNoOfDays(openingBalanceValue + proratedAllotmentValue - applicationValue);

        Long iPart = eligibleLeave.getNoOfDays().longValue();
        final Double fPart = (double) (eligibleLeave.getNoOfDays() - iPart);

        if (fPart > 0.5)
            iPart++;

        eligibleLeave.setNoOfDays(iPart.floatValue());
        return getSuccessResponse(Collections.singletonList(eligibleLeave), requestInfo);
    }


    /**
     * Populate Response object and return EligibleLeaveResponse
     *
     * @param eligibleLeavesList
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(final List<EligibleLeave> eligibleLeavesList, final RequestInfo requestInfo) {
        final EligibleLeaveResponse eligibleLeaveResponse = new EligibleLeaveResponse();
        eligibleLeaveResponse.setEligibleLeave(eligibleLeavesList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        eligibleLeaveResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<EligibleLeaveResponse>(eligibleLeaveResponse, HttpStatus.OK);

    }

}
