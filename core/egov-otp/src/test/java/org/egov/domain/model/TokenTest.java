package org.egov.domain.model;

import org.egov.domain.InvalidTokenException;
import org.junit.Test;

public class TokenTest {

    @Test
    public void test_should_not_throw_validation_exception_when_mandatory_fields_are_present() {
        final Token token = new Token("identity", "randomTokenNumber", null);

        token.valid();
    }

    @Test(expected = InvalidTokenException.class)
    public void test_should_throw_validation_exception_when_identity_not_present() {
        final Token token = new Token(null, "randomTokenNumber", null);

        token.valid();
    }

    @Test(expected = InvalidTokenException.class)
    public void test_should_throw_validation_exception_when_random_token_number_not_present() {
        final Token token = new Token("identity", null, null);

        token.valid();
    }
}