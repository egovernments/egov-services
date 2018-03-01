package org.egov.user.domain.v11.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class OtpRequest {
    private String identity;
    private String tenantId;
    private OtpRequestType type;
}