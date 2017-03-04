package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.springframework.stereotype.Service;

@Service
public class TokenRepository {

    private TokenJpaRepository tokenJpaRepository;

    public TokenRepository(TokenJpaRepository tokenJpaRepository) {
        this.tokenJpaRepository = tokenJpaRepository;
    }

    public Token save (Token token) {
        final org.egov.persistence.entity.Token entityToken =
                new org.egov.persistence.entity.Token(token);
        tokenJpaRepository.save(entityToken);
        return token;
    }
}
