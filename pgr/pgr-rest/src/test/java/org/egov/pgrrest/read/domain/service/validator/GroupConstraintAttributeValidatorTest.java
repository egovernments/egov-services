package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.GroupConstraintViolationException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupConstraintAttributeValidatorTest {

    @Test
    public void test_should_not_thrown_exception_when_no_group_constraint_present() {
        final GroupConstraintAttributeValidator validator = new GroupConstraintAttributeValidator();
        final List<AttributeEntry> attributeEntries = Collections.singletonList(
            new AttributeEntry("key3", "value1")
        );
        final AuthenticatedUser user = AuthenticatedUser.createAnonymousUser();
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries, user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .code("key1")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .code("key2")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final GroupDefinition groupDefinition = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.emptyList())
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .groups(Collections.singletonList(groupDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }

    @Test
    public void
    test_should_not_thrown_exception_when_group_constraint_is_all_required_and_corresponding_attribute_entries_are_present() {
        final GroupConstraintAttributeValidator validator = new GroupConstraintAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key1", "value1"),
            new AttributeEntry("key2", "value1"),
            new AttributeEntry("key3", "value1")
        );
        final AuthenticatedUser user = AuthenticatedUser.createAnonymousUser();
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries, user);
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .code("key1")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .code("key2")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final GroupConstraint groupConstraint = GroupConstraint.builder()
            .role("CITIZEN")
            .action(ServiceStatus.CITIZEN_SERVICE_NEW)
            .constraintType(GroupConstraintType.ALL_REQUIRED)
            .build();
        final GroupDefinition groupDefinition = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.singletonList(groupConstraint))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .groups(Collections.singletonList(groupDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }

    @Test
    public void
    test_should_not_thrown_exception_when_group_constraint_is_at_least_one_required_and_one_attribute_entry_is_present() {
        final GroupConstraintAttributeValidator validator = new GroupConstraintAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key1", "value1"),
            new AttributeEntry("key3", "value1")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries, getCitizen());
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .code("key1")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .code("key2")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final GroupConstraint groupConstraint = GroupConstraint.builder()
            .role("CITIZEN")
            .action(ServiceStatus.CITIZEN_SERVICE_NEW)
            .constraintType(GroupConstraintType.AT_LEAST_ONE_REQUIRED)
            .build();
        final GroupDefinition groupDefinition = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.singletonList(groupConstraint))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .groups(Collections.singletonList(groupDefinition))
            .build();

        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.COMPLAINT_REGISTERED);
    }

    @Test
    public void
    test_should_thrown_exception_when_group_constraint_is_at_least_one_required_and_no_attribute_entry_is_present() {
        final GroupConstraintAttributeValidator validator = new GroupConstraintAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key1", ""),
            new AttributeEntry("key3", "value1")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries, getCitizen());
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .code("key1")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .code("key2")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final GroupConstraint groupConstraint = GroupConstraint.builder()
            .role("CITIZEN")
            .action(ServiceStatus.CITIZEN_SERVICE_NEW)
            .constraintType(GroupConstraintType.AT_LEAST_ONE_REQUIRED)
            .build();
        final GroupDefinition groupDefinition = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.singletonList(groupConstraint))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .groups(Collections.singletonList(groupDefinition))
            .build();

        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_NEW);
            Assert.fail("Expected exception to be thrown");
        } catch (GroupConstraintViolationException ex) {
            assertEquals("groupCode1", ex.getGroupCode());
        }
    }

    @Test
    public void
    test_should_thrown_exception_when_group_constraint_is_all_required_and_all_attribute_entries_are_not_present() {
        final GroupConstraintAttributeValidator validator = new GroupConstraintAttributeValidator();
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("key2", ""),
            new AttributeEntry("key3", "value2")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries, getCitizen());
        final AttributeDefinition attributeDefinition1 = AttributeDefinition.builder()
            .code("key1")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final AttributeDefinition attributeDefinition2 = AttributeDefinition.builder()
            .code("key2")
            .groupCode("groupCode1")
            .dataType(AttributeDataType.STRING)
            .build();
        final GroupConstraint groupConstraint = GroupConstraint.builder()
            .role("CITIZEN")
            .action(ServiceStatus.CITIZEN_SERVICE_NEW)
            .constraintType(GroupConstraintType.ALL_REQUIRED)
            .build();
        final GroupDefinition groupDefinition = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.singletonList(groupConstraint))
            .build();
        final List<AttributeDefinition> attributeDefinitions =
            Arrays.asList(attributeDefinition1, attributeDefinition2);
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .groups(Collections.singletonList(groupDefinition))
            .build();

        try {
            validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_NEW);
            Assert.fail("Expected exception to be thrown");
        } catch (GroupConstraintViolationException ex) {
            assertEquals("groupCode1", ex.getGroupCode());
        }
    }

    private ServiceRequest createServiceRequest(List<AttributeEntry> attributeEntries, AuthenticatedUser user) {
        return ServiceRequest.builder()
            .authenticatedUser(user)
            .serviceRequestLocation(ServiceRequestLocation.builder().build())
            .serviceRequestType(ServiceRequestType.builder().build())
            .attributeEntries(attributeEntries)
            .requester(Requester.builder().build())
            .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder()
            .type(UserType.CITIZEN)
            .roleCodes(Collections.singletonList("CITIZEN"))
            .build();
    }


}