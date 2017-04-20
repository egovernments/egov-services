package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.User;

@AllArgsConstructor
@Getter
public class CreateUserRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("User")
    private UserRequest userRequest;

    public User toDomain() {
        return userRequest.toDomain(loggedInUserId());
    }

    private Long loggedInUserId() {
		return requestInfo.getUserInfo() == null ? null : requestInfo.getUserInfo().getId();
	}
    
}
