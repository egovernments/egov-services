package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.domain.InvalidTokenException;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Token {
    private static final int TTL_IN_SECONDS = 300;
    private final String tenantId;
    private String identity;
    private String number;
    private String uuid;

    public Token(String identity, String tenantId) {
        this.identity = identity;
        this.tenantId = tenantId;
        this.number = randomNumeric(5);
        this.uuid = UUID.randomUUID().toString();
    }

    public void valid() {
        if (isEmpty(identity) || isEmpty(number)) {
            throw new InvalidTokenException();
        }
    }

    public long getTimeToLive() {
        return TTL_IN_SECONDS;
    }
}
