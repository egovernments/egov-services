package org.egov.wcms.web.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.service.ServiceChargeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.egov.wcms.web.contract.ServiceChargeRes;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/serviceCharges")
public class ServiceChargeController {
    public static final Logger logger = LoggerFactory.getLogger(ServiceChargeController.class);
    
    @Autowired
    private ValidatorUtils validatorUtils;
    
    @Autowired
    private ServiceChargeService serviceChargeService;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    @PostMapping(value="/_create")
    @ResponseBody
    public ResponseEntity<?> createServiceCharges(@RequestBody ServiceChargeReq serviceChargeRequest,
            final BindingResult errors){
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ServiceChargeReq::" + serviceChargeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateServiceChargeRequest(serviceChargeRequest,false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);        
        List<ServiceCharge> listOfServiceCharges = serviceChargeService.pushServiceChargeCreateRequestToQueue(serviceChargeRequest);
        return getSuccessResponse(listOfServiceCharges,"Created",serviceChargeRequest.getRequestInfo());
    }
    
    @PostMapping(value="/_update")
    @ResponseBody
    public ResponseEntity<?> updateServiceCharges(@RequestBody ServiceChargeReq serviceChargeRequest,
            final BindingResult errors){
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ServiceChargeReq::" + serviceChargeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateServiceChargeRequest(serviceChargeRequest,true);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);        
        List<ServiceCharge> listOfServiceCharges = serviceChargeService.pushServiceChargeUpdateRequestToQueue(serviceChargeRequest);
        return getSuccessResponse(listOfServiceCharges,null,serviceChargeRequest.getRequestInfo());
        
    }
    
    private ResponseEntity<?> getSuccessResponse(final List<ServiceCharge> serviceCharges,
            final String mode, final RequestInfo requestInfo) {
        final ServiceChargeRes serviceChargeResponse = new ServiceChargeRes();
        serviceChargeResponse.setServiceCharge(serviceCharges);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        serviceChargeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(serviceChargeResponse, HttpStatus.OK);
    }

}
