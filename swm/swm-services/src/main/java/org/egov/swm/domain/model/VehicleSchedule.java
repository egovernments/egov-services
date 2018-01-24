package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
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
public class VehicleSchedule {

    @Size(min = 1, max = 256, message = "Value of transactionNo shall be between 1 and 256")
    @JsonProperty("transactionNo")
    private String transactionNo = null;

    @NotNull
    @Size(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("scheduledFrom")
    private Long scheduledFrom = null;

    @NotNull
    @JsonProperty("scheduledTo")
    private Long scheduledTo = null;

    @NotNull
    @JsonProperty("route")
    private Route route = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @Digits(fraction = 2, integer = 3, message = "targeted Garbage shall be with 2 decimal points")
    @DecimalMax(value = "75", message = "targeted Garbage shall be between 0 and 75 Tons")
    @JsonProperty("targetedGarbage")
    private Double targetedGarbage = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}