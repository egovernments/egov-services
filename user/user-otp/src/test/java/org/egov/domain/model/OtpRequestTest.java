package org.egov.domain.model;

import org.egov.domain.exception.InvalidOtpRequestException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OtpRequestTest {

    @Test(expected = InvalidOtpRequestException.class)
    public void test_should_throw_validation_exception_when_tenant_id_is_not_present() {
        final OtpRequest otpRequest = OtpRequest.builder()
                .tenantId(null)
                .mobileNumber("mobile number")
                .build();
        
        assertTrue(otpRequest.isTenantIdAbsent());
        otpRequest.validate();
    }

    @Test(expected = InvalidOtpRequestException.class)
    public void test_should_throw_validation_exception_when_mobile_number_is_not_present() {
        final OtpRequest otpRequest = OtpRequest.builder()
                .tenantId("tenantId")
                .mobileNumber(null)
                .build();

        assertTrue(otpRequest.isMobileNumberAbsent());
        otpRequest.validate();
    }

	@Test(expected = InvalidOtpRequestException.class)
	public void test_should_throw_validation_exception_when_type_is_not_present() {
		final OtpRequest otpRequest = OtpRequest.builder()
				.tenantId("tenantId")
				.mobileNumber("mobileNumber")
				.type(null)
				.build();

		assertTrue(otpRequest.isInvalidType());
		otpRequest.validate();
	}

	@Test
	public void test_validate_should_not_throw_exception_for_a_valid_request() {
		final OtpRequest otpRequest = OtpRequest.builder()
				.tenantId("tenantId")
				.mobileNumber("mobileNumber")
				.type(OtpRequestType.REGISTER)
				.build();

		otpRequest.validate();
	}

}