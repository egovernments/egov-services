package org.egov.tenant.web.controller;


import org.egov.common.contract.response.ResponseInfo;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.domain.service.TenantService;
import org.egov.tenant.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping(value="_search")
    public TenantResponse search(@RequestParam("code") List<String> code,
                                 @RequestBody TenantSearchRequest tenantSearchRequest) {
        TenantSearchCriteria tenantSearchCriteria = new TenantSearchCriteria(code);
        List<Tenant> tenants =  tenantService.find(tenantSearchCriteria).stream()
                .map(Tenant::new)
                .collect(Collectors.toList());

        return new TenantResponse(new ResponseInfo(), tenants);
    }

    @PostMapping(value="_create")
    public SingleTenantResponse createTenant(@RequestBody CreateTenantRequest createTenantRequest) {
        org.egov.tenant.domain.model.Tenant tenant = tenantService.createTenant(createTenantRequest.getTenant().toDomain());
        return new SingleTenantResponse(new ResponseInfo(), new Tenant(tenant));
    }
}
