package org.egov.filter.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Parameter {

	private String name;
	private String source;
	private SourceInEnum in;
	private TypeEnum type;
	private String destination;
	
}
