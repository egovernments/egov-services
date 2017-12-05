package org.egov.swm.domain.model;

import javax.validation.Valid;
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
    @Length(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 6, max = 22)
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

    @JsonProperty("insuranceDetails")
    private InsuranceDetails insuranceDetails = null;

    @JsonProperty("kilometers")
    private Long kilometers = null;

    @JsonProperty("endOfWarranty")
    private Long endOfWarranty = null;

    @Length(min = 0, max = 300)
    @JsonProperty("remarks")
    private String remarks = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
