package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.read.domain.exception.UpdateServiceRequestNotAllowedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticatedUser {
    private String mobileNumber;
    private String email;
    private String name;
    private Long id;
    private boolean anonymousUser;
    private UserType type;
    private String tenantId;
    private List<String> roleCodes;
    public static final String GRIEVANCE_OFFICER = "GO";
    public static final String GRIEVANCE_ADMINISTRATOR = "GA";
    public static final String GRIEVANCE_ROUTING_OFFICER = "GRO";

    public static AuthenticatedUser createAnonymousUser() {
        return AuthenticatedUser.builder()
            .anonymousUser(true)
            .type(UserType.SYSTEM)
            .roleCodes(Collections.singletonList(UserType.SYSTEM.getValue()))
            .id(0L)
            .build();
    }

    public UserType getType() {
        return type;
    }

    public boolean isCitizen() {
        return getType() == UserType.CITIZEN;
    }

    public boolean isEmployee() {
        return getType() == UserType.EMPLOYEE;
    }

    public void validateUpdateEligibility() {
        boolean isRoleMatching = roleCodes.stream().anyMatch(rolesAllowedToUpdateServiceRequest()::contains);
        if (!isRoleMatching)
            throw new UpdateServiceRequestNotAllowedException();
    }

    private List<String> rolesAllowedToUpdateServiceRequest() {
        return Arrays.asList(GRIEVANCE_ADMINISTRATOR, GRIEVANCE_OFFICER, GRIEVANCE_ROUTING_OFFICER);
    }
}

