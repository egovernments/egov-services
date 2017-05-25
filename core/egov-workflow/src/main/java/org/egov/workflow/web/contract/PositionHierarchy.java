package org.egov.workflow.web.contract;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionHierarchy {

    private Long id;

    private PositionResponse fromPosition;

    private PositionResponse toPosition;

    private ObjectType objectType;

    private String objectSubType;

    private String tenantId;

}
