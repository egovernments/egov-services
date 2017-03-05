package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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