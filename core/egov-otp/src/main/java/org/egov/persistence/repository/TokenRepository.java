package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenRepository {

    private TokenJpaRepository tokenJpaRepository;

    public TokenRepository(TokenJpaRepository tokenJpaRepository) {
        this.tokenJpaRepository = tokenJpaRepository;
    }

    public Token save(TokenRequest tokenRequest) {
        final org.egov.persistence.entity.Token entityToken =
                new org.egov.persistence.entity.Token(tokenRequest);
        tokenJpaRepository.save(entityToken);
        return entityToken.toDomain();
    }
}
