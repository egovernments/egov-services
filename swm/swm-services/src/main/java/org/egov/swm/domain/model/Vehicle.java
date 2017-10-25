package org.egov.swm.domain.model;

import javax.validation.Valid;
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
public class Vehicle {

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("vehicleType")
	private VehicleType vehicleType = null;

	@NotNull
	@Size(min = 0, max = 256)
	@JsonProperty("regNumber")
	private String regNumber = null;

	@JsonProperty("engineSrNumber")
	private String engineSrNumber = null;

	@NotNull
	@Size(min = 0, max = 256)
	@JsonProperty("chassisSrNumber")
	private String chassisSrNumber = null;

	@NotNull
	@JsonProperty("vehicleCapacity")
	private Long vehicleCapacity = null;

	@NotNull
	@JsonProperty("numberOfPersonsReq")
	private Long numberOfPersonsReq = null;

	@NotNull
	@Size(min = 0, max = 256)
	@JsonProperty("model")
	private String model = null;

	@JsonProperty("ulbOwnedVehicle")
	private Boolean ulbOwnedVehicle = null;

	@Size(min = 0, max = 256)
	@JsonProperty("vendorName")
	private String vendorName = null;

	@Size(min = 0, max = 256)
	@JsonProperty("vehicleDriverName")
	private String vehicleDriverName = null;

	@Valid
	@JsonProperty("purchaseDate")
	private Long purchaseDate = null;

	@Size(min = 0, max = 256)
	@JsonProperty("yearOfPurchase")
	private String yearOfPurchase = null;

	@JsonProperty("price")
	private Double price = null;

	@Size(min = 0, max = 256)
	@JsonProperty("sourceOfPurchase")
	private String sourceOfPurchase = null;

	@Size(min = 0, max = 300)
	@JsonProperty("remarks")
	private String remarks = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("insuranceNumber")
	private String insuranceNumber = null;

	@NotNull
	@Valid
	@JsonProperty("insuranceValidityDate")
	private Long insuranceValidityDate = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
