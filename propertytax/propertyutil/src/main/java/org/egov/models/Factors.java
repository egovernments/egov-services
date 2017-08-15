package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factors {
	
	@JsonProperty("name")
	private FactorEnum name = null;

	@JsonProperty("value")
	private Integer value = null;

}
