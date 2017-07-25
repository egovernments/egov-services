package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.InvalidDateAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateAttributeValidatorTest {

    @Test
    public void test_should_throw_exception_when_date_format_is_invalid() {
        final DateAttributeValidator validator = new DateAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("date1"))
            .thenReturn(new AttributeEntry("date1", "07-30-2017"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.DATE)
            .code("date1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();
        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
            Assert.fail("Expected exception to be thrown");
        } catch (InvalidDateAttributeEntryException ex) {
            assertEquals("date1", ex.getAttributeCode());
        }
    }

    @Test
    public void test_should_not_throw_exception_when_date_format_is_valid() {
        final DateAttributeValidator validator = new DateAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("date1"))
            .thenReturn(new AttributeEntry("date1", "15-07-2017"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.DATE)
            .code("date1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
    }


}