package org.egov.pgr.read.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;

@Setter
@Getter
public class UserRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;

	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("tenantId")
    private String tenantId;
}
