package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.egov.domain.model.Tokens;
import org.egov.domain.model.ValidateRequest;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenRepositoryTest {

    private static final String TOKEN_NUMBER = "TOKEN_NUMBER";
    private static final String TENANT_ID = "TENANT_ID";
    private static final long TIME_TO_LIVE_IN_SECONDS = 300L;
    private static final String IDENTITY = "IDENTITY";

    @Mock
    private TokenJpaRepository tokenJpaRepository;

    @InjectMocks
    private TokenRepository tokenRepository;

    @Test
    public void test_should_save_entity_token() {
        final org.egov.persistence.entity.Token expectedEntityToken =
                new org.egov.persistence.entity.Token();

        expectedEntityToken.setNumber(TOKEN_NUMBER);
        expectedEntityToken.setTimeToLiveInSeconds(TIME_TO_LIVE_IN_SECONDS);
        expectedEntityToken.setIdentity(IDENTITY);
        expectedEntityToken.setTenant(TENANT_ID);
        final TokenRequest tokenRequest = mock(TokenRequest.class);
        when(tokenRequest.getIdentity()).thenReturn(IDENTITY);
        when(tokenRequest.getTenantId()).thenReturn(TENANT_ID);
        when(tokenRequest.generateToken()).thenReturn(TOKEN_NUMBER);
        when(tokenRequest.getTimeToLive()).thenReturn(TIME_TO_LIVE_IN_SECONDS);
        when(tokenJpaRepository.save(argThat(new TokenEntityMatcher(expectedEntityToken))))
                .thenReturn(expectedEntityToken);

        final Token actualToken = tokenRepository.save(tokenRequest);

        verify(tokenJpaRepository).save(argThat(new TokenEntityMatcher(expectedEntityToken)));
        assertNotNull(actualToken.getUuid());
        assertEquals(TOKEN_NUMBER, actualToken.getNumber());
        assertEquals(IDENTITY, actualToken.getIdentity());
        assertEquals(TENANT_ID, actualToken.getTenantId());
    }

    @Test
    public void test_should_retrieve_tokens_given_otp_number_identity_and_tenant() {
        final String otpNumber = "optNumber";
        final String identity = "identity";
        final String tenant = "tenant";
        final org.egov.persistence.entity.Token token1 = new org.egov
                .persistence.entity.Token();
        token1.setTimeToLiveInSeconds(200L);
        token1.setCreatedDate(new Date());
        final org.egov.persistence.entity.Token token2 = new org.egov
                .persistence.entity.Token();
        token2.setTimeToLiveInSeconds(200L);
        token2.setCreatedDate(new Date());
        when(tokenJpaRepository.findByNumberAndIdentityAndTenant(otpNumber, identity, tenant))
                .thenReturn(Arrays.asList(token1, token2));
        final ValidateRequest validateRequest = new ValidateRequest(tenant, otpNumber, identity);

        final Tokens actualTokens = tokenRepository.find(validateRequest);

        assertNotNull(actualTokens);
        assertNotNull(actualTokens.getTokens());
        assertEquals(2, actualTokens.getTokens().size());
    }

    @Test
    public void test_should_return_true_when_token_is_updated_to_validated() {
        final Token token = Token.builder()
                .uuid("uuid")
                .build();
        when(tokenJpaRepository.markTokenAsValidated("uuid")).thenReturn(1);

        final boolean updateSuccessful = tokenRepository.markAsValidated(token);
        assertTrue(updateSuccessful);
    }

    @Test
    public void test_should_return_false_when_token_is_not_updated_successfully() {
        final Token token = Token.builder()
                .uuid("uuid")
                .build();
        when(tokenJpaRepository.markTokenAsValidated("uuid")).thenReturn(0);

        final boolean updateSuccessful = tokenRepository.markAsValidated(token);
        assertFalse(updateSuccessful);
    }

    private class TokenEntityMatcher extends CustomMatcher<org.egov.persistence.entity.Token> {
        private org.egov.persistence.entity.Token expectedToken;

        public TokenEntityMatcher(org.egov.persistence.entity.Token expectedToken) {
            super("Entity token matcher");
            this.expectedToken = expectedToken;
        }

        @Override
        public boolean matches(Object o) {
            final org.egov.persistence.entity.Token actualToken = (org.egov.persistence.entity.Token) o;
            return expectedToken.getTenant().equals(actualToken.getTenant())
                    && expectedToken.getIdentity().equals(actualToken.getIdentity())
                    && expectedToken.getNumber().equals(actualToken.getNumber())
                    && expectedToken.getTimeToLiveInSeconds().equals(actualToken.getTimeToLiveInSeconds());

        }

    }
}