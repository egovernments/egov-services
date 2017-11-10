package org.egov.dataupload.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Definition {

	@JsonProperty("fileName")
	private String fileName;
	
	@JsonProperty("apiRequest")
	private Object apiRequest;
	
	@JsonProperty("isBulkApi")
	private Boolean isBulkApi;
	
	@JsonProperty("uri")
	private String uri;	
	
}
