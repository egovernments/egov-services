package org.egov.swm.web.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
public @Data class ScheduledMaintenanceDateResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    private Long sceduledDate;
}
