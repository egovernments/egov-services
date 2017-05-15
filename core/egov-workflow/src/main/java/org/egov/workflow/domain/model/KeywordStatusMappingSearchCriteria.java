package org.egov.workflow.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class KeywordStatusMappingSearchCriteria {

    private String keyword;
    private String tenantId;
}