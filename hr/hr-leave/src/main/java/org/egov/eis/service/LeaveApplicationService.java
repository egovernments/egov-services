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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.eis.broker.LeaveApplicationProducer;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.LeaveType;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.LeaveApplicationRepository;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.LeaveApplicationGetRequest;
import org.egov.eis.web.contract.LeaveApplicationRequest;
import org.egov.eis.web.contract.LeaveApplicationResponse;
import org.egov.eis.web.contract.LeaveApplicationSingleRequest;
import org.egov.eis.web.contract.LeaveApplicationUploadResponse;
import org.egov.eis.web.contract.LeaveTypeGetRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveApplicationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveApplicationService.class);

    @Value("${kafka.topics.leaveapplication.create.name}")
    private String leaveApplicationCreateTopic;

    @Value("${kafka.topics.leaveapplication.create.key}")
    private String leaveApplicationCreateKey;

    @Value("${kafka.topics.leaveapplication.update.name}")
    private String leaveApplicationUpdateTopic;

    @Value("${kafka.topics.leaveapplication.update.key}")
    private String leaveApplicationUpdateKey;

    @Autowired
    private LeaveApplicationProducer leaveApplicationProducer;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

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

    public List<LeaveApplication> getLeaveApplications(final LeaveApplicationGetRequest leaveApplicationGetRequest,
            final RequestInfo requestInfo) {
        return leaveApplicationRepository.findForCriteria(leaveApplicationGetRequest, requestInfo);
    }

    public ResponseEntity<?> createLeaveApplication(final LeaveApplicationRequest leaveApplicationRequest) {
        final Boolean isExcelUpload = leaveApplicationRequest.getLeaveApplication().size() > 1 ? true : false;
        final List<LeaveApplication> leaveApplicationsList = validate(leaveApplicationRequest, isExcelUpload);
        final List<LeaveApplication> successLeaveApplicationsList = new ArrayList<>();
        final List<LeaveApplication> errorLeaveApplicationsList = new ArrayList<>();
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
        String leaveApplicationRequestJson = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            leaveApplicationRequestJson = mapper.writeValueAsString(leaveApplicationRequest);
            LOGGER.info("leaveApplicationRequestJson::" + leaveApplicationRequestJson);
        } catch (final JsonProcessingException e) {
            LOGGER.error("Error while converting Leave Application to JSON", e);
            e.printStackTrace();
        }
        try {
            leaveApplicationProducer.sendMessage(leaveApplicationCreateTopic, leaveApplicationCreateKey,
                    leaveApplicationRequestJson);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        if (isExcelUpload)
            return getSuccessResponseForUpload(successLeaveApplicationsList, errorLeaveApplicationsList,
                    leaveApplicationRequest.getRequestInfo());
        else
            return getSuccessResponseForCreate(leaveApplicationsList, leaveApplicationRequest.getRequestInfo());
    }

    private List<LeaveApplication> validate(final LeaveApplicationRequest leaveApplicationRequest, final Boolean isExcelUpload) {
        String errorMsg = "";
        for (final LeaveApplication leaveApplication : leaveApplicationRequest.getLeaveApplication()) {
            errorMsg = "";
            final LeaveTypeGetRequest leaveTypeGetRequest = new LeaveTypeGetRequest();
            leaveTypeGetRequest.setId(new ArrayList<>(Arrays.asList(leaveApplication.getLeaveType().getId())));
            final List<LeaveType> leaveTypes = leaveTypeService.getLeaveTypes(leaveTypeGetRequest);
            final List<LeaveApplication> applications = getLeaveApplicationForDateRange(leaveApplication,
                    leaveApplicationRequest.getRequestInfo());
            if (leaveTypes.isEmpty())
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
            if (!applications.isEmpty())
                errorMsg = errorMsg + applicationConstants.getErrorMessage(ApplicationConstants.MSG_ALREADY_PRESENT);
            leaveApplication.setErrorMsg(errorMsg);
        }
        return leaveApplicationRequest.getLeaveApplication();
    }

    public LeaveApplicationRequest create(final LeaveApplicationRequest leaveApplicationRequest) {
        return leaveApplicationRepository.saveLeaveApplication(leaveApplicationRequest);
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
            String leaveApplicationRequestJson = null;
            try {
                final ObjectMapper mapper = new ObjectMapper();
                leaveApplicationRequestJson = mapper.writeValueAsString(leaveApplicationRequest);
                LOGGER.info("leaveApplicationRequestJson::" + leaveApplicationRequestJson);
            } catch (final JsonProcessingException e) {
                LOGGER.error("Error while converting Leave Application to JSON", e);
                e.printStackTrace();
            }
            try {
                leaveApplicationProducer.sendMessage(leaveApplicationUpdateTopic, leaveApplicationUpdateKey,
                        leaveApplicationRequestJson);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
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