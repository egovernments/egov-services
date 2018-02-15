package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.hibernate.validator.constraints.NotEmpty;

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
    @NotEmpty
    private List<VehicleMaintenanceDetails> vehicleMaintenanceDetails = new ArrayList<>();
}
