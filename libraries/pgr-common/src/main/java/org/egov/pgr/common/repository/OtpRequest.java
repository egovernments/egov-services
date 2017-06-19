package org.egov.pgr.common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OtpRequest {
    private Otp otp;

    public OtpRequest(org.egov.pgr.common.model.OtpRequest otpRequest) {
        this.otp = Otp.builder()
                .tenantId(otpRequest.getTenantId())
                .identity(otpRequest.getMobileNumber())
                .build();
    }
}

