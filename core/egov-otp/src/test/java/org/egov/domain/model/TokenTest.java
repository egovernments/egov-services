package org.egov.domain.model;


import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_is_expired_should_return_false_when_token_expiry_is_in_the_future() {
        final Token token = Token.builder()
                .expiryDateTime(LocalDateTime.now(ZoneId.of(IST)).plusSeconds(30))
                .build();

        assertFalse(token.isExpired());
    }

    @Test
    public void test_is_expired_should_return_true_when_token_expiry_is_in_the_past() {
        final Token token = Token.builder()
                .expiryDateTime(LocalDateTime.now(ZoneId.of(IST)).minusSeconds(30))
                .build();

        assertTrue(token.isExpired());
    }


}