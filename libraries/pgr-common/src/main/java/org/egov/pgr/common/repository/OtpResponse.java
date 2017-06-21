package org.egov.pgr.common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
public class OtpResponse {
    private Otp otp;

    public String getOtpNumber() {
        return otp != null ? otp.getOtp() : null;
    }

    public boolean isOtpNumberAbsent() {
        return isEmpty(getOtpNumber());
    }

    public boolean isValidationComplete(String mobileNumber) {
        return otp != null && otp.isValidationComplete(mobileNumber);
    }
}


