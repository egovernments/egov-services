package org.egov.pgrrest.read.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.exception.UpdateServiceRequestNotAllowedException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceRequestService;
import org.egov.pgrrest.read.domain.service.UpdateServiceRequestEligibilityService;
import org.egov.pgrrest.read.web.contract.CountResponse;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.egov.pgrrest.read.web.contract.ServiceResponse;
import org.egov.pgrrest.read.web.contract.SevaResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping(value = {"/seva"})
public class ServiceRequestController {

    private ServiceRequestService serviceRequestService;
    private UpdateServiceRequestEligibilityService updateEligibilityService;

    @Autowired
    public ServiceRequestController(ServiceRequestService serviceRequestService,
                                    UpdateServiceRequestEligibilityService updateEligibilityService) {
        this.serviceRequestService = serviceRequestService;
        this.updateEligibilityService = updateEligibilityService;
    }

    @PostMapping(value = "/v1/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createServiceRequest(@RequestBody SevaRequest request) {
        final ServiceRequest complaint = request.toDomainForCreateRequest();
        serviceRequestService.save(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(request.getServiceRequest()));
    }

    @PostMapping(value = "/v1/_update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse updateServiceRequest(@RequestBody SevaRequest request) {
        final ServiceRequest complaint = request.toDomainForUpdateRequest();
        serviceRequestService.update(complaint, request);
        ResponseInfo responseInfo = getResponseInfo(request);
        return new ServiceResponse(responseInfo, Collections.singletonList(new org.egov.pgrrest.common.contract.web.ServiceRequest(complaint)));
    }

    @PostMapping(value = "/v1/_search")
    public ServiceResponse getServiceRequests(@RequestParam(value = "tenantId", required = false) String tenantId,
                                              @RequestParam(value = "serviceRequestId", required = false) String
                                                  serviceRequestId,
                                              @RequestParam(value = "serviceCode", required = false) String
                                                  serviceCode,
                                              @RequestParam(value = "startDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") DateTime startDate,
                                              @RequestParam(value = "endDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy") DateTime endDate,
                                              @RequestParam(value = "escalationDate", required = false) @DateTimeFormat
                                                  (pattern = "dd-MM-yyyy HH:mm:ss") DateTime escalationDate,
                                              @RequestParam(value = "status", required = false) List<String> status,
                                              @RequestParam(value = "lastModifiedDatetime", required = false)
                                              @DateTimeFormat(pattern = "dd-MM-yyyy") DateTime lastModifiedDate,
                                              @RequestParam(value = "positionId", required = false) Long positionId,
                                              @RequestParam(value = "userId", required = false) Long userId,
                                              @RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
                                              @RequestParam(value = "emailId", required = false) String emailId,
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "receivingMode", required = false) String receivingMode,
                                              @RequestParam(value = "locationId", required = false) Long locationId,
                                              @RequestParam(value = "fromIndex", required = false) Integer fromIndex,
                                              @RequestParam(value = "sizePerPage", required = false) Integer pageSize,
                                              @RequestParam(value = "childLocationId", required = false) Long childLocationId,
                                              @RequestBody RequestInfoBody requestInfoBody) {

        boolean searchAttribute = searchAttributes(receivingMode, locationId, keyword);

        List<String> crnList = getCrnList(receivingMode, locationId, keyword);

        ServiceRequestSearchCriteria serviceRequestSearchCriteria = ServiceRequestSearchCriteria.builder()
            .positionId(positionId)
            .startDate(startDate)
            .endDate(endDate)
            .startLastModifiedDate(lastModifiedDate)
            .serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId)
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
            .searchAttribute(searchAttribute)
            .crnList(searchAttribute ? crnList : Collections.emptyList())
            .build();
        final List<ServiceRequest> submissions = serviceRequestService.findAll(serviceRequestSearchCriteria);
        return createResponse(submissions);
    }

    @PostMapping(value = "/v1/_count")
    public CountResponse getServiceRequestCount(@RequestParam(value = "tenantId") String tenantId,
                                                @RequestParam(value = "serviceRequestId", required = false)
                                                    String serviceRequestId,
                                                @RequestParam(value = "serviceCode", required = false)
                                                    String serviceCode,
                                                @RequestParam(value = "startDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") DateTime startDate,
                                                @RequestParam(value = "endDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") DateTime endDate,
                                                @RequestParam(value = "escalationDate", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
                                                    DateTime escalationDate,
                                                @RequestParam(value = "status", required = false) List<String> status,
                                                @RequestParam(value = "lastModifiedDatetime", required = false)
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") DateTime lastModifiedDate,
                                                @RequestParam(value = "positionId", required = false) Long positionId,
                                                @RequestParam(value = "userId", required = false) Long userId,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "mobileNumber", required = false) String
                                                    mobileNumber,
                                                @RequestParam(value = "emailId", required = false) String emailId,
                                                @RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "receivingMode", required = false)
                                                    String receivingMode,
                                                @RequestParam(value = "locationId", required = false) Long locationId,
                                                @RequestParam(value = "childLocationId", required = false)
                                                    Long childLocationId,
                                                @RequestBody RequestInfoBody requestInfoBody) {

        ServiceRequestSearchCriteria serviceRequestSearchCriteria = ServiceRequestSearchCriteria.builder()
            .positionId(positionId)
            .startDate(startDate)
            .endDate(endDate)
            .startLastModifiedDate(lastModifiedDate)
            .serviceCode(serviceCode)
            .serviceRequestId(serviceRequestId)
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

    @PostMapping(value = "/v1/_get")
    public SevaResponse getUpdateEligibity(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                           @RequestParam(value = "crn", required = false) String crn,
                                           @RequestBody RequestInfoBody requestInfoBody) {
        AuthenticatedUser authenticatedUser = requestInfoBody.toAuthenticatedUser();
        boolean isUpdateEligible = isEligibleToUpdate(crn, tenantId, authenticatedUser);
        return new SevaResponse(null, isUpdateEligible);
    }

    private boolean isEligibleToUpdate(String serviceRequestId, String tenantId,
                                       AuthenticatedUser authenticatedUser) {
        try {
            updateEligibilityService.validate(serviceRequestId, tenantId, authenticatedUser);
            return true;
        } catch (UpdateServiceRequestNotAllowedException ex) {
            return false;
        }
    }

    private ServiceResponse createResponse(List<ServiceRequest> submissions) {
        final List<org.egov.pgrrest.common.contract.web.ServiceRequest> serviceRequests = submissions.stream()
            .map(org.egov.pgrrest.common.contract.web.ServiceRequest::new)
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

    private boolean checkIsAnonymous(RequestInfoBody request) {
        return request != null && request.isAnonymous();
    }

    private boolean searchAttributes(String receivingMode, Long locationId, String keyword) {
        return null != locationId || !isEmpty(receivingMode) || !isEmpty(keyword);
    }

    private List<String> getCrnList(String receivingMode, Long locationId, String keyword) {
        return serviceRequestService.getCrnByAttributes(receivingMode, locationId, keyword);
    }
}