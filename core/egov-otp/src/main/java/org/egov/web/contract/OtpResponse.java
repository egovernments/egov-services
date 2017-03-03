package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpResponse {
    private ResponseInfo responseInfo;
    private String otp;
    private String id;
}

