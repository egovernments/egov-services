package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.MultipleAttributeValuesReceivedException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SingleValueAttributeValidatorTest {

    @Test
    public void test_should_thrown_exception_when_multiple_attribute_value_entries_present_for_attribute_of_date_type() {
        final SingleValueAttributeValidator validator = new SingleValueAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key1", "value1"),
            new AttributeEntry("key1", "value2"),
            new AttributeEntry("key2", "value3")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .code("key1")
            .dataType(AttributeDataType.DATE)
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();
        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
            Assert.fail("Expected exception to be thrown");
        } catch (MultipleAttributeValuesReceivedException ex) {
            assertEquals("key1", ex.getAttributeCode());
        }
    }

    @Test
    public void test_should_not_thrown_exception_when_single_attribute_value_present_for_attribute_of_date_type() {
        final SingleValueAttributeValidator validator = new SingleValueAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key1", "value1"),
            new AttributeEntry("key2", "value3")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .code("key1")
            .dataType(AttributeDataType.DATE)
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }

    private ServiceRequest createServiceRequest(List<AttributeEntry> attributeEntries) {
        return ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(ServiceRequestLocation.builder().build())
            .serviceRequestType(ServiceRequestType.builder().build())
            .attributeEntries(attributeEntries)
            .requester(Requester.builder().build())
            .build();
    }

}