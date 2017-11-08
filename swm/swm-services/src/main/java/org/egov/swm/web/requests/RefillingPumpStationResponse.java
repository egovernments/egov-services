package org.egov.swm.web.requests;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data class RefillingPumpStationResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    private List<RefillingPumpStation> refillingPumpStations;

    private Pagination page;
}
