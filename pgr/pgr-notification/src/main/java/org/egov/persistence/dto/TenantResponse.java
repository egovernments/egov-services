package org.egov.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.exception.TenantDoesNotExistException;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@AllArgsConstructor
public class TenantResponse {
    private List<Tenant> tenant;

    public org.egov.domain.model.Tenant toDomainTenant() {
        if (CollectionUtils.isEmpty(tenant)) {
            throw new TenantDoesNotExistException();
        }
        return new org.egov.domain.model.Tenant(tenant.get(0).getCityName(), tenant.get(0).getUlbGrade());
    }
}