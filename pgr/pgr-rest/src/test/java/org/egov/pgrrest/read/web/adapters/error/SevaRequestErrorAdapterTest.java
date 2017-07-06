package org.egov.pgrrest.read.web.adapters.error;


import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SevaRequestErrorAdapterTest {

    @Mock
    private ServiceRequest complaint;

    @InjectMocks
    private SevaRequestErrorAdapter errorAdapter;

    @Test
    public void testShouldSetErrorsWhenLocationDetailsAreNotPresentForCreateRequest() {
        when(complaint.isLocationAbsent()).thenReturn(true);
        when(complaint.isRawLocationAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(3, errorFields.size());
        assertEquals("pgr.0001", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.lat", errorFields.get(0).getField());
        assertEquals("latitude/longitude or cross hierarcy id is required", errorFields.get(0).getMessage());
        assertEquals("pgr.0002", errorFields.get(1).getCode());
        assertEquals("ServiceRequest.lng", errorFields.get(1).getField());
        assertEquals("latitude/longitude or cross hierarcy id is required", errorFields.get(1).getMessage());
        assertEquals("pgr.0003", errorFields.get(2).getCode());
        assertEquals("ServiceRequest.addressId", errorFields.get(2).getField());
        assertEquals("latitude/longitude or cross hierarcy id is required", errorFields.get(2).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenLocationIdIsNotPresentForAnUpdateRequest() {
        when(complaint.isLocationAbsent()).thenReturn(true);
        when(complaint.isLocationIdAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0004", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.values.locationId", errorFields.get(0).getField());
        assertEquals("Location id is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenFirstNameIsNotPresent() {
        when(complaint.isRequesterAbsent()).thenReturn(true);
        when(complaint.isComplainantFirstNameAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0005", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.firstName", errorFields.get(0).getField());
        assertEquals("First name is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenMobileNumberIsNotPresent() {
        when(complaint.isRequesterAbsent()).thenReturn(true);
        when(complaint.isComplainantPhoneAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0006", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.phone", errorFields.get(0).getField());
        assertEquals("Phone is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenTenantIdIsNotPresent() {
        when(complaint.isTenantIdAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0011", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.tenantId", errorFields.get(0).getField());
        assertEquals("Tenant id is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenCompaintTypeCodeIsNotPresent() {
        when(complaint.isServiceRequestTypeAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0012", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.serviceCode", errorFields.get(0).getField());
        assertEquals("Service code is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenDescriptionIsNotPresent() {
        when(complaint.isDescriptionAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0013", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.description", errorFields.get(0).getField());
        assertEquals("Description is required", errorFields.get(0).getMessage());
    }

    @Test
    public void testShouldSetErrorWhenCrnIsNotPresent() {
        when(complaint.isCrnAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0014", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.serviceRequestId", errorFields.get(0).getField());
        assertEquals("Service request id is required", errorFields.get(0).getMessage());
    }
    
    @Test
    public void testShouldSetErrorWhenDescriptionIsLess() {
        when(complaint.descriptionLength()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0015", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.description", errorFields.get(0).getField());
        assertEquals("Description must have minimum 10 characters", errorFields.get(0).getMessage());
    }

}