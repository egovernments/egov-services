package org.egov.pgr.web.controller;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.domain.service.ComplaintService;
import org.egov.pgr.domain.service.UserService;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.ServiceRequest;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.web.contract.ResponseInfo;
import org.egov.pgr.web.contract.ServiceResponse;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
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

    private UserService userService;
    private ComplaintService complaintService;

    @Autowired
    public ComplaintController(UserService userService, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintService = complaintService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createServiceRequest(@RequestBody SevaRequest request) {
        final AuthenticatedUser user = userService.getUser(request.getAuthToken());
        final Complaint complaint = request.toDomainForCreateRequest(user);
        complaintService.save(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(request.getServiceRequest()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse updateServiceRequest(@RequestBody SevaRequest request) {
        RequestInfo requestInfo = request.getRequestInfo();
        final AuthenticatedUser user = userService.getUser(requestInfo.getAuthToken());
        final Complaint complaint = request.toDomainForUpdateRequest(user);
        complaintService.update(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(new ServiceRequest(complaint)));
    }

    @GetMapping(headers = {"api_id", "ver", "ts", "action", "did", "msg_id", "requester_id", "auth_token"})
    public ServiceResponse getServiceRequests(@RequestParam("jurisdiction_id") Long jurisdictionId,
                                              @RequestParam(value = "service_request_id", required = false)
                                                             String serviceRequestId,
                                              @RequestParam(value = "service_code", required = false)
                                                             String serviceCode,
                                              @RequestParam(value = "start_date", required = false)
                                                     @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                              @RequestParam(value = "end_date", required = false)
                                                     @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                              @RequestParam(value = "status", required = false) String status,
                                              @RequestParam(value = "last_modified_datetime", required = false)
                                                     @DateTimeFormat(pattern = "dd-MM-yyyy") Date lastModifiedDate,
                                              @RequestHeader HttpHeaders headers) {
        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder()
                .serviceRequestId(serviceRequestId)
                .serviceCode(serviceCode)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .lastModifiedDatetime(lastModifiedDate)
                .build();
        final List<Complaint> complaints = complaintService.findAll(complaintSearchCriteria);
        return createResponse(headers, complaints);
    }

    private ServiceResponse createResponse(@RequestHeader HttpHeaders headers, List<Complaint> complaints) {
        final List<ServiceRequest> serviceRequests = complaints
                .stream()
                .map(ServiceRequest::new)
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