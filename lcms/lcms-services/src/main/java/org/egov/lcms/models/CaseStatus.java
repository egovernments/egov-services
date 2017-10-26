package io.swagger.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseStatus {
	@NotEmpty
	@NotNull
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@NotEmpty
	@JsonProperty("active")
	private Boolean active = true;

	@NotEmpty
	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;
}
