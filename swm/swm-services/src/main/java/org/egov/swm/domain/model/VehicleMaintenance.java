package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class VehicleMaintenance {

    @Size(min = 1, max = 256)
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @Size(min = 2, max = 5)
    @JsonProperty("maintenanceUom")
    private String maintenanceUom = null;

    @NotNull
    @Min(1)
    @Max(50000)
    @JsonProperty("maintenanceAfter")
    private Long maintenanceAfter = null;

    @NotNull
    @Size(min = 3, max = 5)
    @JsonProperty("downtimeforMaintenanceUom")
    private String downtimeforMaintenanceUom = null;

    @NotNull
    @Min(1)
    @Max(720)
    @JsonProperty("downtimeforMaintenance")
    private Long downtimeforMaintenance = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
}
