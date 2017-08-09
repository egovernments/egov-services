package org.egov.pgr.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.domain.service.ServiceTypeConfigurationService;
import org.egov.pgr.web.contract.RequestInfoBody;
import org.egov.pgr.web.contract.ServiceTypeConfigurationRequest;
import org.egov.pgr.web.contract.ServiceTypeConfigurationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/v1/_update")
    public ServiceTypeConfigurationResponse update(@RequestBody ServiceTypeConfigurationRequest serviceTypeConfigurationRequest){
        serviceTypeConfigurationService.update(serviceTypeConfigurationRequest.getServiceTypeConfiguration().toDomain());
        return new ServiceTypeConfigurationResponse(null,serviceTypeConfigurationRequest.getServiceTypeConfiguration());
    }
    
    @PostMapping("/v1/_search")
    public List<org.egov.pgr.web.contract.ServiceTypeConfiguration> search(@RequestBody RequestInfoBody requestInfoBody ,
    		@RequestParam(value = "serviceCode", required = false) String serviceCode,
            @RequestParam String tenantId){
    	ServiceTypeConfigurationSearchCriteria serviceTypeSearchCriteria = ServiceTypeConfigurationSearchCriteria.builder()
    															.serviceCode(serviceCode)
    															.tenantId(tenantId)
    															.build();
        List<ServiceTypeConfiguration> serviceTypeConfigurations = serviceTypeConfigurationService.search(serviceTypeSearchCriteria);

        return serviceTypeConfigurations.stream()
                .map(org.egov.pgr.web.contract.ServiceTypeConfiguration::new)
                .collect(Collectors.toList());
        		
    }
}

