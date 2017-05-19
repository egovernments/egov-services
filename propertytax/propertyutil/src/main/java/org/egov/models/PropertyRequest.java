package org.egov.models;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Contract class to receive request. Array of Property items are used in case
 * of create . Where as single Property item is used for update
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PropertyRequest {

	private RequestInfo requestInfo;

	private List<Property> properties;

}
