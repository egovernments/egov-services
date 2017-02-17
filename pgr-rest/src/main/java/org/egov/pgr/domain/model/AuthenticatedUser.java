package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticatedUser {
    private static final String CITIZEN = "Citizen";
    private String mobileNumber;
    private String emailId;
    private String name;
    private Integer id;
    private boolean anonymousUser;
    private List<Role> roles;

    public static AuthenticatedUser createAnonymousUser() {
        return AuthenticatedUser.builder().anonymousUser(true).build();
    }

    public boolean isCitizen() {
        return roles != null && roles.contains(new Role(CITIZEN));
    }

    public Complainant toComplainant() {
        return new Complainant(name, mobileNumber, emailId, null);
    }
}
