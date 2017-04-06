package org.egov.pgr.read.web.adapters.error;


import org.egov.pgr.read.domain.model.Complaint;
import org.egov.pgr.read.web.contract.ErrorField;
import org.egov.pgr.read.web.contract.ErrorResponse;
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
    private Complaint complaint;

    @InjectMocks
    private SevaRequestErrorAdapter errorAdapter;

    @Test
    public void test_should_set_errors_when_location_details_are_not_present_for_a_create_request() {
        when(complaint.isLocationAbsent()).thenReturn(true);
        when(complaint.isRawLocationAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
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
    public void test_should_set_error_when_location_id_is_not_present_for_an_update_request() {
        when(complaint.isLocationAbsent()).thenReturn(true);
        when(complaint.isLocationIdAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0004", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.values.locationId", errorFields.get(0).getField());
        assertEquals("Location id is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_first_name_is_not_present() {
        when(complaint.isComplainantAbsent()).thenReturn(true);
        when(complaint.isComplainantFirstNameAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0005", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.firstName", errorFields.get(0).getField());
        assertEquals("First name is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_mobile_number_is_not_present() {
        when(complaint.isComplainantAbsent()).thenReturn(true);
        when(complaint.isComplainantPhoneAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0006", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.phone", errorFields.get(0).getField());
        assertEquals("Phone is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_receiving_mode_is_not_present() {
        when(complaint.isReceivingModeAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0009", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.values.receivingMode", errorFields.get(0).getField());
        assertEquals("Receiving mode is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_receiving_center_is_not_present() {
        when(complaint.isReceivingCenterAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0010", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.values.receivingCenter", errorFields.get(0).getField());
        assertEquals("Receiving center is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_tenant_id_is_not_present() {
        when(complaint.isTenantIdAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0011", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.tenantId", errorFields.get(0).getField());
        assertEquals("Tenant id is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_compaint_type_code_is_not_present() {
        when(complaint.isComplaintTypeAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0012", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.serviceCode", errorFields.get(0).getField());
        assertEquals("Service code is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_description_is_not_present() {
        when(complaint.isDescriptionAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0013", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.description", errorFields.get(0).getField());
        assertEquals("Description is required", errorFields.get(0).getMessage());
    }

    @Test
    public void test_should_set_error_when_crn_is_not_present() {
        when(complaint.isCrnAbsent()).thenReturn(true);

        final ErrorResponse errorResponse = errorAdapter.adapt(complaint);

        final List<ErrorField> errorFields = errorResponse.getErrorFields();
        assertNotNull(errorFields);
        assertEquals(1, errorFields.size());
        assertEquals("pgr.0014", errorFields.get(0).getCode());
        assertEquals("ServiceRequest.serviceRequestId", errorFields.get(0).getField());
        assertEquals("Service request id is required", errorFields.get(0).getMessage());
    }

}