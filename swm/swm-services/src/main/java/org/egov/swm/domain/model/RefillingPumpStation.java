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

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Valid
	@JsonProperty("ward")
	private Boundary ward = null;

	@NotNull
	@Valid
	@JsonProperty("zone")
	private Boundary zone = null;

	@NotNull
	@Valid
	@JsonProperty("street")
	private Boundary street = null;

	@Valid
	@JsonProperty("colony")
	private Boundary colony = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("name")
	private String name = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("type")
	private String type = null;

	@Size(min = 0, max = 300)
	@JsonProperty("remarks")
	private String remarks = null;

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("typeOfFuel")
	private String typeOfFuel = null;

	@NotNull
	@JsonProperty("quantity")
	private Long quantity = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
