package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract class to send response. Array of Property items are used in case of
 * search results or response for create. Where as single Property item is used
 * for update Author : Narendra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("properties")
	private List<Property> properties = new ArrayList<Property>();
}
