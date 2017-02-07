package org.egov.pgr.controller;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.exception.UnauthorizedAccessException;
import org.egov.pgr.factory.ResponseInfoFactory;
import org.egov.pgr.factory.ServiceRequestsFactory;
import org.egov.pgr.model.*;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.SevaNumberGeneratorService;
import org.egov.pgr.repository.specification.SevaSpecification;
import org.egov.pgr.validators.SevaRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = { "/seva" })
public class ServiceRequestController {

    @Autowired
    private SevaNumberGeneratorService sevaNumberGeneratorImpl;

    @Autowired
    private GrievanceProducer kafkaProducer;

    @Autowired
    private ComplaintService complaintService;

    private ResponseInfo resInfo = null;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new SevaRequestValidator());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ServiceRequestRes createServiceRequest(@RequestParam String jurisdiction_id,
            @Valid @RequestBody SevaRequest request) throws Exception {
        try {
            User user = null;
            RequestInfo requestInfo = request.getRequestInfo();
            String token = requestInfo.getAuthToken();
            if (token != null)
                user = retrieveUser(token);
            if (user != null) {
                ServiceRequest serviceRequest = request.getServiceRequest();
                serviceRequest.setFirstName(user.getName());
                serviceRequest.setPhone(user.getMobileNumber());
                serviceRequest.setEmail(user.getEmailId());
            }
            String newComplaintId = sevaNumberGeneratorImpl.generate();
            request.getServiceRequest().setCrn(newComplaintId);
            request.getRequestInfo().setAction("create");
            kafkaProducer.sendMessage(jurisdiction_id + ".mseva.validated", request);
            resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324",
                    requestInfo.getMsgId(), "true");
            ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
            serviceRequestResponse.setResposneInfo(resInfo);
            serviceRequestResponse.getServiceRequests().add(request.getServiceRequest());
            return serviceRequestResponse;
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ServiceRequestRes updateServiceRequest(@RequestParam String jurisdiction_id,
            @Valid @RequestBody SevaRequest request) throws Exception {
        try {
            User user = null;
            RequestInfo requestInfo = request.getRequestInfo();
            String token = requestInfo.getAuthToken();
            if (token != null)
                user = retrieveUser(token);
            if (user != null) {
                ServiceRequest serviceRequest = request.getServiceRequest();
                serviceRequest.setFirstName(user.getName());
                serviceRequest.setPhone(user.getMobileNumber());
                serviceRequest.setEmail(user.getEmailId());
            }
            request.getRequestInfo().setAction("update");
            kafkaProducer.sendMessage(jurisdiction_id + ".mseva.validated", request);
            resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324",
                    requestInfo.getMsgId(), "true");
            ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
            serviceRequestResponse.setResposneInfo(resInfo);
            serviceRequestResponse.getServiceRequests().add(request.getServiceRequest());
            return serviceRequestResponse;
        } catch (Exception exception) {
            throw exception;
        }
    }

    
    private User retrieveUser(String token) {
        // TODO - Externalize the url to the user details
        String url = String.format("http://localhost:8080/user/details?access_token=%s", token);
        return new RestTemplate().getForObject(url, User.class);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = { "api_id", "ver", "ts",
            "action", "did", "msg_id", "requester_id", "auth_token" })
    @ResponseBody
    public ServiceRequestRes getServiceRequests(@RequestParam("jurisdiction_id") Long jurisdictionId,
            @RequestParam(value = "service_request_id", required = false) String serviceRequestId,
            @RequestParam(value = "service_code", required = false) String serviceCode,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "last_modified_datetime", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date lastModifiedDate,
            @RequestHeader HttpHeaders headers) throws UnauthorizedAccessException {
        try {

            // validateAuthToken(headers.getFirst("auth_token"));
            SevaSearchCriteria sevaSearchCriteria = new SevaSearchCriteria(serviceRequestId, serviceCode, startDate,
                    endDate, status, lastModifiedDate);
            SevaSpecification sevaSpecification = new SevaSpecification(sevaSearchCriteria);
            List<Complaint> complaints = complaintService.findAll(sevaSpecification);

            ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
            serviceRequestResponse.setServiceRequests(ServiceRequestsFactory.createServiceRequestsFromComplaints(complaints));
            ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestHeaders(headers, true);
            serviceRequestResponse.setResposneInfo(responseInfo);
            return serviceRequestResponse;
        } catch (Exception exception) {
            throw exception;
        }
    }

    private void validateAuthToken(String authToken) throws UnauthorizedAccessException {
        User user = null;
        user = retrieveUser(authToken);
        if (user == null) {
            throw new UnauthorizedAccessException();
        }
    }
}