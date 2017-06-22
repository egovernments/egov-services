package org.egov.domain.model;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Token {
    @NotEmpty
    private final String tenantId;
    private String identity;
    private String number;
    private String uuid;
    private LocalDateTime expiryDateTime;
    private Long timeToLiveInSeconds;
    @Setter
    private boolean validated;

    public boolean isExpired(LocalDateTime now) {
        return now.isAfter(expiryDateTime);
    }
}


