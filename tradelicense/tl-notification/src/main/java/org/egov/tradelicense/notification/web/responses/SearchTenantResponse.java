package org.egov.tradelicense.notification.web.responses;

import java.util.List;

import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tradelicense.notification.web.contract.Tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchTenantResponse {

    private ResponseInfo responseInfo;
    private List<Tenant> tenant;
}
