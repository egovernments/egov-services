package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AssignmentResponse {

	@JsonProperty("Assignment")
	private Assignment assignment;
}
