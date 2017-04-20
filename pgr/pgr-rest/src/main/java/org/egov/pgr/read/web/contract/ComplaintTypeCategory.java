package org.egov.pgr.read.web.contract;

import lombok.Getter;

@Getter
public class ComplaintTypeCategory {
    private Long id;
    private String name;
    private String description;
    private String tenantId;

    public ComplaintTypeCategory(org.egov.pgr.common.entity.ComplaintTypeCategory entityComplaintTypeCategory) {
        this.id = entityComplaintTypeCategory.getId();
        this.name = entityComplaintTypeCategory.getName();
        this.description = entityComplaintTypeCategory.getDescription();
        this.tenantId = entityComplaintTypeCategory.getTenantId();
    }
}
