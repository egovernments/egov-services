package org.egov.pgr.web.contract;

import java.util.List;

import org.egov.pgr.persistence.queue.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GetUserByIdRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("id")
    private List<Long> id;
}
