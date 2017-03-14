package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test(expected = InvalidUserException.class)
    public void testUserWithEmptyNameIsInvalid() throws Exception {
        User user = User.builder()
                .mobileNumber("8899776655")
                .username("foolan_devi")
                .active(Boolean.TRUE)
                .gender(Gender.FEMALE)
                .type(UserType.CITIZEN)
                .build();

        assertTrue(user.isNameAbsent());
        user.validate();
    }

    @Test(expected = InvalidUserException.class)
    public void testUserWithEmptyUserNameIsInvalid() throws Exception {
        User user = User.builder()
                .mobileNumber("8899776655")
                .name("foolan")
                .active(Boolean.TRUE)
                .gender(Gender.FEMALE)
                .type(UserType.CITIZEN)
                .build();

        assertTrue(user.isUsernameAbsent());
        user.validate();
    }

    @Test(expected = InvalidUserException.class)
    public void testUserWithEmptyMobileIsInvalid() throws Exception {
        User user = User.builder()
                .username("foolan_devi")
                .name("foolan")
                .active(Boolean.TRUE)
                .gender(Gender.FEMALE)
                .type(UserType.CITIZEN)
                .build();

        assertTrue(user.isMobileNumberAbsent());
        user.validate();
    }

    @Test(expected = InvalidUserException.class)
    public void testUserWithEmptyGenderIsInvalid() throws Exception {
        User user = User.builder()
                .username("foolan_devi")
                .name("foolan")
                .mobileNumber("9988776655")
                .active(Boolean.TRUE)
                .type(UserType.CITIZEN)
                .build();

        assertTrue(user.isGenderAbsent());
        user.validate();
    }

    @Test(expected = InvalidUserException.class)
    public void testUserWithEmptyTypeIsInvalid() throws Exception {
        User user = User.builder()
                .username("foolan_devi")
                .name("foolan")
                .mobileNumber("9988776655")
                .active(Boolean.TRUE)
                .gender(Gender.FEMALE)
                .build();

        assertTrue(user.isTypeAbsent());
        user.validate();
    }

    @Test
    public void testUserWithAllMandatoryValuesProvidedIsValid() throws Exception {
        User user = User.builder()
                .username("foolan_devi")
                .name("foolan")
                .mobileNumber("9988776655")
                .active(Boolean.TRUE)
                .gender(Gender.FEMALE)
                .type(UserType.CITIZEN)
                .build();

        try {
            user.validate();
            assertFalse(user.isTypeAbsent());
            assertFalse(user.isGenderAbsent());
            assertFalse(user.isActiveIndicatorAbsent());
            assertFalse(user.isNameAbsent());
            assertFalse(user.isMobileNumberAbsent());
            assertFalse(user.isUsernameAbsent());
        } catch (InvalidUserException ie) {
            fail();
        }
    }

}