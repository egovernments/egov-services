package org.egov.pgr.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.pgr.common.model.exception.InvalidOtpValidationRequestException;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class OtpValidationRequest {
	private String otpReference;
	private String mobileNumber;
    private String tenantId;

    public void validate() {
        if (isOtpReferenceAbsent() || isMobileNumberAbsent() || isTenantIdAbsent()) {
            throw new InvalidOtpValidationRequestException(this);
        }
    }

    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    public boolean isMobileNumberAbsent() {
        return isEmpty(mobileNumber);
    }

    public boolean isOtpReferenceAbsent() {
        return isEmpty(otpReference);
    }

}
