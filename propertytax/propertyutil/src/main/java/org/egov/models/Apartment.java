package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Yosadhara
 *
 */
@JsonIgnoreProperties({ "data" })
public class Apartment {

	@JsonProperty("id")
	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@NotNull
	@Size(min = 4, max = 64)
	@JsonProperty("code")
	private String code;

	@NotNull
	@Size(min = 4, max = 256)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@JsonProperty("totalBuiltUpArea")
	private Double totalBuiltUpArea;

	@NotNull
	@JsonProperty("totalProperties")
	private Long totalProperties;

	@NotNull
	@JsonProperty("totalFloors")
	private Long totalFloors;

	@NotNull
	@JsonProperty("totalOpenSpace")
	private Double totalOpenSpace;

	@JsonProperty("liftFacility")
	private Boolean liftFacility;

	@JsonProperty("powerBackUp")
	private Boolean powerBackUp;

	@JsonProperty("parkingFacility")
	private Boolean parkingFacility;

	@NotNull
	@JsonProperty("residtinalProperties")
	private Long residtinalProperties;

	@JsonProperty("nonResidtinalProperties")
	private Long nonResidtinalProperties;

	@NotNull
	@Size(min = 4, max = 256)
	@JsonProperty("sourceOfWater")
	private String sourceOfWater;

	@JsonProperty("floorDetails")
	private Floor floor;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("data")
	private String data;
}