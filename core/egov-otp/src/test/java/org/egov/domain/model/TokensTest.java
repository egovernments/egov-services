package org.egov.domain.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokensTest {

    @Test
    public void test_should_return_true_when_single_non_expired_token_is_present() {
        final Token token1 = mock(Token.class);
        when(token1.isExpired()).thenReturn(false);
        final Tokens tokens = new Tokens(Collections.singletonList(token1));

        assertTrue(tokens.hasSingleNonExpiredToken());
        assertEquals(token1, tokens.getNonExpiredToken());
    }

    @Test
    public void test_should_return_false_when_no_matching_tokens_are_present() {
        final Tokens tokens = new Tokens(Collections.emptyList());

        assertFalse(tokens.hasSingleNonExpiredToken());
        assertNull(tokens.getNonExpiredToken());
    }

    @Test
    public void test_should_return_false_when_matching_tokens_is_null() {
        final Tokens tokens = new Tokens(null);

        assertFalse(tokens.hasSingleNonExpiredToken());
    }

    @Test
    public void test_should_return_false_when_all_tokens_are_expired() {
        final Token token1 = mock(Token.class);
        when(token1.isExpired()).thenReturn(true);
        final Token token2 = mock(Token.class);
        when(token2.isExpired()).thenReturn(true);

        final Tokens tokens = new Tokens(Arrays.asList(token1, token2));

        assertFalse(tokens.hasSingleNonExpiredToken());
    }
}