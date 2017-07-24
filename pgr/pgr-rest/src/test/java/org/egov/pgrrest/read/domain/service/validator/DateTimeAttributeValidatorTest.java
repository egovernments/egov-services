package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.InvalidDateTimeAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeAttributeValidatorTest {

    @Test
    public void test_should_throw_exception_when_date_time_format_is_invalid() {
        final DateTimeAttributeValidator validator = new DateTimeAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("dateTime1"))
            .thenReturn(new AttributeEntry("dateTime1", "02-11-2017 56:32:45"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.DATE_TIME)
            .code("dateTime1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();
        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
            Assert.fail("Expected exception to be thrown");
        } catch (InvalidDateTimeAttributeEntryException ex) {
            assertEquals("dateTime1", ex.getAttributeCode());
        }
    }

    @Test
    public void test_should_not_throw_exception_when_date_time_format_is_valid() {
        final DateTimeAttributeValidator validator = new DateTimeAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("dateTime1"))
            .thenReturn(new AttributeEntry("dateTime1", "02-11-2017 23:32:45"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.DATE_TIME)
            .code("dateTime1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
    }

}