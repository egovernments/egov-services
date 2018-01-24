package org.egov.swm.domain.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 256, message = "Value of transactionNo shall be between 0 and 256")
    @JsonProperty("transactionNo")
    private String transactionNo = null;

    @NotNull
    @JsonProperty("transactionDate")
    private Long transactionDate = null;

    @NotNull
    @JsonProperty("vehicle")
    private Vehicle vehicle = null;

    @NotNull
    @Min(value = 1, message = "Value of fuelFilled shall be between 1 and 100")
    @Max(value = 300000, message = "Value of fuelFilled shall be between 1 and 100")
    @JsonProperty("vehicleReadingDuringFuelling")
    private Double vehicleReadingDuringFuelling = null;

    @NotNull
    @JsonProperty("refuellingStation")
    private RefillingPumpStation refuellingStation = null;

    @NotNull
    @DecimalMin(value = "1", message = "fuelFilled shall be between 1 and 500 Ltrs")
    @DecimalMax(value = "500", message = "fuelFilled shall be between 1 and 500 Ltrs")
    @Digits(fraction = 2, integer = 3, message = "fuelFilled shall be with 2 decimal points")
    @JsonProperty("fuelFilled")
    private Double fuelFilled = null;

    @NotNull
    @JsonProperty("totalCostIncurred")
    private Double totalCostIncurred = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of receiptNo shall be between 1 and 256")
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
