package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TokenRepositoryTest {

    @Mock
    private TokenJpaRepository tokenJpaRepository;

    @InjectMocks
    private TokenRepository tokenRepository;

    @Test
    public void test_should_save_entity_token() {
        final Token token = new Token("tenantId", "identity", "tokenNumber", "uuid");
        final org.egov.persistence.entity.Token expectedEntityToken =
                new org.egov.persistence.entity.Token();
        expectedEntityToken.setNumber("tokenNumber");
        expectedEntityToken.setTimeToLiveInSeconds(300L);
        expectedEntityToken.setId("uuid");
        expectedEntityToken.setIdentity("identity");

        final Token actualToken = tokenRepository.save(token);

        verify(tokenJpaRepository).save(argThat(new TokenEntityMatcher(expectedEntityToken)));

        assertEquals(token, actualToken);
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
            return expectedToken.getId().equals(actualToken.getId())
                    && expectedToken.getIdentity().equals(actualToken.getIdentity())
                    && expectedToken.getNumber().equals(actualToken.getNumber())
                    && expectedToken.getTimeToLiveInSeconds().equals(actualToken.getTimeToLiveInSeconds());

        }

    }
}