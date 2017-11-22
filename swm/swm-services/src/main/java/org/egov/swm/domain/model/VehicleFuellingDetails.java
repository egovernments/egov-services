package org.egov.swm.domain.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class VehicleFuellingDetails {

    @NotNull
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 1, max = 256)
    @JsonProperty("transactionNo")
    private String transactionNo = null;

    @NotNull
    @JsonProperty("transactionDate")
    private Long transactionDate = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @Min(value = 1)
    @Max(value = 300000)
    @JsonProperty("vehicleReadingDuringFuelling")
    private Long vehicleReadingDuringFuelling = null;

    @NotNull
    @JsonProperty("refuellingStation")
    private RefillingPumpStation refuellingStation = null;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @JsonProperty("fuelFilled")
    private Double fuelFilled = null;

    @NotNull
    @JsonProperty("typeOfFuel")
    private FuelType typeOfFuel = null;

    @NotNull
    @JsonProperty("totalCostIncurred")
    private Double totalCostIncurred = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("receiptNo")
    private String receiptNo = null;

    @NotNull
    @JsonProperty("receiptDate")
    private Long receiptDate = null;

    @JsonProperty("receiptCopy")
    private Document receiptCopy = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
