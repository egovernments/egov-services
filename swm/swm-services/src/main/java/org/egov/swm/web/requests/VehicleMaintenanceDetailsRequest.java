package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceDetailsRequest {

    @Valid
    @JsonProperty(value = "RequestInfo")
    private RequestInfo requestInfo;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<VehicleMaintenanceDetails> vehicleMaintenanceDetails = new ArrayList<>();
}
