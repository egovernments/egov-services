package org.egov.tenant.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TenantSearchCriteria {
    private List<String> tenantCodes;
}
