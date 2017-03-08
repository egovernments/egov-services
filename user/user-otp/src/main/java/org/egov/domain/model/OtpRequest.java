package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.domain.exception.InvalidOtpRequestException;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
@Builder
public class OtpRequest {
    private String mobileNumber;
    private String tenantId;

    public void validate() {
        if(isTenantIdAbsent() || isMobileNumberAbsent()) {
            throw new InvalidOtpRequestException(this);
        }
    }

    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    public boolean isMobileNumberAbsent() {
        return isEmpty(mobileNumber);
    }
}
