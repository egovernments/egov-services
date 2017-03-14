package org.egov.workflow.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;
}
