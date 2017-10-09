package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WoodType
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("data")
public class WoodType {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("name")
	@NotNull
	@Size(min = 4, max = 128)
	private String name = null;

	@JsonProperty("code")
	@NotNull
	@Size(min = 2, max = 64)
	private String code = null;

	@JsonProperty("nameLocal")
	@Size(min = 1, max = 256)
	private String nameLocal = null;

	@JsonProperty("description")
	@Size(min = 4, max = 512)
	private String description = null;

	@JsonIgnore
	@JsonProperty("data")
	private String data;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
