package org.egov.asset.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Additional information of the asset.Hold the data for dynamic custom field in
 * JSON format. There key and value will be LABEL NAME and USER INPUT DATA
 * respactively.
 */

@Setter
@Getter
@ToString
public class Attributes {
	private String key = null;

	private String type = null;

	private Object value = null;

}
