package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract class to send response. Array of Property items are used in case of
 * search results or response for create. Where as single Property item is used
 * for update Author : Narendra
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("properties")
	private List<Property> properties = new ArrayList<Property>();
}
