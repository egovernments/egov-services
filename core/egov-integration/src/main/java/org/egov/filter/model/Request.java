package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Request {

	private String url;
	private String basePath;
	private String type;
	private List<Parameter> headerParams;
	private List<Parameter> pathParams;
	private List<Parameter> queryParams;
	private String body;
	private List<Parameter> bodyParams;
	private Response response;
}
