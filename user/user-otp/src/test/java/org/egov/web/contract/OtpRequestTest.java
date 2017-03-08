package org.egov.web.contract;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OtpRequestTest {

    @Test
    public void test_should_map_from_contract_to_domain() {
        final Otp otp = new Otp("mobileNumber", "tenantId");
        final OtpRequest request = new OtpRequest(null, otp);

        final org.egov.domain.model.OtpRequest domainOtpRequest = request.toDomain();

        assertNotNull(domainOtpRequest);
        assertEquals("mobileNumber", domainOtpRequest.getMobileNumber());
        assertEquals("tenantId", domainOtpRequest.getTenantId());
    }
}