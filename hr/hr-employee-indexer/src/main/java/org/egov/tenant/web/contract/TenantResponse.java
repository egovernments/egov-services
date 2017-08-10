package org.egov.tenant.web.contract;

import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TenantResponse {
    private ResponseInfo responseInfo;
    private List<Tenant> tenant;
}
