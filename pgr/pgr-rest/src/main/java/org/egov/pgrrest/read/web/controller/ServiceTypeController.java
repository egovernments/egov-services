package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceRequestTypeService;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.egov.pgrrest.read.web.contract.ServiceType;
import org.egov.pgrrest.read.web.contract.ServiceTypeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceTypeController {

    private ServiceRequestTypeService serviceRequestTypeService;

    public ServiceTypeController(ServiceRequestTypeService serviceRequestTypeService) {
        this.serviceRequestTypeService = serviceRequestTypeService;
    }

    @PostMapping(value = "/v1/_search")
    public ServiceTypeResponse getServiceTypes(@RequestParam String type,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(required = false) Integer count,
                                               @RequestParam(value = "tenantId", defaultValue = "default")
                                                   final String tenantId,
                                               @RequestBody RequestInfoBody requestInfo) {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
            .categoryId(categoryId)
            .count(count)
            .tenantId(tenantId)
            .complaintTypeSearch(type)
            .build();
        List<ServiceType> serviceTypes = serviceRequestTypeService.findByCriteria(searchCriteria)
            .stream()
            .map(ServiceType::new)
            .collect(Collectors.toList());
        return new ServiceTypeResponse(null, serviceTypes);
    }

    @PostMapping("/v1/{serviceCode}/_search")
    public ServiceTypeResponse getServiceTypes(@PathVariable(name = "serviceCode") String complaintTypeCode,
                                               @RequestParam(value = "tenantId", defaultValue = "default")
                                                   String tenantId,
                                               @RequestBody RequestInfoBody requestInfo) {
        org.egov.pgrrest.common.persistence.entity.ServiceType complaintType = serviceRequestTypeService
            .getComplaintType(complaintTypeCode, tenantId);
        return new ServiceTypeResponse(null, getComplaintType(complaintType));
    }

    private List<ServiceType> getComplaintType(org.egov.pgrrest.common.persistence.entity.ServiceType complaintType) {
        if (complaintType == null)
            return Collections.emptyList();
        else
            return Collections.singletonList(new ServiceType(complaintType));
    }
}
