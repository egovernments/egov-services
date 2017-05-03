package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.OtpRequestType;

@Getter
@AllArgsConstructor
public class OtpRequest {
    private RequestInfo requestInfo;
    private Otp otp;

    public org.egov.domain.model.OtpRequest toDomain() {
        return org.egov.domain.model.OtpRequest.builder()
                .mobileNumber(getMobileNumber())
                .tenantId(getTenantId())
				.type(getType())
                .build();
    }

	private OtpRequestType getType() {
		return otp != null ? otp.getTypeOrDefault() : null;
	}

	private String getMobileNumber() {
        return otp != null ? otp.getMobileNumber() : null;
    }


    private String getTenantId() {
        return otp != null ? otp.getTenantId() : null;
    }
}

