package org.egov.pgr.controller;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.Error;
import org.egov.pgr.model.*;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.SevaNumberGeneratorServiceImpl;
import org.egov.pgr.validators.FieldErrorDTO;
import org.egov.pgr.validators.SevaRequestValidator;
import org.egov.pgr.validators.ValidationErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/seva"})
public class ServiceRequestController {

    @Autowired
    private SevaNumberGeneratorServiceImpl sevaNumberGeneratorImpl;

    @Autowired
    private GrievanceProducer kafkaProducer;

    @Autowired
    private ComplaintService complaintService;

    private ResponseInfo resInfo = null;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new SevaRequestValidator());
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ServiceRequestRes createServiceRequest(@RequestParam String jurisdiction_id,
                                                  @Valid @RequestBody SevaRequest request) throws Exception {
        try {
            User user = null;
            RequestInfo requestInfo = request.getRequestInfo();
            String token = requestInfo.getAuthToken();
            if (token != null) user = retrieveUser(token);
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
            resInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), new Date().toString(), "uief87324", requestInfo.getMsgId(), "true");
            ServiceRequestRes serviceRequestResponse = new ServiceRequestRes();
            serviceRequestResponse.setResposneInfo(resInfo);
            serviceRequestResponse.getServiceRequests().add(request.getServiceRequest());
            return serviceRequestResponse;
        } catch (Exception exception) {
            throw exception;
        }
    }

    private User retrieveUser(String token) {
        //TODO - Externalize the url to the user details
        String url = String.format("http://localhost:8080/user/details?access_token=%s", token);
        return new RestTemplate().getForObject(url, User.class);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handle(MethodArgumentNotValidException exception) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        dto.addFieldErrors(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleError(Exception ex) {
        ex.printStackTrace();
        ErrorRes response = new ErrorRes();
        response.setResposneInfo(resInfo);
        Error error = new Error();
        error.setCode(400);
        error.setDescription("General Server Error");
        response.setError(error);
        return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Complaint getAllComplaintTypeByCategory(@RequestParam Long complaintId,
                                                   @RequestParam String tenantId) {
        return complaintService.getComplaintById(complaintId);
    }
}