package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Response {
	
	private List<ResponseParam> responseParams;
	private String basePath;

}
