package org.egov.pgrrest.read.domain.service.validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.egov.pgrrest.common.domain.model.AttributeDataType;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.exception.InvalidLongAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Assert;
import org.junit.Test;

public class LongAttributeValidatorTest {

    @Test
    public void test_should_throw_exception_when_long_format_is_invalid() {
        final LongAttributeValidator validator = new LongAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("Long1"))
            .thenReturn(new AttributeEntry("Long1", "15.23"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.LONG)
            .code("Long1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();
        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
            Assert.fail("Expected exception to be thrown");
        } catch (InvalidLongAttributeEntryException ex) {
            assertEquals("Long1", ex.getAttributeCode());
        }
    }

    @Test
    public void test_should_not_throw_exception_when_long_format_is_valid() {
        final LongAttributeValidator validator = new LongAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeWithKey("Long1"))
            .thenReturn(new AttributeEntry("Long1", "9223372036855555"));
        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
            .dataType(AttributeDataType.LONG)
            .code("Long1")
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(Collections.singletonList(attributeDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
    }


}