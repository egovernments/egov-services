package org.egov.pgr.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceType {
    private static final String COMPLAINT_KEYWORD = "COMPLAINT";
    private static final String DELIVERABLE_KEYWORD = "DELIVERABLE";
    private String name;
    private List<String> keywords;

    public boolean isComplaintType() {
        return isKeywordPresent(COMPLAINT_KEYWORD);
    }

    public boolean isDeliverableType() {
        return isKeywordPresent(DELIVERABLE_KEYWORD);
    }

    private boolean isKeywordPresent(String keyword) {
        return keywords != null && keywords.stream().anyMatch(keyword::equalsIgnoreCase);
    }
}
