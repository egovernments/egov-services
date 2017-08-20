package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionHierarchyResponse {


    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    private List<EscalationHierarchy> escalationHierarchies;

}
