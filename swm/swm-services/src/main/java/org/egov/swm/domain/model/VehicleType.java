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
public class VehicleType {

	@NotNull
	@Size(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("name")
	private String name = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
