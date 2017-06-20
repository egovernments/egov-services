package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A logical group of rooms on a floor Author : Narendra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("unitNo")
	@NotNull
	private Integer unitNo = null;

	@JsonProperty("unitType")
	@Size(min = 1, max = 16)
	private UnitTypeEnum unitType = null;

	@JsonProperty("length")
	private Double length = null;

	@JsonProperty("width")
	private Double width = null;

	@JsonProperty("builtupArea")
	@NotNull
	private Double builtupArea = null;

	@JsonProperty("assessableArea")
	private Double assessableArea = null;

	@JsonProperty("bpaBuiltupArea")
	private Double bpaBuiltupArea = null;

	@JsonProperty("bpaNo")
	@Size(min = 1, max = 16)
	private String bpaNo = null;

	@JsonProperty("bpaDate")
	private String bpaDate = null;

	@JsonProperty("usage")
	@NotNull
	@Size(min = 1, max = 16)
	private String usage = null;

	@JsonProperty("occupancy")
	@NotNull
	@Size(min = 1, max = 16)
	private String occupancy = null;

	@JsonProperty("occupierName")
	@Size(min = 1, max = 128)
	private String occupierName = null;

	@JsonProperty("firmName")
	@Size(min = 1, max = 128)
	private String firmName = null;

	@JsonProperty("rentCollected")
	private Double rentCollected = null;

	@JsonProperty("structure")
	@NotNull
	@Size(min = 1, max = 16)
	private String structure = null;

	@JsonProperty("age")
	@Size(min = 1, max = 16)
	private String age = null;

	@JsonProperty("exemptionReason")
	@Size(min = 1, max = 32)
	private String exemptionReason = null;

	@JsonProperty("isStructured")
	private Boolean isStructured = true;

	@JsonProperty("occupancyDate")
	private String occupancyDate = null;

	@JsonProperty("constCompletionDate")
	private String constCompletionDate = null;

	@JsonProperty("manualArv")
	private Double manualArv = null;

	@JsonProperty("arv")
	private Double arv = null;

	@JsonProperty("electricMeterNo")
	@Size(min = 1, max = 64)
	private String electricMeterNo = null;

	@JsonProperty("waterMeterNo")
	@Size(min = 1, max = 64)
	private String waterMeterNo = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
