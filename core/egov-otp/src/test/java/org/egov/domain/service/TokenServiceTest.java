package org.egov.domain.service;

import org.egov.domain.model.Token;
import org.egov.persistence.repository.TokenRepository;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void test_should_save_new_token_with_given_identity_and_tenant() {
        final String identity = "identity";
        final String tenantId = "tenant";
        final String randomTokenNumber = "randomTokenNumber";
        final Token savedToken = Token.builder()
                .number(randomTokenNumber)
                .identity(identity)
                .uuid("uuid")
                .build();
        when(tokenRepository.save(argThat(new TokenMatcher(identity, tenantId)))).thenReturn(savedToken);

        final Token actualToken = tokenService.createToken(identity, tenantId);

        assertEquals(savedToken, actualToken);
    }

    private class TokenMatcher extends CustomMatcher<Token> {
        private String identity;
        private String tenantId;

        TokenMatcher(String identity, String tenantId) {
            super("Domain token matcher");
            this.identity = identity;
            this.tenantId = tenantId;
        }

        @Override
        public boolean matches(Object o) {
            final Token actualToken = (Token) o;
            return identity.equals(actualToken.getIdentity())
                    && tenantId.equals(actualToken.getTenantId());
        }
    }
}