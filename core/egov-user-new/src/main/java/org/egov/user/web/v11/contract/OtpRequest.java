package org.egov.user.web.v11.contract;

import org.egov.user.domain.v11.model.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpRequest {
    private Otp otp;

    public OtpRequest(org.egov.user.domain.v11.model.OtpRequest otpRequest) {
        this.otp = Otp.builder()
                .tenantId(otpRequest.getTenantId())
                .identity(otpRequest.getIdentity())
                .build();
    }
}