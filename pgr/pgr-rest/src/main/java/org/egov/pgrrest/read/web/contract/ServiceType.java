package org.egov.pgrrest.read.web.contract;

import lombok.Getter;

import java.util.List;

@Getter
public class ServiceType {
    private Long id;
    private String serviceName;
    private String serviceCode;
    private String description;
    private boolean metadata;
    private String type;
    private List<String> keywords;
    private Long groupId;
    private String groups;
    private Integer slaHours;
    private String tenantId;
    private String localName;

    public ServiceType(org.egov.pgrrest.common.persistence.entity.ServiceType entityServiceType) {
        this.id = entityServiceType.getId();
        this.serviceName = entityServiceType.getName();
        this.serviceCode = entityServiceType.getCode();
        this.description = entityServiceType.getDescription();
        this.metadata = entityServiceType.isMetadata();
        this.type = entityServiceType.getType();
        this.keywords = entityServiceType.getKeywords();
        this.groups = entityServiceType.getCategory().getName();
        this.groupId = entityServiceType.getCategory().getId();
        this.slaHours = entityServiceType.getSlaHours();
        this.tenantId = entityServiceType.getTenantId();
        this.localName = entityServiceType.getLocalName();
    }
}
