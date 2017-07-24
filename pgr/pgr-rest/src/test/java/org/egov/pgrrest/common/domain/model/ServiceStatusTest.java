package org.egov.pgrrest.common.domain.model;

import org.egov.pgrrest.read.domain.exception.UnknownServiceStatusException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceStatusTest {

    @Test
    public void test_should_return_service_status_enum_for_given_code() {
        final ServiceStatus actualStatus = ServiceStatus.parse("REGISTERED");

        assertEquals(ServiceStatus.COMPLAINT_REGISTERED, actualStatus);
    }

    @Test(expected = UnknownServiceStatusException.class)
    public void test_should_throw_exception_when_service_status_code_is_not_in_known_list() {
        ServiceStatus.parse("UNKNOWN");
    }
}