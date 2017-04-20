package org.egov.persistence.repository;

import org.egov.domain.TokenUpdateException;
import org.egov.domain.model.*;
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
    public Token markAsValidated(Token token) {
        token.setValidated(true);
        final boolean isUpdateSuccessful = tokenJpaRepository
                .markTokenAsValidated(token.getUuid()) == UPDATED_ROWS_COUNT;
        if (!isUpdateSuccessful) {
            throw new TokenUpdateException(token);
        }
        return token;
    }

    public Tokens find(ValidateRequest request) {
        final List<org.egov.persistence.entity.Token> entityTokens =
                tokenJpaRepository
                        .findByNumberAndIdentityAndTenantId(request.getOtp(), request.getIdentity(), request.getTenantId());
        final List<Token> domainTokens = entityTokens.stream()
                .map(org.egov.persistence.entity.Token::toDomain)
                .collect(Collectors.toList());
        return new Tokens(domainTokens);
    }

    public Token findBy(TokenSearchCriteria searchCriteria) {
        final org.egov.persistence.entity.Token entityToken =
                tokenJpaRepository.findOne(searchCriteria.getUuid());
        return entityToken != null ? entityToken.toDomain() : null;
    }
}
