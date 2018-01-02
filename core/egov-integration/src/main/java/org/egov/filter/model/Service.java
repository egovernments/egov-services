package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Service {
	
	private String name;
	private String description;
	private String fromEndPont;
	private FinalResponse finalResponse;
	private List<Request> requests;
	

}
