package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad Depreciation request model class
 *
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("depreciations")
	private List<Depreciation> depreciations;

}
