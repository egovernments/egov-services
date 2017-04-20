package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponse {

    @JsonProperty("id")
    private Long id;

    private String name;
    
    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

}
