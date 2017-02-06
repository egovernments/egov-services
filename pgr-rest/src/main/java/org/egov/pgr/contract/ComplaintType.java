package org.egov.pgr.contract;

import lombok.Getter;

@Getter
public class ComplaintType {
    private Long id;
    private String name;
    private String code;
    private Long department;
    private String description;
    private Integer slaHours;
    private boolean active;
    private boolean financialImpact;
    private ComplaintTypeCategory category;
    private boolean metadata;
    private String type;
    private String keywords;
    private String attributes;

    public ComplaintType(org.egov.pgr.entity.ComplaintType domainComplaintType) {
        this.id = domainComplaintType.getId();
        this.name = domainComplaintType.getName();
        this.code = domainComplaintType.getCode();
        this.department = domainComplaintType.getDepartment();
        this.description = domainComplaintType.getDescription();
        this.slaHours = domainComplaintType.getSlaHours();
        this.active = domainComplaintType.getIsActive();
        this.financialImpact = domainComplaintType.isHasFinancialImpact();
        this.category = new ComplaintTypeCategory(domainComplaintType.getCategory());
        this.metadata = domainComplaintType.isMetadata();
        this.type = domainComplaintType.getType();
        this.keywords = domainComplaintType.getKeywords();
        this.attributes = domainComplaintType.getAttributes();
    }
}
