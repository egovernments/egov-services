package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PumpStationFuelTypes {

    @Size(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("pumpStation")
    private String pumpStation = null;

    @NotNull
    @Size(min = 1, max = 256, message = "Value of fuelType shall be between 1 and 256")
    @JsonProperty("fuelType")
    private String fuelType = null;

    @JsonProperty("pumpStations")
    private String pumpStations = null;

}
