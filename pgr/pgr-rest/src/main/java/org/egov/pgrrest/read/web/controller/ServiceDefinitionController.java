package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.common.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceDefinitionService;
import org.egov.pgrrest.read.web.contract.ServiceDefinitionRequest;
import org.egov.pgrrest.read.web.contract.ServiceDefinitionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicedefinition")
public class ServiceDefinitionController {

    private ServiceDefinitionService serviceDefinitionService;

    public ServiceDefinitionController(ServiceDefinitionService serviceDefinitionService) {
        this.serviceDefinitionService = serviceDefinitionService;
    }

    @PostMapping("/_search")
    public ServiceDefinitionResponse getServiceDefinition(@RequestParam("serviceCode") String serviceCode,
                                                          @RequestParam("tenantId") String tenantId) {
        final ServiceDefinition serviceDefinition = serviceDefinitionService.
            find(new ServiceDefinitionSearchCriteria(serviceCode, tenantId));
        return new ServiceDefinitionResponse(null, serviceDefinition);
    }

    @PostMapping("/_create")
    public ServiceDefinitionResponse createServiceDefinition(@RequestBody ServiceDefinitionRequest request) {
        final ServiceDefinition serviceDefinition = serviceDefinitionService.create();
        return new ServiceDefinitionResponse(null, serviceDefinition);
    }
}
