package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticatedUser {
    private String mobileNumber;
    private String emailId;
    private String name;
    private Integer id;
    private boolean anonymousUser;
    private List<Role> roles;
    private UserType type;

    public static AuthenticatedUser createAnonymousUser() {
        final UserType type = UserType.SYSTEM;
        return AuthenticatedUser.builder().anonymousUser(true).type(type).id(0).build();
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

    public Complainant toComplainant() {
        return new Complainant(name, mobileNumber, emailId, null, null);
    }
}

