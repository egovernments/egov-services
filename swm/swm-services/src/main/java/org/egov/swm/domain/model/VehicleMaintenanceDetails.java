package org.egov.swm.domain.model;

import javax.validation.constraints.*;

import org.egov.swm.domain.enums.MaintenanceType;

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
public class VehicleMaintenanceDetails {
    @Size(min = 1, max = 256, message = "Value of code shall be between 1 and 256")
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Size(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("isScheduled")
    private Boolean isScheduled = null;

    @NotNull
    @JsonProperty("maintenanceType")
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
    @DecimalMin(value = "0", message = "Vehicle reading shall be between 0 and 300000")
    @DecimalMax(value = "300000", message = "Vehicle reading shall be between 0 and 300000")
    private Double vehicleReadingDuringMaintenance = null;

    @JsonProperty("remarks")
    @Size(min = 0, max = 300, message = "Value of remarks shall be between 0 and 300")
    private String remarks = null;

    @NotNull
    @JsonProperty("costIncurred")
    @DecimalMin(value = "1", message = "cost incurred shall be between 1 and 1000000")
    @DecimalMax(value = "1000000", message = "cost incurred shall be between 1 and 1000000")
    private Double costIncurred = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
