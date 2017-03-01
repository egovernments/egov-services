package org.egov.workflow.web.contract;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PositionHierarchyResponse {

    private Long id;
    private PositionResponse fromPosition;
    private PositionResponse toPosition;

}
