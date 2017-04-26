package org.egov.pgr.read.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.contract.ServiceRequest;
import org.egov.pgr.common.contract.SevaRequest;
import org.egov.pgr.read.domain.model.Complaint;
import org.egov.pgr.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.read.domain.service.ComplaintService;
import org.egov.pgr.read.web.contract.RequestInfoBody;
import org.egov.pgr.read.web.contract.ResponseInfo;
import org.egov.pgr.read.web.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/seva"})
public class ComplaintController {

    private ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping(value = "/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createServiceRequest(@RequestBody SevaRequest request) {
        final Complaint complaint = request.toDomainForCreateRequest();
        complaintService.save(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(request.getServiceRequest()));
    }

    @PostMapping(value = "/_update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse updateServiceRequest(@RequestBody SevaRequest request) {
        final Complaint complaint = request.toDomainForUpdateRequest();
        complaintService.update(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(new ServiceRequest(complaint)));
    }

    @PostMapping(value = "/_search")
    public ServiceResponse getServiceRequests(@RequestParam("tenantId") String tenantId,
                                              @RequestParam(value = "service_request_id", required = false) String
                                                  serviceRequestId,
                                              @RequestParam(value = "service_code", required = false) String
                                                  serviceCode,
                                              @RequestParam(value = "start_date", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date startDate,
                                              @RequestParam(value = "end_date", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date endDate,
                                              @RequestParam(value = "escalation_date", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy HH:mm:ss") Date escalationDate,
                                              @RequestParam(value = "status", required = false) List<String> status,
                                              @RequestParam(value = "last_modified_datetime", required = false)
                                              @DateTimeFormat(pattern = "dd-MM-yyyy") Date lastModifiedDate,
                                              @RequestParam(value = "assignment_id", required = false) Long
                                                  assignmentId,
                                              @RequestParam(value = "user_id", required = false) Long userId,
                                              @RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "mobile_number", required = false) String
                                                  mobileNumber,
                                              @RequestParam(value = "email_id", required = false) String emailId,
                                              @RequestParam(value = "receiving_mode", required = false) Long
                                                  receivingMode,
                                              @RequestParam(value = "location_id", required = false) Long locationId,
                                              @RequestParam(value = "child_location_id", required = false) Long
                                                  childLocationId,
                                              @RequestBody RequestInfoBody requestInfoBody) {

        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder().assignmentId(assignmentId)
            .endDate(endDate).lastModifiedDatetime(lastModifiedDate).serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId).startDate(startDate).escalationDate(escalationDate).status(status).userId(userId).name(name)
            .mobileNumber(mobileNumber).emailId(emailId).receivingMode(receivingMode).locationId(locationId)
            .childLocationId(childLocationId).tenantId(tenantId).build();
        final List<Complaint> complaints = complaintService.findAll(complaintSearchCriteria);
        return createResponse(complaints);
    }

    @PostMapping(value = "/updateLastAccessedTime")
    @ResponseBody
    public void updateLastAccessedTime(@RequestParam final String serviceRequestId,
                                       @RequestBody RequestInfoBody requestInfo, @RequestParam(value = "tenantId", required = true) final String tenantId) {
        complaintService.updateLastAccessedTime(serviceRequestId,tenantId);
    }

    private ServiceResponse createResponse(List<Complaint> complaints) {
        final List<ServiceRequest> serviceRequests = complaints.stream().map(ServiceRequest::new)
            .collect(Collectors.toList());
        return new ServiceResponse(null, serviceRequests);
    }

    private ResponseInfo getResponseInfo(SevaRequest request) {
        final RequestInfo requestInfo = request.getRequestInfo();
        return ResponseInfo.builder()
            .apiId(requestInfo.getApiId())
            .ver(requestInfo.getVer())
            .ts(new Date().toString())
            .msgId(requestInfo.getMsgId())
            .resMsgId("placeholder")
            .status("placeholder")
            .build();
    }

}