package org.egov.web.contract;

import org.egov.domain.model.OtpRequestType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OtpRequestTest {

    @Test
    public void test_should_map_from_contract_to_domain() {
        final Otp otp = new Otp("mobileNumber", "tenantId", "register");
        final OtpRequest request = new OtpRequest(null, otp);

        final org.egov.domain.model.OtpRequest domainOtpRequest = request.toDomain();

        assertNotNull(domainOtpRequest);
        assertEquals("mobileNumber", domainOtpRequest.getMobileNumber());
        assertEquals("tenantId", domainOtpRequest.getTenantId());
        assertEquals("register", domainOtpRequest.getType());
        assertEquals(OtpRequestType.REGISTER, domainOtpRequest.getOtpRequestType());
    }

	@Test
	public void test_should_set_request_type_to_register_when_type_not_explicitly_specified() {
		final Otp otp = new Otp("mobileNumber", "tenantId", null);
		final OtpRequest request = new OtpRequest(null, otp);

		final org.egov.domain.model.OtpRequest domainOtpRequest = request.toDomain();

		assertEquals(OtpRequestType.REGISTER, domainOtpRequest.getOtpRequestType());
	}
}