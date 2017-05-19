package org.egov.pgrrest.common.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private org.egov.pgrrest.common.contract.ServiceRequest serviceRequest;

    @JsonIgnore
    public ServiceRequest toDomainForUpdateRequest() {
        return serviceRequest.toDomainForUpdateRequest(toAuthenticatedUser());
    }

    @JsonIgnore
    public ServiceRequest toDomainForCreateRequest() {
        return serviceRequest.toDomainForCreateRequest(toAuthenticatedUser());
    }

    @JsonIgnore
    public Long getUserId() {
        return getUserInfo() == null ? null : getUserInfo().getId();
    }

    @JsonIgnore
    private AuthenticatedUser toAuthenticatedUser() {
        final User userInfo = getUserInfo();
        if (userInfo == null) {
            return AuthenticatedUser.createAnonymousUser();
        }
        return AuthenticatedUser.builder()
            .type(UserType.valueOf(userInfo.getType()))
            .email(userInfo.getEmailId())
            .mobileNumber(userInfo.getMobileNumber())
            .name(userInfo.getName())
            .id(userInfo.getId())
            .build();
    }

    @JsonIgnore
    private User getUserInfo() {
        return requestInfo.getUserInfo();
    }

    public void update(ServiceRequest complaint) {
        serviceRequest.setCrn(complaint.getCrn());
    }
}