package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class SearchRequestInfo {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
