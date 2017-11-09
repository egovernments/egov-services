package org.egov.swm.web.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data
class VehicleMaintenanceDetailsResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    private List<VehicleMaintenanceDetails> vehicleMaintenanceDetails;

    private Pagination page;
}
