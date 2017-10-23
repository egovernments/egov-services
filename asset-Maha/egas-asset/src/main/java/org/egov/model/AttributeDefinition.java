package org.egov.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Hold the Asset dynamic fields details as list of json object.
 */

@Setter
@Getter
@ToString
public class AttributeDefinition {
	private String name = null;

	private String type = null;

	private Boolean isActive = null;

	private Boolean isMandatory = null;

	private String values = null;

	private String localText = null;

	private String regExFormate = null;

	private String url = null;

	private String order = null;

	private List<AttributeDefinition> columns = null;

}

