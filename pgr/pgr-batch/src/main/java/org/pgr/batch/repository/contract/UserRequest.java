package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Setter
@Getter
public class UserRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("tenantId")
	private String tenantId;
}
