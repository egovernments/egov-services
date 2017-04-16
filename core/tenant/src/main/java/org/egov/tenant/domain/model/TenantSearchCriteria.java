package org.egov.tenant.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TenantSearchCriteria {
    private List<String> tenantCodes;
}
