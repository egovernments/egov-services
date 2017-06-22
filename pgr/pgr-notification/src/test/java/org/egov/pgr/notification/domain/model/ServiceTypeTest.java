package org.egov.pgr.notification.domain.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceTypeTest {

    @Test
    public void test_should_return_true_when_service_type_has_complaint_keyword() {
        final List<String> keywords = Arrays.asList("keyword1", "complaint");
        final String serviceName = "service type name";
        final ServiceType serviceType = new ServiceType(serviceName, keywords);

        assertTrue(serviceType.isComplaintType());
    }

    @Test
    public void test_should_return_false_when_service_type_does_not_have_complaint_keyword() {
        final List<String> keywords = Arrays.asList("keyword1", "keyword2");
        final String serviceName = "service type name";
        final ServiceType serviceType = new ServiceType(serviceName, keywords);

        assertFalse(serviceType.isComplaintType());
    }

    @Test
    public void test_should_return_true_when_service_type_has_deliverable_keyword() {
        final List<String> keywords = Arrays.asList("keyword1", "deliverable");
        final String serviceName = "service type name";
        final ServiceType serviceType = new ServiceType(serviceName, keywords);

        assertTrue(serviceType.isDeliverableType());
    }

    @Test
    public void test_should_return_false_when_service_type_does_not_have_deliverable_keyword() {
        final List<String> keywords = Arrays.asList("keyword1", "keyword2");
        final String serviceName = "service type name";
        final ServiceType serviceType = new ServiceType(serviceName, keywords);

        assertFalse(serviceType.isDeliverableType());
    }

}