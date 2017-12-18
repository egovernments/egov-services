package org.egov.dataupload.model;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Definition {

	@JsonProperty("defName")
	private String defName;
	
	@JsonProperty("apiRequest")
	private Object apiRequest;
	
	@JsonProperty("headerJsonPathMap")
	private Map<String, String> headerJsonPathMap;
	
	@JsonProperty("isBulkApi")
	private Boolean isBulkApi;
	
	@JsonProperty("arrayPath")
	private String arrayPath;
	
	@JsonProperty("uri")
	private String uri;	
	
	@JsonProperty("additionalResFields")
	private Map<String, String> additionalResFields;
	
}