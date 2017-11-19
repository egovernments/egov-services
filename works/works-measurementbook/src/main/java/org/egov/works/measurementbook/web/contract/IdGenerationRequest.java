package org.egov.works.measurementbook.web.contract;

import java.util.List;

import org.egov.works.commons.web.contract.IdRequest;

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
