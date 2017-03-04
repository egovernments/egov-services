package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.Token;

@Getter
@AllArgsConstructor
public class Otp {
    private String otp;
    private String uuid;
    private String identity;
    private String tenantId;

    public Otp(Token token) {
        otp = token.getNumber();
        uuid = token.getUuid();
        identity = token.getIdentity();
        tenantId = token.getTenantId();
    }
}
