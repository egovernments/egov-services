package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.user.domain.model.User;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("User")
    private UserRequest userRequest;

    public User toDomain() {
        return userRequest.toDomain();
    }
    
    public User toUpdateDomain() {
        return userRequest.toUpdateDomain();
    }

    public boolean isTenantIdPresent(){return this.getUserRequest().getTenantId()!=null && !this.getUserRequest().getTenantId().isEmpty();}
}
