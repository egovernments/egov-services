package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.works.estimate.web.model.RequestInfo;

import java.util.List;

@Getter
@Setter
public class IdGenerationRequest {

    @JsonProperty("RequestInfo")
    public RequestInfo requestInfo;

    private List<IdRequest> idRequests;
}
