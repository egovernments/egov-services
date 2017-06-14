package org.egov.pgrrest.read.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceRequestService;
import org.egov.pgrrest.read.web.contract.CountResponse;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.egov.pgrrest.read.web.contract.ServiceResponse;
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
public class ServiceRequestController {

    private ServiceRequestService serviceRequestService;

    @Autowired
    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping(value = "/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createServiceRequest(@RequestBody SevaRequest request) {
        final ServiceRequest complaint = request.toDomainForCreateRequest();
        serviceRequestService.save(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(request.getServiceRequest()));
    }

    @PostMapping(value = "/_update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse updateServiceRequest(@RequestBody SevaRequest request) {
        final ServiceRequest complaint = request.toDomainForUpdateRequest();
        serviceRequestService.update(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(new org.egov.pgrrest.common.contract
            .ServiceRequest(complaint)));
    }

    @PostMapping(value = "/_search")
    public ServiceResponse getServiceRequests(@RequestParam(value = "tenantId", required = false) String tenantId,
                                              @RequestParam(value = "serviceRequestId", required = false) String
                                                  serviceRequestId,
                                              @RequestParam(value = "serviceCode", required = false) String
                                                  serviceCode,
                                              @RequestParam(value = "startDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date startDate,
                                              @RequestParam(value = "endDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") Date endDate,
                                              @RequestParam(value = "escalationDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy HH:mm:ss") Date escalationDate,
                                              @RequestParam(value = "status", required = false) List<String> status,
                                              @RequestParam(value = "lastModifiedDatetime", required = false)
                                              @DateTimeFormat(pattern = "dd-MM-yyyy") Date lastModifiedDate,
                                              @RequestParam(value = "assignmentId", required = false) Long
                                                  assignmentId,
                                              @RequestParam(value = "userId", required = false) Long userId,
                                              @RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "mobileNumber", required = false) String
                                                  mobileNumber,
                                              @RequestParam(value = "emailId", required = false) String emailId,
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "receivingMode", required = false) Long
                                                  receivingMode,
                                              @RequestParam(value = "locationId", required = false) Long locationId,
                                              @RequestParam(value = "fromIndex", required = false) Integer fromIndex,
                                              @RequestParam(value = "sizePerPage", required = false) Integer pageSize,
                                              @RequestParam(value = "childLocationId", required = false) Long
                                                  childLocationId,
                                              @RequestBody RequestInfoBody requestInfoBody) {

        ServiceRequestSearchCriteria serviceRequestSearchCriteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(assignmentId)
            .endDate(endDate)
            .lastModifiedDatetime(lastModifiedDate)
            .serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId)
            .startDate(startDate)
            .escalationDate(escalationDate)
            .status(status)
            .userId(userId)
            .name(name)
            .mobileNumber(mobileNumber)
            .emailId(emailId)
            .receivingMode(receivingMode)
            .locationId(locationId)
            .childLocationId(childLocationId)
            .tenantId(tenantId)
            .keyword(keyword)
            .fromIndex(fromIndex)
            .pageSize(pageSize)
            .isAnonymous(checkIsAnonymous(requestInfoBody))
            .build();
        final List<ServiceRequest> submissions = serviceRequestService.findAll(serviceRequestSearchCriteria);
        return createResponse(submissions);
    }

    @PostMapping(value = "/_count")
    public CountResponse getServiceRequestCount(@RequestParam(value = "tenantId") String tenantId,
                                                @RequestParam(value = "serviceRequestId", required = false)
                                                    String serviceRequestId,
                                                @RequestParam(value = "serviceCode", required = false)
                                                    String serviceCode,
                                                @RequestParam(value = "startDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                                @RequestParam(value = "endDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                @RequestParam(value = "escalationDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
                                                    Date escalationDate,
                                                @RequestParam(value = "status", required = false) List<String> status,
                                                @RequestParam(value = "lastModifiedDatetime", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") Date lastModifiedDate,
                                                @RequestParam(value = "assignmentId", required = false) Long
                                                    assignmentId,
                                                @RequestParam(value = "userId", required = false) Long userId,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "mobileNumber", required = false) String
                                                    mobileNumber,
                                                @RequestParam(value = "emailId", required = false) String emailId,
                                                @RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "receivingMode", required = false) Long
                                                    receivingMode,
                                                @RequestParam(value = "locationId", required = false) Long locationId,
                                                @RequestParam(value = "childLocationId", required = false)
                                                    Long childLocationId,
                                                @RequestBody RequestInfoBody requestInfoBody) {

        ServiceRequestSearchCriteria serviceRequestSearchCriteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(assignmentId)
            .endDate(endDate)
            .lastModifiedDatetime(lastModifiedDate)
            .serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId)
            .startDate(startDate)
            .escalationDate(escalationDate)
            .status(status)
            .userId(userId)
            .name(name)
            .mobileNumber(mobileNumber)
            .emailId(emailId)
            .receivingMode(receivingMode)
            .locationId(locationId)
            .childLocationId(childLocationId)
            .tenantId(tenantId)
            .keyword(keyword)
            .build();
        final Long count = serviceRequestService.getCount(serviceRequestSearchCriteria);
        return new CountResponse(null, count);
    }


    private ServiceResponse createResponse(List<ServiceRequest> submissions) {
        final List<org.egov.pgrrest.common.contract.ServiceRequest> serviceRequests = submissions.stream()
            .map(org.egov.pgrrest.common.contract.ServiceRequest::new)
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

    private boolean checkIsAnonymous(RequestInfoBody request){
        return request == null || request.getRequestInfo() == null ||request.getRequestInfo().getUserInfo() == null ? true : false;
    }

}