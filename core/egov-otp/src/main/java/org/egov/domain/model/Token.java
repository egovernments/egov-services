package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Token {
    private final String tenantId;
    private String identity;
    private String number;
    private String uuid;
}
