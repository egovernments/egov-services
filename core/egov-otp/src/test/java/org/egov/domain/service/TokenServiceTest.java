package org.egov.domain.service;

import org.egov.domain.exception.TokenValidationFailureException;
import org.egov.domain.model.*;
import org.egov.persistence.repository.TokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private LocalDateTimeFactory localDateTimeFactory;

	@InjectMocks
	private TokenService tokenService;

	private LocalDateTime now;

	@Before
	public void before() {
		now = LocalDateTime.now(ZoneId.of("UTC"));
		when(localDateTimeFactory.now()).thenReturn(now);
	}

	@Test
	public void test_should_save_new_token_with_given_identity_and_tenant() {
		final Token savedToken = Token.builder().build();
		final TokenRequest tokenRequest = mock(TokenRequest.class);
		when(tokenRepository.save(tokenRequest)).thenReturn(savedToken);

		final Token actualToken = tokenService.create(tokenRequest);

		assertEquals(savedToken, actualToken);
	}

	@Test
	public void test_should_validate_token_request() {
		final TokenRequest tokenRequest = mock(TokenRequest.class);

		tokenService.create(tokenRequest);

		verify(tokenRequest).validate();
	}

	@Test
	public void test_should_validate_token_validation_request() {
		final Tokens tokens = mock(Tokens.class);
		final Token tokenToMarkAsValidated = mock(Token.class);
		when(tokens.getNonExpiredToken(now)).thenReturn(tokenToMarkAsValidated);
		when(tokens.hasSingleNonExpiredToken(now)).thenReturn(true);
		final ValidateRequest validateRequest = mock(ValidateRequest.class);
		when(tokenRepository.find(validateRequest)).thenReturn(tokens);

		tokenService.validate(validateRequest);

		verify(validateRequest).validate();
	}

	@Test(expected = TokenValidationFailureException.class)
	public void test_should_throw_exception_when_no_matching_non_expired_token_is_present() {
		final ValidateRequest validateRequest = new ValidateRequest("tenant", "otpNumber", "identity");
		final Tokens tokens = mock(Tokens.class);
		when(tokens.hasSingleNonExpiredToken(now)).thenReturn(false);
		when(tokenRepository.find(validateRequest)).thenReturn(tokens);

		tokenService.validate(validateRequest);
	}

	@Test
	public void test_should_return_token_when_token_is_successfully_updated_to_validated() {
		final ValidateRequest validateRequest = new ValidateRequest("tenant", "otpNumber", "identity");
		final Tokens tokens = mock(Tokens.class);
		final Token tokenToMarkAsValidated = mock(Token.class);
		when(tokens.getNonExpiredToken(now)).thenReturn(tokenToMarkAsValidated);
		when(tokens.hasSingleNonExpiredToken(now)).thenReturn(true);
		when(tokenRepository.find(validateRequest)).thenReturn(tokens);

		final Token token = tokenService.validate(validateRequest);

		assertEquals(tokenToMarkAsValidated, token);
	}

	@Test
	public void test_should_return_otp_for_given_search_criteria() {
		final Token expectedToken = Token.builder().build();
		final TokenSearchCriteria searchCriteria = new TokenSearchCriteria("uuid", "tenant");
		when(tokenRepository.findBy(searchCriteria)).thenReturn(expectedToken);

		final Token actualToken = tokenService.search(searchCriteria);

		assertEquals(expectedToken, actualToken);
	}


}