package org.egov.tenant.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Builder
@AllArgsConstructor
public class CreateTenantRequest {

    private RequestInfo requestInfo;
    private Tenant tenant;

    @JsonIgnore
    public org.egov.tenant.domain.model.Tenant toDomainForCreateTenantRequest() {
        return tenant.toDomain();
    }


}
