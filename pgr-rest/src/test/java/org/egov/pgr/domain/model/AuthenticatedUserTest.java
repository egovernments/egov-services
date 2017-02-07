package org.egov.pgr.domain.model;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticatedUserTest {

    @Test
    public void test_is_citizen_should_return_true_when_user_is_a_citizen() {
        final List<Role> roles = Collections.singletonList(new Role("Citizen"));
        final AuthenticatedUser user = AuthenticatedUser.builder()
                .roles(roles)
                .build();

        assertTrue(user.isCitizen());
    }

    @Test
    public void test_is_citizen_should_return_false_no_roles_are_present() {
        final AuthenticatedUser user = AuthenticatedUser.builder()
                .roles(null)
                .build();

        assertFalse(user.isCitizen());
    }

}