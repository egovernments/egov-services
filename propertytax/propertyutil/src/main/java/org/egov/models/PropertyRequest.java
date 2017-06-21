package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract class to receive request. Array of Property items are used in case
 * of create . Where as single Property item is used for update
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("properties")
	private List<Property> properties = new ArrayList<Property>();
}
