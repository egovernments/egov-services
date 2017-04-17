package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@AllArgsConstructor
public class CreateTenantRequest {

    private RequestInfo requestInfo;
    private Tenant tenant;
}
