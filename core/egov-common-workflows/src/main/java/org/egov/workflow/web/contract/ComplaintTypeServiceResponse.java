package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ComplaintTypeServiceResponse {

	@JsonProperty("ComplaintType")
	private ComplaintTypeResponse complaintType;

}
