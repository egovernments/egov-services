package org.egov.web.contract;

import lombok.Getter;
import org.egov.domain.model.Token;

@Getter
public class OtpResponse {
    private ResponseInfo responseInfo;
    private String otp;
    private String id;

    public OtpResponse(Token token) {
        otp = token.getNumber();
        id = token.getUuid();
    }
}

