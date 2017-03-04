package org.egov.web.contract;

import lombok.Getter;
import org.egov.domain.model.Token;

@Getter
public class OtpResponse {
    private ResponseInfo responseInfo;
    private Otp otp;

    public OtpResponse(Token token) {
        otp = new Otp(token);
    }
}

