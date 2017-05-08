package org.egov.pgrrest.read.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;

@Setter
@Getter
@Builder
public class GetUserByIdRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private List<Long> id;

    private String tenantId;
}
