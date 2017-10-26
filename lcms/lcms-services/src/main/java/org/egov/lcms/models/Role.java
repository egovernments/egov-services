package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	@JsonProperty("name")
	@NotNull
	@Size(max = 64)
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	public Role name(String name) {
		this.name = name;
		return this;
	}
}
