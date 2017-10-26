package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Side {
	@NotNull
	@NotEmpty
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name = null;
	
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("active")
	private Boolean active = true;

	@NotNull
	@NotEmpty
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;
}
