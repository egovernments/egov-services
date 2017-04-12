package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tenant.domain.model.Tenant;

import java.util.List;

/**
 * Created by parvati on 10/4/17.
 */
@Getter
@AllArgsConstructor
public class TenantResponse {

    private ResponseInfo responseInfo;

    private List<Tenant> tenant;
}
