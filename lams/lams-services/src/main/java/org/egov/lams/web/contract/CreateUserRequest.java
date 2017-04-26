package org.egov.lams.web.contract;

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
    private UserRequest user;
}
