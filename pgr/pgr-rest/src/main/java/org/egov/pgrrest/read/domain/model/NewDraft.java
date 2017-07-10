package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.HashMap;

@Getter
@Builder
@EqualsAndHashCode
public class NewDraft {
    private Long userId;
    private String tenantId;
    private String serviceCode;
    private HashMap<String, Object> draft;
}

