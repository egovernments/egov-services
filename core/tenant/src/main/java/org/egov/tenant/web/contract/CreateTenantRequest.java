package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Builder
@AllArgsConstructor
public class CreateTenantRequest {

    private RequestInfo requestInfo;
    private Tenant tenantContract;

    public org.egov.tenant.domain.model.Tenant toDomainForCreateTenantRequest() {
        return tenantContract.toDomain();
    }


}
