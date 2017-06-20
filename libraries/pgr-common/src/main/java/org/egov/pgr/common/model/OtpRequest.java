package org.egov.pgr.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.pgr.common.model.exception.InvalidOtpRequestException;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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
