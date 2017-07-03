package org.egov.pgrrest.read.domain.model;

import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.UserType;
import org.egov.pgrrest.read.domain.exception.UpdateServiceRequestNotAllowedException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticatedUserTest {

    @Test
    public void test_is_citizen_should_return_true_when_user_is_a_citizen() {
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .type(UserType.CITIZEN)
            .build();

        assertTrue(user.isCitizen());
    }

    @Test
    public void test_is_citizen_should_return_false_when_user_user_is_not_a_citizen() {
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .type(UserType.EMPLOYEE)
            .build();

        assertFalse(user.isCitizen());
    }

    @Test(expected = UpdateServiceRequestNotAllowedException.class)
    public void test_to_check_any_role_doesnt_matche_to_userroles() {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
            .type(UserType.EMPLOYEE)
            .roleCodes(getUserRolesNotMatching())
            .build();

        authenticatedUser.validateUpdateEligibility();
    }

    @Test
    public void test_to_check_any_role_matches_to_userroles() {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
            .type(UserType.EMPLOYEE)
            .roleCodes(getUserRolesMatching())
            .build();

        authenticatedUser.validateUpdateEligibility();
    }

    private List<String> getUserRolesMatching() {
        return Arrays.asList("GRO");
    }

    private List<String> getUserRolesNotMatching() {
        return Arrays.asList("RO");
    }

}