package org.egov.pgr.web.contract;

import lombok.Getter;

@Getter
public class ComplaintType {
    private String serviceName;
    private String serviceCode;
    private String description;
    private boolean metadata;
    private String type;
    private String keywords;
    private Long groupId;
    private String groups;

    public ComplaintType(org.egov.pgr.persistence.entity.ComplaintType domainComplaintType) {
        this.serviceName = domainComplaintType.getName();
        this.serviceCode = domainComplaintType.getCode();
        this.description = domainComplaintType.getDescription();
        this.metadata = domainComplaintType.isMetadata();
        this.type = domainComplaintType.getType();
        this.keywords = domainComplaintType.getKeywords();
        this.groups = domainComplaintType.getCategory().getName();
        this.groupId = domainComplaintType.getCategory().getId();
    }
}
