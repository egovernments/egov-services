package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

@Data
public class PositionResponse {

    @JsonProperty("id")
    private Long id;

    private String name;

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

}
