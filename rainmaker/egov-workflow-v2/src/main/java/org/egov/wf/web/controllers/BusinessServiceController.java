package org.egov.wf.web.controllers;


import org.egov.wf.service.BusinessMasterService;
import org.egov.wf.util.ResponseInfoFactory;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/egov-wf")
public class BusinessServiceController {

    private BusinessMasterService businessMasterService;

    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public BusinessServiceController(BusinessMasterService businessMasterService, ResponseInfoFactory responseInfoFactory) {
        this.businessMasterService = businessMasterService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value="/businessservice/_create", method = RequestMethod.POST)
    public ResponseEntity<BusinessServiceResponse> create(@Valid @RequestBody BusinessServiceRequest businessServiceRequest) {
        List<BusinessService> businessServices = businessMasterService.create(businessServiceRequest);
        BusinessServiceResponse response = BusinessServiceResponse.builder().businessServices(businessServices)
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(businessServiceRequest.getRequestInfo(),true))
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
