package org.egov.user.web.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@NoArgsConstructor
public class OtpResponse {
    private Otp otp;
    private ResponseInfo responseInfo;

    public boolean isValidationComplete(String mobileNumber) {
        return otp.isValidationComplete(mobileNumber);
    }

}
