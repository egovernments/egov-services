package org.egov.pgr.web.contract;

import java.util.List;

import org.egov.pgr.persistence.queue.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;

	@JsonProperty("userName")
	private String userName;
}
