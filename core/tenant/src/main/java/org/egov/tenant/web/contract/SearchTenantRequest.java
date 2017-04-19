package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchTenantRequest {

    private RequestInfo requestInfo;
    private List<String> tenantCodes;
}
