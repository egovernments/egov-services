package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemolitionResponse {
	
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("demolition")
	private Demolition demolition;
	
}
