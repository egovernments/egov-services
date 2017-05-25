package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionHierarchyResponse {


    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("PositionHierarchy")
    private List<PositionHierarchy> positionHierarchy = new ArrayList<PositionHierarchy>();

}
