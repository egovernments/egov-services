package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.MandatoryAttributesAbsentException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MandatoryAttributeValidatorTest {

    @Test
    public void test_should_throw_exception_when_mandatory_attribute_entries_are_not_present_in_service_request() {
        final MandatoryAttributeValidator validator = new MandatoryAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeEntries())
            .thenReturn(Collections.singletonList(new AttributeEntry("code3", "value3")
            ));

        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code1")
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        try {
            validator.validate(serviceRequest, serviceDefinition);
            Assert.fail("Excepted validation to be thrown");
        } catch (MandatoryAttributesAbsentException ex) {
            final ArrayList<String> missingMandatoryAttributeCodes = ex.getMissingMandatoryAttributeCodes();
            assertEquals(2, missingMandatoryAttributeCodes.size());
            assertTrue("code1 is absent", missingMandatoryAttributeCodes.contains("code1"));
            assertTrue("code2 is absent", missingMandatoryAttributeCodes.contains("code2"));
        }
    }

    @Test
    public void test_should_not_throw_exception_when_mandatory_attribute_entries_are_present_in_service_request() {
        final MandatoryAttributeValidator validator = new MandatoryAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeEntries())
            .thenReturn(Arrays.asList(
                new AttributeEntry("code1", "value1"),
                new AttributeEntry("code2", "value2"),
                new AttributeEntry("code2", "value3"),
                new AttributeEntry("code3", "value4")
            ));
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code1")
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition);
    }

    @Test
    public void test_should_not_throw_exception_when_attributes_definitions_are_not_present() {
        final MandatoryAttributeValidator validator = new MandatoryAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeEntries())
            .thenReturn(Arrays.asList(
                new AttributeEntry("code1", "value1"),
                new AttributeEntry("code2", "value2"),
                new AttributeEntry("code2", "value3"),
                new AttributeEntry("code3", "value4")
            ));
        final List<AttributeDefinition> attributeDefinitions = Collections.emptyList();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition);
    }


}