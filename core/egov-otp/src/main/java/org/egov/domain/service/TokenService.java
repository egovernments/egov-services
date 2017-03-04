package org.egov.domain.service;

import org.egov.domain.model.Token;
import org.egov.persistence.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(String identity) {
        final Token token = new Token(identity);
        return tokenRepository.save(token);
    }


}
