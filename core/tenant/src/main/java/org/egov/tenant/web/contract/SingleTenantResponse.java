package org.egov.tenant.web.contract;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@AllArgsConstructor
public class SingleTenantResponse {
    private ResponseInfo responseInfo;
    private Tenant tenant;
}
