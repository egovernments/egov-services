package org.egov.pgr.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class OtpValidationRequest {
	private String otpReference;
	private String mobileNumber;
	protected String tenantId;
}
