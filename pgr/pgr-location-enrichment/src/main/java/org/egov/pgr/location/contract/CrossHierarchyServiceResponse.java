package org.egov.pgr.location.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CrossHierarchyServiceResponse {
    @JsonProperty("CrossHierarchy")
    private List<CrossHierarchyResponse> crossHierarchys;

    public CrossHierarchyResponse getCrossHierarchy() {
        return crossHierarchys.get(0);
    }

}
