package org.egov.persistence.repository;

import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.egov.domain.model.Tokens;
import org.egov.domain.model.ValidateRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenRepository {

    private static final int UPDATED_ROWS_COUNT = 1;
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

    @Transactional
    public boolean markAsValidated(Token token) {
        return tokenJpaRepository.markTokenAsValidated(token.getUuid()) == UPDATED_ROWS_COUNT;
    }

    public Tokens find(ValidateRequest request) {
        final List<org.egov.persistence.entity.Token> entityTokens =
                tokenJpaRepository
                        .findByNumberAndIdentityAndTenant(request.getOtp(), request.getIdentity(), request.getTenantId());
        final List<Token> domainTokens = entityTokens.stream()
                .map(org.egov.persistence.entity.Token::toDomain)
                .collect(Collectors.toList());
        return new Tokens(domainTokens);
    }
}
