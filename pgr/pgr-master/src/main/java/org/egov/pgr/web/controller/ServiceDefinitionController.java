package org.egov.pgr.web.controller;

import org.egov.pgr.domain.service.ServiceDefinitionService;
import org.egov.pgr.web.contract.ServiceDefinitionRequest;
import org.egov.pgr.web.contract.ServiceDefinitionResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicedefinition")
public class ServiceDefinitionController {


    private ServiceDefinitionService serviceDefinitionService;

    public ServiceDefinitionController(ServiceDefinitionService serviceDefinitionService) {
        this.serviceDefinitionService = serviceDefinitionService;
    }

    @PostMapping("/v1/_create")
    public ServiceDefinitionResponse create(@RequestBody ServiceDefinitionRequest request){
        serviceDefinitionService.create(request.toDomain(), request);
        return new ServiceDefinitionResponse(null, request.getServiceDefinition());
    }

}
