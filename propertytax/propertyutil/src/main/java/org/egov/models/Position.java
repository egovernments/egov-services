package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Position {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;
}
