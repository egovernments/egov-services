package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Getter
public class Tokens {
    private List<Token> tokens;

    public boolean hasSingleNonExpiredToken() {
        return !CollectionUtils.isEmpty(tokens)
                && tokens.stream().anyMatch(token -> !token.isExpired());
    }

    public Token getNonExpiredToken() {
        return tokens.stream()
                .filter(token -> !token.isExpired())
                .findFirst()
                .map(token -> token)
                .orElse(null);
    }
}
