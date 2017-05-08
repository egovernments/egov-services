package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    public static AuthenticatedUser createAnonymousUser() {
        return AuthenticatedUser.builder()
            .anonymousUser(true)
            .type(UserType.SYSTEM)
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

}

