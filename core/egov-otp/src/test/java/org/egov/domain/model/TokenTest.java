package org.egov.domain.model;

import org.egov.domain.InvalidTokenException;
import org.junit.Test;

public class TokenTest {

    @Test
    public void test_should_not_throw_validation_exception_when_mandatory_fields_are_present() {
        final Token token = Token.builder()
                .identity("identity")
                .number("randomTokenNumber")
                .build();

        token.valid();
    }

    @Test(expected = InvalidTokenException.class)
    public void test_should_throw_validation_exception_when_identity_not_present() {
        final Token token = Token.builder()
                .identity(null)
                .number("randomTokenNumber")
                .build();

        token.valid();
    }

    @Test(expected = InvalidTokenException.class)
    public void test_should_throw_validation_exception_when_random_token_number_not_present() {
        final Token token = Token.builder()
                .identity("identity")
                .number(null)
                .build();

        token.valid();
    }
}