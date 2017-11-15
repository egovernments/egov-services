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
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.LeaveType;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.CommonMastersRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.LeaveApplicationRepository;
import org.egov.eis.util.ApplicationConstants;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.max;

@Service
public class LeaveApplicationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveApplicationService.class);

    @Value("${kafka.topics.leaveapplication.create.name}")
    private String leaveApplicationCreateTopic;

    @Value("${kafka.topics.leaveapplication.update.name}")
    private String leaveApplicationUpdateTopic;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private CommonMastersRepository commonMastersRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HRStatusService hrStatusService;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private HRConfigurationService hrConfigurationService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private LeaveApplicationNumberGeneratorService leaveApplicationNumberGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    public List<LeaveApplication> getLeaveApplications(final LeaveApplicationGetRequest leaveApplicationGetRequest,
                                                       final RequestInfo requestInfo) {
        return leaveApplicationRepository.findForCriteria(leaveApplicationGetRequest, requestInfo);
    }

    public ResponseEntity<?> createLeaveApplication(final LeaveApplicationRequest leaveApplicationRequest, final String type) {
        final Boolean isExcelUpload = type != null && "upload".equalsIgnoreCase(type);
        final List<LeaveApplication> leaveApplicationsList = validate(leaveApplicationRequest, isExcelUpload);
        final List<LeaveApplication> successLeaveApplicationsList = new ArrayList<>();
        final List<LeaveApplication> errorLeaveApplicationsList = new ArrayList<>();
        leaveApplicationRequest.setType(type);
        for (final LeaveApplication leaveApplication : leaveApplicationsList)
            if (leaveApplication.getErrorMsg().isEmpty())
                successLeaveApplicationsList.add(leaveApplication);
            else
                errorLeaveApplicationsList.add(leaveApplication);
        leaveApplicationRequest.setLeaveApplication(successLeaveApplicationsList);
        for (final LeaveApplication leaveApplication : leaveApplicationRequest.getLeaveApplication()) {
            if (isExcelUpload)
                leaveApplication.setStatus(hrStatusService.getHRStatuses("APPROVED", leaveApplication.getTenantId(),
                        leaveApplicationRequest.getRequestInfo()).get(0).getId());
            else
                leaveApplication.setStatus(hrStatusService.getHRStatuses("APPLIED", leaveApplication.getTenantId(),
                        leaveApplicationRequest.getRequestInfo()).get(0).getId());
            leaveApplication.setApplicationNumber(leaveApplicationNumberGeneratorService.generate());
        }
        kafkaTemplate.send(leaveApplicationCreateTopic, leaveApplicationRequest);
        if (isExcelUpload)
            return getSuccessResponseForUpload(successLeaveApplicationsList, errorLeaveApplicationsList,
                    leaveApplicationRequest.getRequestInfo());
        else
            return getSuccessResponseForCreate(leaveApplicationsList, leaveApplicationRequest.getRequestInfo());
    }

    public List<LeaveApplication> getLeaveApplicationsReport(final LeaveSearchRequest leaveSearchRequest, final RequestInfo requestInfo) {

        List<Long> employeeIds = null;
        EmployeeInfoResponse employeeResponse = employeeRepository.getEmployeesForLeaveRequest(leaveSearchRequest, requestInfo);
        employeeIds = employeeResponse.getEmployees().stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
        leaveSearchRequest.setEmployeeIds(employeeIds);

        return leaveApplicationRepository.findForReportCriteria(leaveSearchRequest, requestInfo);

    }

    private List<LeaveApplication> validate(final LeaveApplicationRequest leaveApplicationRequest, final Boolean isExcelUpload) {
        String errorMsg = "";
        for (final LeaveApplication leaveApplication : leaveApplicationRequest.getLeaveApplication()) {
            errorMsg = "";
            final LeaveTypeGetRequest leaveTypeGetRequest = new LeaveTypeGetRequest();
            List<LeaveType> leaveTypes = new ArrayList<>();
            if (leaveApplication.getCompensatoryForDate() == null || leaveApplication.getCompensatoryForDate().equals("")) {
                leaveTypeGetRequest.setId(new ArrayList<>(Arrays.asList(leaveApplication.getLeaveType().getId())));
                leaveTypes = leaveTypeService.getLeaveTypes(leaveTypeGetRequest);
            }
            final List<EmployeeInfo> employees = employeeRepository.getEmployeeById(leaveApplicationRequest.getRequestInfo(), leaveApplication.getTenantId(), leaveApplication.getEmployee());
            final List<LeaveApplication> applications = getLeaveApplicationForDateRange(leaveApplication,
                    leaveApplicationRequest.getRequestInfo());
            if (leaveTypes.isEmpty() && (leaveApplication.getCompensatoryForDate() == null || leaveApplication.getCompensatoryForDate().equals("")))
                errorMsg = applicationConstants.getErrorMessage(ApplicationConstants.MSG_LEAVETYPE_NOTPRESENT) + " ";
            if (leaveApplication.getFromDate().after(leaveApplication.getToDate()))
                errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_FROMDATE_TODATE) + " ";
            if (isExcelUpload) {
                Date cutOffDate = null;
                try {
                    cutOffDate = hrConfigurationService.getCuttOffDate(leaveApplication.getTenantId(),
                            leaveApplicationRequest.getRequestInfo());
                } catch (final ParseException e) {
                    errorMsg = errorMsg + e.getMessage() + " ";
                }
                if (cutOffDate == null || leaveApplication.getFromDate().after(cutOffDate))
                    errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_FROMDATE_CUTOFFDATE)
                            + " ";
            }
            if (leaveApplication.getCompensatoryForDate() != null && !leaveApplication.getCompensatoryForDate().equals("") && leaveApplication.getStateId() == null) {
                final List<Holiday> holidays = commonMastersRepository.getHolidayByDate(leaveApplicationRequest.getRequestInfo(), leaveApplication.getFromDate(), leaveApplication.getTenantId());

                if (holidays.size() > 0)
                    errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_DATE_HOLIDAY);
                if (employees.size() > 0) {

                    if (employees.get(0).getDateOfAppointment() != null && !employees.get(0).getDateOfAppointment().equals("") && leaveApplication.getFromDate().before(employees.get(0).getDateOfAppointment()))
                        errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_APPOINTMENT_DATE);

                    if (employees.get(0).getDateOfRetirement() != null && !employees.get(0).getDateOfRetirement().equals("") && leaveApplication.getFromDate().after(employees.get(0).getDateOfRetirement()))
                        errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_RETIREMENT_DATE);
                }

                Long statusId = hrStatusService.getHRStatuses(LeaveStatus.REJECTED.toString(), leaveApplication.getTenantId(),
                        leaveApplicationRequest.getRequestInfo()).get(0).getId();


                LeaveApplication leaveApp = leaveApplicationRepository.getLeaveApplicationForDate(leaveApplication.getEmployee(), leaveApplication.getCompensatoryForDate(), leaveApplication.getTenantId());

                if (leaveApp != null && leaveApp.getStatus() != statusId)
                    errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_COMPENSATORYDATE_PRESENT);

            }

            if (employees.size() > 0) {
                List<Assignment> assignments = employees.get(0).getAssignments().stream()
                        .filter(assign -> assign.getIsPrimary().equals(true)).collect(Collectors.toList());

                List<Date> todate = assignments.stream().map(assign -> assign.getToDate()).collect(Collectors.toList());

                if (max(todate).before(leaveApplication.getToDate())) {
                    errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_ASSIGNMENT_TODATE);
                }
            }

            if (!applications.isEmpty())
                errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_ALREADY_PRESENT);
            leaveApplication.setErrorMsg(errorMsg);

        }
        return leaveApplicationRequest.getLeaveApplication();
    }

    public LeaveApplicationRequest create(final LeaveApplicationRequest leaveApplicationRequest) {
        if (leaveApplicationRequest.getLeaveApplication().size() > 0 && leaveApplicationRequest.getLeaveApplication().get(0).getLeaveType() != null)
            return leaveApplicationRepository.saveLeaveApplication(leaveApplicationRequest);
        else
            return leaveApplicationRepository.saveCompoffLeaveApplication(leaveApplicationRequest);
    }

    public ResponseEntity<?> updateLeaveApplication(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final LeaveApplicationRequest applicationRequest = new LeaveApplicationRequest();
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        leaveApplications.add(leaveApplicationRequest.getLeaveApplication());
        applicationRequest.setLeaveApplication(leaveApplications);
        applicationRequest.setRequestInfo(leaveApplicationRequest.getRequestInfo());
        leaveApplications = validate(applicationRequest, false);
        if (leaveApplications.get(0).getErrorMsg().isEmpty()) {
            final LeaveApplicationGetRequest leaveApplicationGetRequest = new LeaveApplicationGetRequest();
            final List<Long> ids = new ArrayList<>();
            ids.add(leaveApplications.get(0).getId());
            leaveApplicationGetRequest.setId(ids);
            final List<LeaveApplication> oldApplications = leaveApplicationRepository.findForCriteria(leaveApplicationGetRequest,
                    leaveApplicationRequest.getRequestInfo());
            leaveApplications.get(0).setStatus(oldApplications.get(0).getStatus());
            leaveApplications.get(0).setStateId(oldApplications.get(0).getStateId());
            kafkaTemplate.send(leaveApplicationUpdateTopic, leaveApplicationRequest);
            leaveApplicationStatusChange(leaveApplications.get(0), leaveApplicationRequest.getRequestInfo());
        }
        return getSuccessResponseForCreate(leaveApplications, leaveApplicationRequest.getRequestInfo());
    }

    private ResponseEntity<?> getSuccessResponseForCreate(final List<LeaveApplication> leaveApplicationsList,
                                                          final RequestInfo requestInfo) {
        final LeaveApplicationResponse leaveApplicationRes = new LeaveApplicationResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        if (!leaveApplicationsList.get(0).getErrorMsg().isEmpty())
            httpStatus = HttpStatus.BAD_REQUEST;
        leaveApplicationRes.setLeaveApplication(leaveApplicationsList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(httpStatus.toString());
        leaveApplicationRes.setResponseInfo(responseInfo);
        return new ResponseEntity<LeaveApplicationResponse>(leaveApplicationRes, httpStatus);
    }

    private ResponseEntity<?> getSuccessResponseForUpload(final List<LeaveApplication> successLeaveApplicationsList,
                                                          final List<LeaveApplication> errorLeaveApplicationsList, final RequestInfo requestInfo) {
        final LeaveApplicationUploadResponse leaveApplicationUploadResponse = new LeaveApplicationUploadResponse();
        leaveApplicationUploadResponse.getSuccessList().addAll(successLeaveApplicationsList);
        leaveApplicationUploadResponse.getErrorList().addAll(errorLeaveApplicationsList);

        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        leaveApplicationUploadResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<LeaveApplicationUploadResponse>(leaveApplicationUploadResponse, HttpStatus.OK);
    }

    private void leaveApplicationStatusChange(final LeaveApplication leaveApplication, final RequestInfo requestInfo) {
        final String workFlowAction = leaveApplication.getWorkflowDetails().getAction();
        if ("Approve".equalsIgnoreCase(workFlowAction))
            leaveApplication
                    .setStatus(hrStatusService.getHRStatuses(LeaveStatus.APPROVED.toString(), leaveApplication.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            leaveApplication
                    .setStatus(hrStatusService.getHRStatuses(LeaveStatus.REJECTED.toString(), leaveApplication.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Cancel".equalsIgnoreCase(workFlowAction))
            leaveApplication
                    .setStatus(hrStatusService.getHRStatuses(LeaveStatus.CANCELLED.toString(), leaveApplication.getTenantId(),
                            requestInfo).get(0).getId());
        else if ("Submit".equalsIgnoreCase(workFlowAction))
            leaveApplication
                    .setStatus(hrStatusService.getHRStatuses(LeaveStatus.RESUBMITTED.toString(), leaveApplication.getTenantId(),
                            requestInfo).get(0).getId());
    }

    public LeaveApplication update(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        return leaveApplicationRepository.updateLeaveApplication(leaveApplicationRequest);
    }

    public List<LeaveApplication> getLeaveApplicationForDateRange(final LeaveApplication leaveApplication,
                                                                  final RequestInfo requestInfo) {
        return leaveApplicationRepository.getLeaveApplicationForDateRange(leaveApplication, requestInfo);
    }

}