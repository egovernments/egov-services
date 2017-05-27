package org.egov.web.indexer.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceType {
    public static final String COMPLAINT_KEYWORD = "COMPLAINT";
    public static final String DELIVERABLE_KEYWORD = "Deliverable_service";
    private List<String> keywords;
    private Integer slaHours;

    public boolean isComplaintType() {
        return keywords.stream().anyMatch(COMPLAINT_KEYWORD::equalsIgnoreCase);
    }

    public boolean isDeliverableServiceType() {
        return keywords.stream().anyMatch(COMPLAINT_KEYWORD::equalsIgnoreCase);
    }
}