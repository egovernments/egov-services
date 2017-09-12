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

package org.egov.eis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.LeaveOpeningBalance;
import org.egov.eis.model.LeaveType;
import org.egov.eis.repository.CommonMastersRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.LeaveOpeningBalanceRepository;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveOpeningBalanceService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveOpeningBalanceService.class);

    @Value("${kafka.topics.leaveopeningbalance.create.name}")
    private String leaveOpeningBalanceCreateTopic;

    @Value("${kafka.topics.leaveopeningbalance.update.name}")
    private String leaveOpeningBalanceUpdateTopic;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private LeaveOpeningBalanceRepository leaveOpeningBalanceRepository;

    @Autowired
    private CommonMastersRepository commonMastersRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ObjectMapper objectMapper;

    public List<LeaveOpeningBalance> getLeaveOpeningBalances(
            LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {
        return leaveOpeningBalanceRepository.findForCriteria(leaveOpeningBalanceGetRequest);
    }

    public ResponseEntity<?> createLeaveOpeningBalance(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest,
                                                       String type) {
        List<LeaveOpeningBalance> leaveOpeningBalanceList = validate(leaveOpeningBalanceRequest);

        List<LeaveOpeningBalance> successLeaveOpeningBalanceList = new ArrayList<LeaveOpeningBalance>();
        List<LeaveOpeningBalance> errorLeaveOpeningBalanceList = new ArrayList<LeaveOpeningBalance>();
        for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceList) {
            if (leaveOpeningBalance.getErrorMsg().isEmpty())
                successLeaveOpeningBalanceList.add(leaveOpeningBalance);
            else
                errorLeaveOpeningBalanceList.add(leaveOpeningBalance);
        }
        leaveOpeningBalanceRequest.setLeaveOpeningBalance(successLeaveOpeningBalanceList);
        kafkaTemplate.send(leaveOpeningBalanceCreateTopic, leaveOpeningBalanceRequest);
        if (type != null && "upload".equalsIgnoreCase(type))
            return getSuccessResponseForUpload(successLeaveOpeningBalanceList, errorLeaveOpeningBalanceList,
                    leaveOpeningBalanceRequest.getRequestInfo());
        else
            return getSuccessResponseForCreate(leaveOpeningBalanceList, leaveOpeningBalanceRequest.getRequestInfo());
    }

    private List<LeaveOpeningBalance> validate(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {

        String tenantId = "", errorMsg = "";
        Integer year = null;

        if (leaveOpeningBalanceRequest.getLeaveOpeningBalance() != null
                && !leaveOpeningBalanceRequest.getLeaveOpeningBalance().isEmpty()) {
            tenantId = leaveOpeningBalanceRequest.getLeaveOpeningBalance().get(0).getTenantId();
            year = leaveOpeningBalanceRequest.getLeaveOpeningBalance().get(0).getCalendarYear();
        }
        CalendarYearResponse calendarYearResponse = commonMastersRepository
                .getCalendaryears(leaveOpeningBalanceRequest.getRequestInfo(), tenantId);
        EmployeeInfoResponse employeeResponse = employeeRepository
                .getEmployees(leaveOpeningBalanceRequest.getRequestInfo(), tenantId);
        LeaveTypeGetRequest leaveTypeGetRequest = new LeaveTypeGetRequest();
        leaveTypeGetRequest.setTenantId(tenantId);
        leaveTypeGetRequest.setAccumulative(true);
        leaveTypeGetRequest.setPageSize((short) 500);
        List<LeaveType> leaveTypes = leaveTypeService.getLeaveTypes(leaveTypeGetRequest);
        Map<Integer, CalendarYear> calendarYearMap = new HashMap<Integer, CalendarYear>();
        Map<Long, EmployeeInfo> employeeMap = new HashMap<Long, EmployeeInfo>();
        Map<Long, LeaveType> leaveTypeMap = new HashMap<Long, LeaveType>();
        for (CalendarYear cy : calendarYearResponse.getCalendarYear()) {
            calendarYearMap.put(cy.getName(), cy);
        }
        for (EmployeeInfo e : employeeResponse.getEmployees()) {
            employeeMap.put(e.getId(), e);
        }
        for (LeaveType lt : leaveTypes) {
            leaveTypeMap.put(lt.getId(), lt);
        }
        for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
            errorMsg = "";
            if (calendarYearMap.get(leaveOpeningBalance.getCalendarYear()) == null) {
                errorMsg = "CalendarYear " + leaveOpeningBalance.getCalendarYear() + " does not exist in the system";
            }
            if (employeeMap.get(leaveOpeningBalance.getEmployee()) == null) {
                errorMsg = errorMsg + " Employee with id " + leaveOpeningBalance.getEmployee()
                        + " does not exist in the system";
            } else {
                leaveOpeningBalance.setEmployeeName(employeeMap.get(leaveOpeningBalance.getEmployee()).getName());
                leaveOpeningBalance.setEmployeeCode(employeeMap.get(leaveOpeningBalance.getEmployee()).getCode());
            }
            if (leaveTypeMap.get(leaveOpeningBalance.getLeaveType().getId()) == null) {
                errorMsg = errorMsg + " Leave Type with id " + leaveOpeningBalance.getLeaveType().getId()
                        + " does not exist in the system";
            } else {
                leaveOpeningBalance.getLeaveType()
                        .setName(leaveTypeMap.get(leaveOpeningBalance.getLeaveType().getId()).getName());
            }
            leaveOpeningBalance.setErrorMsg(errorMsg);

        }
        LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest = new LeaveOpeningBalanceGetRequest();
        leaveOpeningBalanceGetRequest.setTenantId(tenantId);
        leaveOpeningBalanceGetRequest.setYear(year);
        List<LeaveOpeningBalance> lobFromDb = getLeaveOpeningBalances(leaveOpeningBalanceGetRequest);
        Map<String, LeaveOpeningBalance> lobMap = new HashMap<String, LeaveOpeningBalance>();
        for (LeaveOpeningBalance leaveOpeningBalance : lobFromDb) {
            lobMap.put(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
                    + leaveOpeningBalance.getCalendarYear(), leaveOpeningBalance);
        }
        for (LeaveOpeningBalance leaveOpeningBalance : leaveOpeningBalanceRequest.getLeaveOpeningBalance()) {
            errorMsg = "";
            if (lobMap.get(leaveOpeningBalance.getEmployee() + "-" + leaveOpeningBalance.getLeaveType().getId() + "-"
                    + leaveOpeningBalance.getCalendarYear()) == null) {
                errorMsg = "Leave opening balance already present in the system for this Employee";
            }
        }

        return leaveOpeningBalanceRequest.getLeaveOpeningBalance();
    }

    public ResponseEntity<?> updateLeaveOpeningBalance(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
        List<LeaveOpeningBalance> leaveOpeningBalance = leaveOpeningBalanceRequest.getLeaveOpeningBalance();
        kafkaTemplate.send(leaveOpeningBalanceUpdateTopic, leaveOpeningBalanceRequest);
        return getSuccessResponseForCreate(leaveOpeningBalance, leaveOpeningBalanceRequest.getRequestInfo());
    }

    /**
     * Populate LeaveOpeningBalanceResponse object & returns ResponseEntity of
     * type LeaveOpeningBalanceResponse containing ResponseInfo & array of
     * LeaveOpeningBalance objects
     *
     * @param leaveOpeningBalance
     * @param requestInfo
     * @return ResponseEntity<?>
     */
    public ResponseEntity<?> getSuccessResponseForCreate(List<LeaveOpeningBalance> leaveOpeningBalance,
                                                         RequestInfo requestInfo) {
        LeaveOpeningBalanceResponse leaveOpeningBalanceResponse = new LeaveOpeningBalanceResponse();
        leaveOpeningBalanceResponse.getLeaveOpeningBalance().addAll(leaveOpeningBalance);

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        leaveOpeningBalanceResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<LeaveOpeningBalanceResponse>(leaveOpeningBalanceResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getSuccessResponseForUpload(List<LeaveOpeningBalance> successLeaveOpeningBalanceList,
                                                         List<LeaveOpeningBalance> errorLeaveOpeningBalanceList, RequestInfo requestInfo) {
        LeaveOpeningBalanceUploadResponse leaveOpeningBalanceResponse = new LeaveOpeningBalanceUploadResponse();
        leaveOpeningBalanceResponse.getSuccessList().addAll(successLeaveOpeningBalanceList);
        leaveOpeningBalanceResponse.getErrorList().addAll(errorLeaveOpeningBalanceList);

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        leaveOpeningBalanceResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<LeaveOpeningBalanceUploadResponse>(leaveOpeningBalanceResponse, HttpStatus.OK);
    }

    public void create(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
        leaveOpeningBalanceRepository.create(leaveOpeningBalanceRequest);
    }

    public void update(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest) {
        leaveOpeningBalanceRepository.update(leaveOpeningBalanceRequest);
    }
}