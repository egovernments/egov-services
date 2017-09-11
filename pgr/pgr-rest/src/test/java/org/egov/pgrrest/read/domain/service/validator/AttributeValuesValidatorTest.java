package org.egov.pgrrest.read.domain.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.read.domain.exception.InvalidAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class AttributeValuesValidatorTest {

    @Test
    public void test_should_not_throw_exception_when_attribute_values_are_null() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final ServiceRequest serviceRequest = createServiceRequest(null);

        validator.validate(serviceRequest);
    }

    @Test
    public void test_should_not_throw_exception_when_attribute_values_are_not_empty() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final List<AttributeEntry> attributeEntries = Collections.emptyList();
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }

    @Test
    public void test_should_not_throw_exception_when_attribute_value_entries_are_present_and_within_size_limits() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final List<AttributeEntry> attributeEntries =
            Collections.singletonList(new AttributeEntry("foo", "bar"));
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }

    @Test(expected = InvalidAttributeEntryException.class)
    public void test_should_throw_exception_when_key_is_not_present_in_attribute_value() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final List<AttributeEntry> attributeEntries =
            Collections.singletonList(new AttributeEntry(null, "bar"));
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }

    @Test(expected = InvalidAttributeEntryException.class)
    public void test_should_throw_exception_when_key_size_is_greater_than_500_chars_in_attribute_value() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final String key = StringUtils.repeat("k", 501);
        final List<AttributeEntry> attributeEntries = Collections.singletonList(new AttributeEntry(key, "bar"));
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }

    @Test(expected = InvalidAttributeEntryException.class)
    public void test_should_throw_exception_when_code_is_not_present_in_attribute_value() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final List<AttributeEntry> attributeEntries =
            Collections.singletonList(new AttributeEntry("key", null));
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }

    @Test(expected = InvalidAttributeEntryException.class)
    public void test_should_throw_exception_when_code_size_is_greater_than_500_chars_in_attribute_value() {
        final AttributeValuesValidator validator = new AttributeValuesValidator();
        final String code = StringUtils.repeat("k", 501);
        final List<AttributeEntry> attributeEntries = Collections.singletonList(new AttributeEntry("key", code));
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);

        validator.validate(serviceRequest);
    }


    private ServiceRequest createServiceRequest(List<AttributeEntry> attributeEntries) {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .build();
        final Requester requester = Requester.builder()
            .build();
        final ServiceRequestType serviceRequestType = ServiceRequestType.builder()
            .build();
        return ServiceRequest.builder()
            .attributeEntries(attributeEntries)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(serviceRequestLocation)
            .requester(requester)
            .serviceRequestType(serviceRequestType)
            .build();
    }

}
