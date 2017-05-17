package org.egov.workflow.domain.model;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class KeywordStatusMapping {
    private String keyword;
    private String tenantId;
    private String code;
    private Long id;
    private String name;
}
