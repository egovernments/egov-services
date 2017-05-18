package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Contract class to send response. Array of Property items are used in case of
 * search results or response for create. Where as single Property item is used
 * for update
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PropertyResponse {

	private ResponseInfo responseInfo;

	private List<Property> properties;

}
