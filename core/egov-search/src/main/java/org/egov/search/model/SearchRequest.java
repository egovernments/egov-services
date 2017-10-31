package org.egov.search.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ToString
public class SearchRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("searchCriteria")
	private Object searchCriteria;
  
}

