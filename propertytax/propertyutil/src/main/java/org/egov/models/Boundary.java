package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object the boundary info Author : Narendra
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Boundary {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("name")
	private String name = null;
}
