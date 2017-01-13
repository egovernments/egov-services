package org.egov.pgr.rest.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.rest.web.model.Error;
import org.egov.pgr.rest.web.model.ErrorRes;
import org.egov.pgr.rest.web.model.ResponseInfo;
import org.egov.pgr.rest.web.model.Service;
import org.egov.pgr.rest.web.model.ServiceDefinition;
import org.egov.pgr.rest.web.model.ServiceDefinitionRes;
import org.egov.pgr.rest.web.model.ServiceRes;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = { "/v1", "/a1" })
public class ServiceController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    private ResponseInfo resInfo = null;

    @RequestMapping(value = "/services", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ServiceRes getServiceList(@RequestParam String jurisdictionId,
            @RequestParam String api_id, @RequestParam String ver,
            @RequestParam String ts, @RequestParam String action,
            @RequestParam String did, @RequestParam String msg_id,
            @RequestParam String requester_id, @RequestParam String auth_token)
            throws IllegalAccessException, InvocationTargetException {
        try {
            resInfo = new ResponseInfo(api_id, ver, new Date()
                    .toString(), msg_id, requester_id, "Active");
            ServiceRes serviceRes = new ServiceRes();
            List<ComplaintType> complaintTypes = complaintTypeService.findAll();

            List<Service> serviceList = new ArrayList<>();

            for (ComplaintType complaintType : complaintTypes) {
                Service service = new Service();
                BeanUtils.copyProperties(complaintType, service);
                service.setCategory(complaintType.getCategory().getName());
                serviceList.add(service);
            }
            serviceRes.setServices(serviceList);
            serviceRes.setResposneInfo(resInfo);
            return serviceRes;
        } catch (Exception exception) {
            throw exception;
        }
    }

    @RequestMapping(value = "/servicedetail", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getServiceDetail(@RequestParam String jurisdiction_id,
            @RequestParam String service_code, @RequestParam String api_id,
            @RequestParam String ver, @RequestParam String ts,
            @RequestParam String action, @RequestParam String did,
            @RequestParam String msg_id, @RequestParam String requester_id,
            @RequestParam String auth_token) throws Exception {
        try {
            resInfo = new ResponseInfo(api_id, ver, new Date()
                    .toString(), msg_id, requester_id, "Active");
            Gson gson = new Gson();
            ComplaintType complaintType = complaintTypeService.findByCode(service_code);
            ServiceDefinitionRes response = new ServiceDefinitionRes();
            response.setServiceDefinition(new ServiceDefinition(complaintType.getCode(), complaintType.getAttributes()));
            response.setResposneInfo(resInfo);
            return gson.toJson(response, ServiceDefinitionRes.class);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleError(Exception ex) {
        ErrorRes response = new ErrorRes();
        response.setResposneInfo(resInfo);
        Error error = new Error();
        error.setCode(400);
        error.setDescription("General Server Error");
        response.setError(error);
        return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
    }
}