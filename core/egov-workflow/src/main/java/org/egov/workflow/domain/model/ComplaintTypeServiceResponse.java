package org.egov.workflow.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintTypeServiceResponse {

	@JsonProperty("ComplaintType")
	private ComplaintTypeResponse complaintType;

}
