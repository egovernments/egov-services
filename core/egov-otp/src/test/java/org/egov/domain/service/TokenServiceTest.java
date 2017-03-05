package org.egov.domain.service;

import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.egov.persistence.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void test_should_save_new_token_with_given_identity_and_tenant() {
        final Token savedToken = Token.builder().build();
        final TokenRequest tokenRequest = mock(TokenRequest.class);
        when(tokenRepository.save(tokenRequest)).thenReturn(savedToken);

        final Token actualToken = tokenService.createToken(tokenRequest);

        assertEquals(savedToken, actualToken);
    }

    @Test
    public void test_should_validate_token_request() {
        final TokenRequest tokenRequest = mock(TokenRequest.class);

        tokenService.createToken(tokenRequest);

        verify(tokenRequest).validate();
    }

}