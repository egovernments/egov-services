package org.egov.pgr.web.controller;

import org.egov.pgr.domain.service.ServiceTypeConfigurationService;
import org.egov.pgr.web.contract.ServiceTypeConfigurationRequest;
import org.egov.pgr.web.contract.ServiceTypeConfigurationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicetypeconfiguration")
public class ServiceTypeConfigurationController {

    private ServiceTypeConfigurationService serviceTypeConfigurationService;

    public ServiceTypeConfigurationController(ServiceTypeConfigurationService serviceTypeConfigurationService) {
        this.serviceTypeConfigurationService = serviceTypeConfigurationService;
    }

    @PostMapping("/v1/_create")
    public ServiceTypeConfigurationResponse create(@RequestBody ServiceTypeConfigurationRequest serviceTypeConfigurationRequest){

        serviceTypeConfigurationService.create(serviceTypeConfigurationRequest.getServiceTypeConfiguration().toDomain());

        return new ServiceTypeConfigurationResponse(null,serviceTypeConfigurationRequest.getServiceTypeConfiguration());
    }


}