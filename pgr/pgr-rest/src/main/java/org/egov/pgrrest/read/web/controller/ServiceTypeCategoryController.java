package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.service.ServiceTypeCategoryService;
import org.egov.pgrrest.read.web.contract.ServiceTypeCategory;
import org.egov.pgrrest.read.web.contract.ServiceTypeCategoryResponse;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ServiceTypeCategoryController {

    private ServiceTypeCategoryService serviceTypeCategoryService;

    public ServiceTypeCategoryController(ServiceTypeCategoryService serviceTypeCategoryService) {
        this.serviceTypeCategoryService = serviceTypeCategoryService;
    }

    @PostMapping(value ="/servicecategories/v1/_search")
    public ServiceTypeCategoryResponse getAllServiceTypeCategories(
        @RequestParam(value = "tenantId", defaultValue = "default") final String tenantId,
        @RequestBody RequestInfoBody requestInfo) {
        List<ServiceTypeCategory> serviceTypeCategories = serviceTypeCategoryService
            .getAll(tenantId).stream().map(ServiceTypeCategory::new)
            .collect(Collectors.toList());
        return new ServiceTypeCategoryResponse(null, serviceTypeCategories);
    }

}
