package org.egov.user.web.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OtpResponse {

    private Otp otp;
    private ResponseInfo responseInfo;

}
