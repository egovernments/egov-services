package org.egov.dataupload.model;


import java.util.List;
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
	private Map<String, List<String>> headerJsonPathMap;
	
	@JsonProperty("isBulkApi")
	private Boolean isBulkApi;
	
	@JsonProperty("arrayPath")
	private String arrayPath;
		
	@JsonProperty("tenantIdPaths")
	private List<String> tenantIdPaths;
	
	@JsonProperty("uri")
	private String uri;	
	
	@JsonProperty("isParentChild")
	private Boolean isParentChild;
	
	@JsonProperty("uniqueParentKeys")
	private List<String> uniqueParentKeys;
	
	@JsonProperty("uniqueKeysForInnerObject")
	private List<String> uniqueKeysForInnerObject;
	
	@JsonProperty("additionalResFields")
	private Map<String, String> additionalResFields;
	
	@JsonProperty("templateFileName")
	private String templateFileName;
	
	
	
}