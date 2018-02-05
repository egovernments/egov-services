package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.egov.swm.web.contract.Employee;
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
public class Vehicle {

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 22, message = "Value of regNumber shall be between 1 and 22")
    @JsonProperty("regNumber")
    private String regNumber = null;

    @NotNull
    @JsonProperty("vehicleType")
    private VehicleType vehicleType = null;

    @NotNull
    @Valid
    @JsonProperty("purchaseInfo")
    private PurchaseInfo purchaseInfo = null;

    @NotNull
    @JsonProperty("fuelType")
    private FuelType fuelType = null;

    @NotNull
    @Max(value = 100, message = "Operators Required shall be between 0 and 100")
    @JsonProperty("operatorsReq")
    private Long operatorsReq = null;

    @NotNull
    @JsonProperty("driver")
    private Employee driver = null;

    @JsonProperty("vendor")
    private Vendor vendor = null;

    @NotNull
    @Valid
    @JsonProperty("manufacturingDetails")
    private ManufacturingDetails manufacturingDetails = null;

    @Valid
    @JsonProperty("insuranceDetails")
    private InsuranceDetails insuranceDetails = null;

    @Max(value = 100000, message = "kilometers shall be between 1 and 100000")
    @JsonProperty("kilometers")
    private Long kilometers = null;

    @JsonProperty("endOfWarranty")
    private Long endOfWarranty = null;

    @Length(min = 15, max = 300, message = "Value of remarks shall be between 15 and 300")
    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("isVehicleUnderWarranty")
    private Boolean isVehicleUnderWarranty;

    @JsonProperty("isUlbOwned")
    private Boolean isUlbOwned;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
