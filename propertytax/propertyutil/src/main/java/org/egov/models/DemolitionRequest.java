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
public class DemolitionRequest {
	
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("requestInfo")
	private Demolition demolition;

}
