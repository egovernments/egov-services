package org.egov.user.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSearchRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;

	@JsonProperty("userName")
	private String userName;
}
