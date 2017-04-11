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
import org.egov.pgr.read.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createServiceRequest(@RequestBody SevaRequest request) {
        final Complaint complaint = request.toDomainForCreateRequest();
        complaintService.save(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(request.getServiceRequest()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse updateServiceRequest(@RequestBody SevaRequest request) {
        final Complaint complaint = request.toDomainForUpdateRequest();
        complaintService.update(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(new ServiceRequest(complaint)));
    }

    @GetMapping(headers = {"api_id", "ver", "ts", "action", "did", "msg_id", "requester_id", "auth_token"})
    public ServiceResponse getServiceRequests(@RequestParam("jurisdiction_id") Long jurisdictionId,
                                              @RequestParam(value = "service_request_id", required = false) String
                                                  serviceRequestId,
                                              @RequestParam(value = "service_code", required = false) String
                                                      serviceCode,
                                              @RequestParam(value = "start_date", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date startDate,
                                              @RequestParam(value = "end_date", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date endDate,
                                              @RequestParam(value = "status", required = false)List <String> status,
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
                                              @RequestHeader HttpHeaders headers) {

        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder().assignmentId(assignmentId)
            .endDate(endDate).lastModifiedDatetime(lastModifiedDate).serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId).startDate(startDate).status(status).userId(userId).name(name)
            .mobileNumber(mobileNumber).emailId(emailId).receivingMode(receivingMode).locationId(locationId)
            .childLocationId(childLocationId).build();
        final List<Complaint> complaints = complaintService.findAll(complaintSearchCriteria);
        return createResponse(headers, complaints);
    }

    @PostMapping(value = "/updateLastAccessedTime")
    @ResponseBody
    public void updateLastAccessedTime(@RequestParam final String serviceRequestId,
                                       @RequestBody RequestInfoBody requestInfo) {
        complaintService.updateLastAccessedTime(serviceRequestId);
    }

    private ServiceResponse createResponse(@RequestHeader HttpHeaders headers, List<Complaint> complaints) {
        final List<ServiceRequest> serviceRequests = complaints.stream().map(ServiceRequest::new)
            .collect(Collectors.toList());

        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestHeaders(headers, true);
        return new ServiceResponse(responseInfo, serviceRequests);
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