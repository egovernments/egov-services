package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tenant.domain.model.Tenant;

@Getter
@Builder
@AllArgsConstructor
public class CreateTenantRequest {

    private RequestInfo requestInfo;
    private TenantContract tenantContract;

    public Tenant toDomainForCreateTenantRequest() {
        return tenantContract.toDomain();
    }


}
