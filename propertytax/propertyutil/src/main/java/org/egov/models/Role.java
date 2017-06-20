package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * minimal representation of the Roles in the system to be carried along in
 * UserInfo with RequestInfo meta data. Actual authorization service to extend
 * this to have more role related attributes Author : Narendra
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	@JsonProperty("name")
	@NotNull
	@Size(max = 64)
	private String name = null;

	@JsonProperty("description")
	private String description = null;
}
