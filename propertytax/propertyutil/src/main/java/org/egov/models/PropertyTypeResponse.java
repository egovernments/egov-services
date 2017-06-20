package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypeResponse {
	private ResponseInfo responseInfo;

	private List<PropertyType> propertyTypes;
}
