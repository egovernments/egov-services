package org.egov.pgr.location.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.location.model.CrossHierarchy;

@Getter
@Setter
@Builder
public class CrossHierarchySearchRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("CrossHierarchy")
    private CrossHierarchy crossHierarchy;

}
