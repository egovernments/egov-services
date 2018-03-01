package org.egov.user.web.v11.contract;

import static org.springframework.util.StringUtils.isEmpty;

import org.egov.user.domain.v11.model.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}