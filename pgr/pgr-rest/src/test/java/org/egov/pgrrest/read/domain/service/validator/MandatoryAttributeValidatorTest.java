package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
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
            .thenReturn(Collections.singletonList(new AttributeEntry("code3", "value3")));
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .roleCodes(Arrays.asList("CITIZEN"))
            .build();
        when(serviceRequest.getAuthenticatedUser()).thenReturn(user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code1")
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .roles(Arrays.asList(new AttributeRolesDefinition("CITIZEN")))
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .roles(Arrays.asList(new AttributeRolesDefinition("CITIZEN")))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
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
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .roleCodes(Collections.singletonList("CITIZEN"))
            .build();
        when(serviceRequest.getAuthenticatedUser()).thenReturn(user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code1")
            .roles(Collections.singletonList(new AttributeRolesDefinition("CITIZEN")))
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .roles(Collections.singletonList(new AttributeRolesDefinition("CITIZEN")))
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }

    @Test
    public void
    test_should_not_throw_exception_when_mandatory_attribute_entries_are_not_present_in_service_request_and_current_action_does_not_match() {
        final MandatoryAttributeValidator validator = new MandatoryAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeEntries())
            .thenReturn(Collections.singletonList(
                new AttributeEntry("code1", "value1")
            ));
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .roleCodes(Collections.singletonList("CITIZEN"))
            .build();
        when(serviceRequest.getAuthenticatedUser()).thenReturn(user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .roles(Collections.singletonList(new AttributeRolesDefinition("CITIZEN")))
            .build();
        final List<AttributeDefinition> attributeDefinitions = Collections.singletonList(attributeDefinition1);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_PROCESSING);
    }

    @Test
    public void test_should_not_throw_exception_when_mandatory_attribute_entries_are_not_present_in_service_request_and_current_user_roles_does_not_match() {
        final MandatoryAttributeValidator validator = new MandatoryAttributeValidator();
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAttributeEntries())
            .thenReturn(Collections.singletonList(
                new AttributeEntry("code1", "value1")
            ));
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .roleCodes(Collections.singletonList("EMPLOYEE"))
            .build();
        when(serviceRequest.getAuthenticatedUser()).thenReturn(user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .required(true)
            .code("code2")
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED)))
            .roles(Collections.singletonList(new AttributeRolesDefinition("CITIZEN")))
            .build();
        final List<AttributeDefinition> attributeDefinitions = Collections.singletonList(attributeDefinition1);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
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
        final AuthenticatedUser user = AuthenticatedUser.builder()
            .roleCodes(Collections.singletonList("CITIZEN"))
            .build();
        when(serviceRequest.getAuthenticatedUser()).thenReturn(user);
        final List<AttributeDefinition> attributeDefinitions = Collections.emptyList();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }


}