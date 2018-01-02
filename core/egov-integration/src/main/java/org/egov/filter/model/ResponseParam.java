package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseParam {
	
	private String source;
	private String destination;
	private TypeEnum sourceType;
	private TypeEnum destType;
	private List<String> valueMapping;
}
