package org.egov.web.indexer.contract;

import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@Builder
public class TenantRequest {
    private RequestInfo requestInfo;
    private List<String> tenantCodes;
}
