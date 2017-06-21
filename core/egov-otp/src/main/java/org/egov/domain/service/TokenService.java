package org.egov.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.domain.exception.TokenValidationFailureException;
import org.egov.domain.model.*;
import org.egov.persistence.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TokenService {

    private TokenRepository tokenRepository;
    private LocalDateTimeFactory localDateTimeFactory;

    @Autowired
    public TokenService(TokenRepository tokenRepository,
						LocalDateTimeFactory localDateTimeFactory) {
        this.tokenRepository = tokenRepository;
		this.localDateTimeFactory = localDateTimeFactory;
	}

    public Token create(TokenRequest tokenRequest) {
        tokenRequest.validate();
        return tokenRepository.save(tokenRequest);
    }

    public Token validate(ValidateRequest validateRequest) {
        validateRequest.validate();
        final Tokens tokens = tokenRepository.find(validateRequest);
		final LocalDateTime now = localDateTimeFactory.now();

		if( !tokens.hasSingleNonExpiredToken(now)) {
            log.info("Token validation failure for otp #", validateRequest.getOtp());
            throw new TokenValidationFailureException();
        }

        final Token matchingToken = tokens.getNonExpiredToken(now);
        tokenRepository.markAsValidated(matchingToken);
        return matchingToken;
    }

    public Token search(TokenSearchCriteria searchCriteria) {
        return tokenRepository.findBy(searchCriteria);
    }
}
