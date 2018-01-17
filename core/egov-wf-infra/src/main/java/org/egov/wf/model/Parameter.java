package org.egov.wf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Parameter {

	private String name;
	private String jsonPath;
	private Boolean isMandatory;
	private String defaultValue;
	private TypeEnum type;
	
}
