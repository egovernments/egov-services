package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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

    @Length(min = 1, max = 256, message = "Value of transactionNo shall be between 1 and 256")
    @JsonProperty("transactionNo")
    private String transactionNo = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
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
    @JsonProperty("targetedGarbage")
    private Double targetedGarbage = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}