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
public class Vehicle {

	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("vehicleType")
	private VehicleType vehicleType = null;

	@NotNull
	@Length(min = 6, max = 22)
	@JsonProperty("regNumber")
	private String regNumber = null;

	@Length(min = 1, max = 256)
	@JsonProperty("engineSrNumber")
	private String engineSrNumber = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("chassisSrNumber")
	private String chassisSrNumber = null;

	@NotNull
	@JsonProperty("vehicleCapacity")
	private Double vehicleCapacity = null;

	@NotNull
	@JsonProperty("numberOfPersonsReq")
	private Long numberOfPersonsReq = null;

	@NotNull
	@Length(min = 0, max = 256)
	@JsonProperty("model")
	private String model = null;

	@NotNull
	@JsonProperty("fuelType")
	private FuelType fuelType = null;

	@JsonProperty("ulbOwnedVehicle")
	private Boolean ulbOwnedVehicle = null;

	@JsonProperty("vendor")
	private Vendor vendor = null;

	@Length(min = 0, max = 256)
	@JsonProperty("vehicleDriverName")
	private String vehicleDriverName = null;

	@Valid
	@JsonProperty("purchaseDate")
	private Long purchaseDate = null;

	@JsonProperty("yearOfPurchase")
	private String yearOfPurchase = null;

	@JsonProperty("price")
	private Double price = null;

	@Length(min = 0, max = 256)
	@JsonProperty("sourceOfPurchase")
	private String sourceOfPurchase = null;

	@Length(min = 0, max = 300)
	@JsonProperty("remarks")
	private String remarks = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("insuranceNumber")
	private String insuranceNumber = null;

	@NotNull
	@Valid
	@JsonProperty("insuranceValidityDate")
	private Long insuranceValidityDate = null;

	@JsonProperty("insuranceDocuments")
	private Documents insuranceDocuments = null;

	@JsonProperty("isUnderWarranty")
	private Boolean isUnderWarranty = null;

	@JsonProperty("kilometers")
	private Long kilometers = null;

	@JsonProperty("endOfWarranty")
	private Long endOfWarranty = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
