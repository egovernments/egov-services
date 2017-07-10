package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class DraftSearchCriteria {
    private long userId;
    private String serviceCode;
    private String tenantId;
}
