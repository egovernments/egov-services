package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CrossHierarchyServiceResponse {

    @JsonProperty("CrossHierarchy")
    private List<CrossHierarchyResponse> crossHierarchys;

}
