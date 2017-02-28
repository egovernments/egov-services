package org.egov.workflow.web.contract;

import lombok.Getter;

@Getter
public class PositionHierarchyResponse {

    private Long id;
    private PositionResponse fromPosition;
    private PositionResponse toPosition;

}
