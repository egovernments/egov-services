package org.egov.propertyWorkflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Position class
 * 
 * @author Yosadhara
 *
 */
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