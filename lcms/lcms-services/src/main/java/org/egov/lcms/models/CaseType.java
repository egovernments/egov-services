package org.egov.lcms.models;

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
public class CaseType {
	@NotEmpty
	@NotNull
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("sideCode")
	private String sideCode = null;

	@JsonProperty("active")
	private Boolean active = true;

	@NotEmpty
	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;
}
