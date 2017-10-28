package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class CollectionPointDetails {

	@Length(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Length(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@JsonProperty("collectionType")
	private CollectionType collectionType = null;

	@NotNull
	@JsonProperty("collectionPoint")
	private CollectionPoint collectionPoint = null;

	@NotNull
	@JsonProperty("garbageEstimate")
	private Double garbageEstimate = null;

	@Length(min = 0, max = 300)
	@JsonProperty("description")
	private String description = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
