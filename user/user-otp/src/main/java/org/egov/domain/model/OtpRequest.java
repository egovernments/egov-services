package org.egov.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.domain.exception.InvalidOtpRequestException;

import java.util.stream.Stream;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class OtpRequest {
    private String mobileNumber;
    private String tenantId;
    private String type;

    public void validate() {
        if(isTenantIdAbsent()
				|| isMobileNumberAbsent()
				|| isInvalidType()) {
            throw new InvalidOtpRequestException(this);
        }
    }

    public OtpRequestType getOtpRequestType() {
    	return OtpRequestType.valueOf(type.toUpperCase());
	}

	public boolean isRegistrationRequestType() {
    	return OtpRequestType.REGISTER.equals(getOtpRequestType());
	}

	public boolean isInvalidType() {
    	return isEmpty(type) || isTypeUnknown() ;
	}

	private boolean isTypeUnknown() {
    	return Stream.of(OtpRequestType.values())
				.noneMatch(type -> type.name().equalsIgnoreCase(this.type));
	}

    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    public boolean isMobileNumberAbsent() {
        return isEmpty(mobileNumber);
    }
}
