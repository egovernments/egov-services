package org.egov.tenant.web.controller;


import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.domain.service.TenantService;
import org.egov.tenant.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                .map(org.egov.tenant.web.contract.Tenant::new)
                .collect(Collectors.toList());

        return new TenantResponse(new ResponseInfo(), tenants);
    }

    @PostMapping(value="_create")
    public SingleTenantResponse createTenant(@RequestBody CreateTenantRequest createTenantRequest) {
        org.egov.tenant.domain.model.Tenant tenant = tenantService.createTenant(createTenantRequest.toDomainForCreateTenantRequest());
        return new SingleTenantResponse(new ResponseInfo(), new Tenant(tenant));
    }

    private TenantResponse getSuccessResponse(final List<org.egov.tenant.domain.model.Tenant> tenant, final RequestInfo requestInfo) {
        final ResponseInfo responseInfo = ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).msgId(requestInfo.getMsgId()).status(HttpStatus.OK.toString()).build();
        List<org.egov.tenant.web.contract.Tenant> tenants = tenant
                .stream()
                .map(org.egov.tenant.web.contract.Tenant::new)
                .collect(Collectors.toList());
        return new TenantResponse(responseInfo, tenants);
    }
}
