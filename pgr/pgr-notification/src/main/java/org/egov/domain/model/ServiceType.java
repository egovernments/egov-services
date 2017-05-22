package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceType {
    private static final String COMPLAINT_KEYWORD = "COMPLAINT";
    private String name;
    private List<String> keywords;

    public boolean isComplaintType() {
        return keywords != null && keywords.stream().anyMatch(COMPLAINT_KEYWORD::equalsIgnoreCase);
    }
}
