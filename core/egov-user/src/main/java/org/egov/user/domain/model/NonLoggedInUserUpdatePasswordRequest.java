package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.exception.InvalidNonLoggedInUserUpdatePasswordRequestException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
public class NonLoggedInUserUpdatePasswordRequest {
	private String otpReference;
	private String mobileNumber;
	private String existingPassword;
	private String newPassword;
	private String tenantId;

	public void validate() {
		if (isModelInvalid()) {
			throw new InvalidNonLoggedInUserUpdatePasswordRequestException(this);
		}
	}

	public OtpValidationRequest getOtpValidationRequest() {
		return OtpValidationRequest.builder()
				.otpReference(otpReference)
				.mobileNumber(mobileNumber)
				.tenantId(tenantId)
				.build();
	}

	public boolean isOtpReferenceAbsent() {
		return isEmpty(otpReference);
	}

	public boolean isMobileNumberAbsent() {
		return isEmpty(mobileNumber);
	}

	public boolean isExistingPasswordAbsent() {
		return isEmpty(existingPassword);
	}

	public boolean isNewPasswordAbsent() {
		return isEmpty(newPassword);
	}

	private boolean isModelInvalid() {
		return isOtpReferenceAbsent() || isMobileNumberAbsent() || isExistingPasswordAbsent() || isNewPasswordAbsent();
	}
}
