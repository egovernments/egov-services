package org.egov.lcms.notification.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
	@NotEmpty
	@NotNull
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("description")
	private String description = null;

	@NotNull
	@JsonProperty("fromDate")
	private Long fromDate = null;

	@NotNull
	@JsonProperty("toDate")
	private Long toDate = null;

	@NotNull
	@JsonProperty("fromValue")
	private Double fromValue = null;

	@NotNull
	@JsonProperty("toValue")
	private Double toValue = null;

	@JsonProperty("chargePercentage")
	private Double chargePercentage = null;

	@JsonProperty("chargeAmount")
	private Double chargeAmount = null;

	@JsonProperty("isFlat")
	private Boolean isFlat = false;

	@JsonProperty("court")
	private Court court = null;

	@JsonProperty("caseType")
	private CaseType caseType = null;

	@NotNull
	@JsonProperty("active")
	private Boolean active = true;

	@NotEmpty
	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;
}
