package org.egov.tenant.web.contract;

import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantResponse {
    private ResponseInfo responseInfo;
    private List<Tenant> tenant;
}
