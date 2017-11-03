package org.egov.works.services.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdGenerationRequest {

    @JsonProperty("RequestInfo")
    public RequestInfo requestInfo;

    private List<IdRequest> idRequests;
}
