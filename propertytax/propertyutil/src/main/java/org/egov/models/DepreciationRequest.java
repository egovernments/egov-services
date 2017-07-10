package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Prasad Depreciation request model class
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("depreciations")
	private List<Depreciation> depreciations;

}
