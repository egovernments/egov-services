package org.egov.persistence.repository;

import org.egov.domain.TokenUpdateException;
import org.egov.domain.model.*;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenRepositoryTest {

    private static final String TOKEN_NUMBER = "TOKEN_NUMBER";
    private static final String TENANT_ID = "TENANT_ID";
    private static final long TIME_TO_LIVE_IN_SECONDS = 300L;
    private static final String IDENTITY = "IDENTITY";
    private static final String IST = "Asia/Calcutta";

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
        expectedEntityToken.setTenantId(TENANT_ID);
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
        when(tokenJpaRepository.findByNumberAndIdentityAndTenantId(otpNumber, identity, tenant))
                .thenReturn(Arrays.asList(token1, token2));
        final ValidateRequest validateRequest = new ValidateRequest(tenant, otpNumber, identity);

        final Tokens actualTokens = tokenRepository.find(validateRequest);

        assertNotNull(actualTokens);
        assertNotNull(actualTokens.getTokens());
        assertEquals(2, actualTokens.getTokens().size());
    }

    @Test
    public void test_should_return_token_for_given_id() {
        final String id = "uuid";
        final String tenantId = "tenant";
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 1, 1, 4, 0, 0, 0, ZoneId.of(IST));
        final Date createdDate = Date.from(zonedDateTime.toInstant());
        final org.egov.persistence.entity.Token token1 = new org.egov
                .persistence.entity.Token();
        token1.setTimeToLiveInSeconds(300L);
        token1.setCreatedDate(createdDate);
        when(tokenJpaRepository.findOne(id))
                .thenReturn(token1);
        final Token expectedToken = Token.builder()
                .timeToLiveInSeconds(300L)
                .expiryDateTime(LocalDateTime.of(2016, 1, 1, 4, 5, 0))
                .build();

        final Token actualToken = tokenRepository.findBy(new TokenSearchCriteria(id, tenantId));

        assertNotNull(actualToken);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void test_should_return_null_when_token_not_present_for_given_id() {
        final String id = "uuid";
        final String tenantId = "tenant";
        when(tokenJpaRepository.findOne(id)).thenReturn(null);

        final Token actualToken = tokenRepository.findBy(new TokenSearchCriteria(id, tenantId));

        assertNull(actualToken);
    }

    @Test
    public void test_should_return_true_when_token_is_updated_to_validated() {
        final Token token = Token.builder()
                .uuid("uuid")
                .build();
        when(tokenJpaRepository.markTokenAsValidated("uuid")).thenReturn(1);

        tokenRepository.markAsValidated(token);
        assertTrue(token.isValidated());
    }

    @Test(expected = TokenUpdateException.class)
    public void test_should_return_false_when_token_is_not_updated_successfully() {
        final Token token = Token.builder()
                .uuid("uuid")
                .build();
        when(tokenJpaRepository.markTokenAsValidated("uuid")).thenReturn(0);

        tokenRepository.markAsValidated(token);
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
            return expectedToken.getTenantId().equals(actualToken.getTenantId())
                    && expectedToken.getIdentity().equals(actualToken.getIdentity())
                    && expectedToken.getNumber().equals(actualToken.getNumber())
                    && expectedToken.getTimeToLiveInSeconds().equals(actualToken.getTimeToLiveInSeconds());

        }

    }
}