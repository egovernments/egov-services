package org.egov.pgr.web.controller;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.domain.service.ServiceTypeService;
import org.egov.pgr.web.contract.RequestInfoBody;
import org.egov.pgr.web.contract.ServiceType;
import org.egov.pgr.web.contract.ServiceTypeRequest;
import org.egov.pgr.web.contract.ServiceTypeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
public class ServiceTypeController {

    private ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @PostMapping("/v2/_create")
    public ServiceTypeResponse create(@RequestBody ServiceTypeRequest serviceTypeRequest){
        serviceTypeService.create(serviceTypeRequest.toDomain(), serviceTypeRequest);
        return new ServiceTypeResponse(null, serviceTypeRequest.getServiceType());
    }

    @PostMapping("/v2/_update")
    public ServiceTypeResponse update(@RequestBody ServiceTypeRequest serviceTypeRequest){
        serviceTypeService.update(serviceTypeRequest.toDomain(), serviceTypeRequest);
        return new ServiceTypeResponse(null, serviceTypeRequest.getServiceType());
    }

    @PostMapping("/v2/_search")
    public List<ServiceType> search(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                    @RequestParam(value = "serviceCode", required = false) String serviceCode,
                                    @RequestParam(value = "categoryId", required = false) Integer category,
                                    @RequestParam(value = "keywords", required = false) List<String> keywords,
                                    @RequestBody RequestInfoBody requestInfoBody){

        ServiceTypeSearchCriteria serviceTypeSearchCriteria = ServiceTypeSearchCriteria.builder()
                .tenantId(tenantId)
                .serviceCode(serviceCode)
                .category(category)
                .keywords(keywords)
                .build();

        List<org.egov.pgr.domain.model.ServiceType> serviceTypeList = serviceTypeService.search(serviceTypeSearchCriteria);

        return serviceTypeList.stream()
                .map(serviceType -> new ServiceType(serviceType))
                .collect(Collectors.toList());
    }
}