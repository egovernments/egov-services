package org.egov.swm.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.swm.domain.enums.MaintenanceType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceDetails {
    @Size(min = 1, max = 256)
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("isScheduled")
    private Boolean isScheduled = null;

    @NotNull
    @JsonProperty("maintenanceType")
//    @Size(min = 1, max = 128)
    private MaintenanceType maintenanceType = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @JsonProperty("actualMaintenanceDate")
    private Long actualMaintenanceDate = null;

    @JsonProperty("vehicleScheduledMaintenanceDate")
    private Long vehicleScheduledMaintenanceDate = null;

    @NotNull
    @JsonProperty("vehicleDowntimeActual")
    private Double vehicleDowntimeActual = null;

    @NotNull
    @JsonProperty("vehicleDownTimeActualUom")
    private String vehicleDownTimeActualUom = null;

    @NotNull
    @JsonProperty("vehicleReadingDuringMaintenance")
    private Double vehicleReadingDuringMaintenance = null;

    @JsonProperty("remarks")
    @Size(min = 15, max = 300)
    private String remarks = null;

    @NotNull
    @JsonProperty("costIncurred")
    private Double costIncurred = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
