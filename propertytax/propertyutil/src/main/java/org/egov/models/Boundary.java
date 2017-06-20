package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object the boundary info Author : Narendra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Boundary {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;
}
