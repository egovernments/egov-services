package org.egov.pgrrest.read.web.contract;

import lombok.Getter;

@Getter
public class ServiceTypeCategory {
    private Long id;
    private String name;
    private String description;
    private String tenantId;
    private String localName;

    public ServiceTypeCategory(org.egov.pgrrest.common.persistence.entity.ServiceTypeCategory entityServiceTypeCategory) {
        this.id = entityServiceTypeCategory.getId();
        this.name = entityServiceTypeCategory.getName();
        this.description = entityServiceTypeCategory.getDescription();
        this.tenantId = entityServiceTypeCategory.getTenantId();
        this.localName = entityServiceTypeCategory.getLocalName();
    }
}
