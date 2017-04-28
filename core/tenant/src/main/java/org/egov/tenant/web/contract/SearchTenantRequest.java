package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@AllArgsConstructor
public class SearchTenantRequest {
    private RequestInfo requestInfo;
}
