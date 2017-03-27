package org.egov.lams.web.contract;

import org.egov.lams.model.Allottee;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CreateUserRequest {
	
	@JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
	
	@JsonProperty("User")
    private Allottee user;
}
