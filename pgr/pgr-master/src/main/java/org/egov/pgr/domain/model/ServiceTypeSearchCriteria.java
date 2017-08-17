package org.egov.pgr.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ServiceTypeSearchCriteria {

    private String tenantId;

    private String serviceCode;

    private Integer category;

    private List<String> keywords;

    public boolean isTenantIdEmpty(){
        return tenantId == "" || null == tenantId;
    }

    public boolean isServiceCodeEmpty(){
        return serviceCode == "" || null == serviceCode;
    }

    public boolean isKeywordsEmpty(){
        return null == keywords;
    }

    public boolean isCategoryEmpty(){
        return null == category;
    }
}
