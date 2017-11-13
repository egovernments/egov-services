package org.egov.swm.web.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceDetailsRequest {

    @Valid
    @JsonProperty(value="RequestInfo")
    private RequestInfo requestInfo;

    @Valid
    private List<VehicleMaintenanceDetails>  vehicleMaintenanceDetails = new ArrayList<>();
}
