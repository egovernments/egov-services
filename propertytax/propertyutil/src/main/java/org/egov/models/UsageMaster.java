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
 * UsageMaster
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("data")
public class UsageMaster {
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

	@JsonProperty("parent")
	@Size(min = 2, max = 64)
	private String parent = null;

	@JsonProperty("description")
	@Size(min = 4, max = 512)
	private String description = null;

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("isResidential")
	private Boolean isResidential = null;

	@JsonProperty("orderNumber")
	private Integer orderNumber = null;

	@JsonIgnore
	@JsonProperty("data")
	private String data;

	@JsonProperty("service")
	@Size(min = 2, max = 64)
	private String service = null;

	private AuditDetails auditDetails;
}
