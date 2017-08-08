package org.egov.pgr.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceTypeSearchCriteria {

    private String tenantId;

    private String serviceCode;

    public boolean isTenantIdEmpty(){
        return tenantId == "" || null == tenantId;
    }

    public boolean isServiceCodeEmpty(){
        return serviceCode == "" || null == serviceCode;
    }
}
