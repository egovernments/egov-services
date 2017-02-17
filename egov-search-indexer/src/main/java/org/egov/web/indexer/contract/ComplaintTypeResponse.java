package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
	
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintTypeResponse {

	@JsonProperty("ComplaintType")
	private ComplaintType complaintType;

}