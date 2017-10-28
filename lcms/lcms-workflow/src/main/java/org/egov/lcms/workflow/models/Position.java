package org.egov.lcms.workflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Position class
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Position {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;
}