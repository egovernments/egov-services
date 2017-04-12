package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.domain.model.TenantSearchCriteria;

import java.util.List;

@Getter
@AllArgsConstructor
public class TenantSearchRequest {

    private RequestInfo requestInfo;
    private List<String> tenantCodes;

    public TenantSearchCriteria toDomain() {
        return TenantSearchCriteria.builder().tenantCodes(tenantCodes).build();
    }

}
