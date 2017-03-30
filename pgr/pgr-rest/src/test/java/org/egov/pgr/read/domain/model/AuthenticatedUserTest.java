package org.egov.pgr.read.domain.model;

import org.junit.Test;

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

}