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
public class RefillingPumpStation {

	@Size(min = 1, max = 256)
	@JsonProperty("code")
	private String code = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Valid
	@NotNull
	@JsonProperty("location")
	private Boundary location = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("typeOfPump")
	private OilCompanyName typeOfPump = null;

	@Size(min = 0, max = 300)
	@JsonProperty("remarks")
	private String remarks = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("typeOfFuel")
	private FuelType typeOfFuel = null;

	@NotNull
	@JsonProperty("quantity")
	private Long quantity = null;

}
