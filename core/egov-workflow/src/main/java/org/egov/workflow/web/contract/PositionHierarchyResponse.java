package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String tenantId;
}
