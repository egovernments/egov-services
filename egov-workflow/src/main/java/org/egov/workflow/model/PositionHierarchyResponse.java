package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionHierarchyResponse {

    private Long id;
    private PositionResponse fromPosition;
    private PositionResponse toPosition;

}
