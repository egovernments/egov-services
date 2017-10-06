package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuidanceValueBoundary {
	@JsonProperty("id")
	private Long id;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("guidanceValueBoundary1")
	@NotNull
	private String guidanceValueBoundary1;

	@JsonProperty("guidanceValueBoundary2")
	private String guidanceValueBoundary2;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
