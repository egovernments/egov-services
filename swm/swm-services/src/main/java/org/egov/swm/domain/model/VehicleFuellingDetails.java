package org.egov.swm.domain.model;

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

	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

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
	@JsonProperty("vehicleType")
	private VehicleType vehicleType = null;

	@NotNull
	@JsonProperty("vehicleRegNo")
	private Vehicle vehicleRegNo = null;

	@NotNull
	@JsonProperty("vehicleReadingDuringFuelling")
	private Long vehicleReadingDuringFuelling = null;

	@NotNull
	@JsonProperty("refuellingStation")
	private RefillingPumpStation refuellingStation = null;

	@NotNull
	@Length(min = 1, max = 256)
	@JsonProperty("fuelFilled")
	private String fuelFilled = null;

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

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
