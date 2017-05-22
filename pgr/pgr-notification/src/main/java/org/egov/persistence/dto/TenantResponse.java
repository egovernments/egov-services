package org.egov.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@AllArgsConstructor
public class TenantResponse {
    private static final String TENANT_NAME_MISSING_MESSAGE = "TENANT NAME MISSING";
    private List<Tenant> tenant;

    public org.egov.domain.model.Tenant toDomainTenant() {
        if (CollectionUtils.isEmpty(tenant)) {
            return new org.egov.domain.model.Tenant(TENANT_NAME_MISSING_MESSAGE);
        }
        return new org.egov.domain.model.Tenant(tenant.get(0).getCityName());
    }
}