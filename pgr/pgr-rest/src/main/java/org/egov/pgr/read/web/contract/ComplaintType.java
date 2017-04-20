package org.egov.pgr.read.web.contract;

import lombok.Getter;

@Getter
public class ComplaintType {
    private Long id;
    private String serviceName;
    private String serviceCode;
    private String description;
    private boolean metadata;
    private String type;
    private String keywords;
    private Long groupId;
    private String groups;
    private Integer slaHours;
    private String tenantId;

    public ComplaintType(org.egov.pgr.common.entity.ComplaintType domainComplaintType) {
        this.id = domainComplaintType.getId();
        this.serviceName = domainComplaintType.getName();
        this.serviceCode = domainComplaintType.getCode();
        this.description = domainComplaintType.getDescription();
        this.metadata = domainComplaintType.isMetadata();
        this.type = domainComplaintType.getType();
        this.keywords = domainComplaintType.getKeywords();
        this.groups = domainComplaintType.getCategory().getName();
        this.groupId = domainComplaintType.getCategory().getId();
        this.slaHours = domainComplaintType.getSlaHours();
        this.tenantId = domainComplaintType.getTenantId();
    }
}
