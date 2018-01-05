package org.egov.swm.web.requests;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data class ScheduledMaintenanceDateResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    private Long scheduledDate;
}
