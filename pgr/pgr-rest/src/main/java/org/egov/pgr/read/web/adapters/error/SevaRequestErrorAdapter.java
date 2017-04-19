package org.egov.pgr.read.web.adapters.error;

import org.egov.pgr.read.domain.model.Complaint;
import org.egov.pgr.read.web.contract.Error;
import org.egov.pgr.read.web.contract.ErrorField;
import org.egov.pgr.read.web.contract.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class SevaRequestErrorAdapter implements ErrorAdapter<Complaint> {

    private static final String INVALID_SEVA_REQUEST_MESSAGE = "SevaRequest is invalid";

    private static final String LOCATION_MANDATORY_MESSAGE = "latitude/longitude or cross hierarcy id is required";

    private static final String MANDATORY_LATITUDE_ERROR_CODE = "pgr.0001";
    private static final String LATITUDE_FIELD_NAME = "ServiceRequest.lat";

    private static final String MANDATORY_LONGITUDE_ERROR_CODE = "pgr.0002";
    private static final String LONGITUDE_FIELD_NAME = "ServiceRequest.lng";

    private static final String MANDATORY_CROSS_HIERARCHY_ERROR_CODE = "pgr.0003";
    private static final String CROSS_HIERARCHY_FIELD_NAME = "ServiceRequest.addressId";

    private static final String MANDATORY_LOCATION_ID_ERROR_CODE = "pgr.0004";
    private static final String LOCATION_ID_FIELD_NAME = "ServiceRequest.values.locationId";
    private static final String LOCATION_ID_MANDATORY_MESSAGE = "Location id is required";

    private static final String MANDATORY_FIRST_NAME_CODE = "pgr.0005";
    private static final String FIRST_NAME_FIELD_NAME = "ServiceRequest.firstName";
    private static final String FIRST_NAME_MANDATORY_MESSAGE = "First name is required";

    private static final String MANDATORY_PHONE_NUMBER_CODE = "pgr.0006";
    private static final String PHONE_FIELD_NAME = "ServiceRequest.phone";
    private static final String PHONE_MANDATORY_MESSAGE = "Phone is required";

    private static final String MANDATORY_RECEIVING_MODE_CODE = "pgr.0009";
    private static final String RECEIVING_MODE_FIELD_NAME = "ServiceRequest.values.receivingMode";
    private static final String RECEIVING_MODE_MANDATORY_MESSAGE = "Receiving mode is required";

    private static final String MANDATORY_RECEIVING_CENTER_CODE = "pgr.0010";
    private static final String RECEIVING_CENTER_FIELD_NAME = "ServiceRequest.values.receivingCenter";
    private static final String RECEIVING_CENTER_MANDATORY_MESSAGE = "Receiving center is required";

    private static final String MANDATORY_TENANT_ID_CODE = "pgr.0011";
    private static final String TENANT_ID_FIELD_NAME = "ServiceRequest.tenantId";
    private static final String TENANT_ID_MANDATORY_MESSAGE = "Tenant id is required";

    private static final String MANDATORY_COMPLAINT_TYPE_CODE = "pgr.0012";
    private static final String COMPLAINT_TYPE_CODE_FIELD_NAME = "ServiceRequest.serviceCode";
    private static final String COMPLAINT_TYPE_CODE_MANDATORY_MESSAGE = "Service code is required";

    private static final String MANDATORY_DESCRIPTION_CODE = "pgr.0013";
    private static final String MANDATORY_DESCRIPTION_FIELD_NAME = "ServiceRequest.description";
    private static final String DESCRIPTION_MANDATORY_MESSAGE = "Description is required";

    private static final String MANDATORY_CRN_CODE = "pgr.0014";
    private static final String CRN_FIELD_NAME = "ServiceRequest.serviceRequestId";
    private static final String CRN_MANDATORY_MESSAGE = "Service request id is required";
    
	private static final String DESCRIPTION_LENGTH_CODE = "pgr.0015";
	private static final String DESCRIPTION_LENGTH_FIELD = "ServiceRequest.description";
	private static final String DESCRIPTION_LENGTH_MESSAGE = "Description must have minimum 10 characters";

    @Override
    public ErrorResponse adapt(Complaint model) {
        final Error error = getError(model);
        return new ErrorResponse(null, error);
    }

    private Error getError(Complaint model) {
        List<ErrorField> errorFields = getErrorFields(model);
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_SEVA_REQUEST_MESSAGE)
                .fields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(Complaint model) {
        List<ErrorField> errorFields = new ArrayList<>();
        addRawLocationValidationErrors(model, errorFields);
        addLocationIdValidationErrors(model, errorFields);
        addComplainantFirstNameValidationErrors(model, errorFields);
        addComplainantMobileValidationErrors(model, errorFields);
        addReceivingModeValidationErrors(model, errorFields);
        addReceivingCenterValidationErrors(model, errorFields);
        addTenantIdValidationErrors(model, errorFields);
        addComplaintTypeCodeValidationErrors(model, errorFields);
        addDescriptionValidationErrors(model, errorFields);
        addDescriptionLengthValidationErrors(model, errorFields);
        addCRNValidationErrors(model, errorFields);
        return errorFields;
    }

    private void addCRNValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isCrnAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_CRN_CODE)
                .message(CRN_MANDATORY_MESSAGE)
                .field(CRN_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addTenantIdValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isTenantIdAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_TENANT_ID_CODE)
                .message(TENANT_ID_MANDATORY_MESSAGE)
                .field(TENANT_ID_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addComplaintTypeCodeValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isComplaintTypeAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_COMPLAINT_TYPE_CODE)
                .message(COMPLAINT_TYPE_CODE_MANDATORY_MESSAGE)
                .field(COMPLAINT_TYPE_CODE_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addDescriptionValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isDescriptionAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_DESCRIPTION_CODE)
                .message(DESCRIPTION_MANDATORY_MESSAGE)
                .field(MANDATORY_DESCRIPTION_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addReceivingModeValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isReceivingModeAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_RECEIVING_MODE_CODE)
                .message(RECEIVING_MODE_MANDATORY_MESSAGE)
                .field(RECEIVING_MODE_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addReceivingCenterValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.isReceivingCenterAbsent()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_RECEIVING_CENTER_CODE)
                .message(RECEIVING_CENTER_MANDATORY_MESSAGE)
                .field(RECEIVING_CENTER_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addComplainantFirstNameValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!(model.isComplainantAbsent() && model.isComplainantFirstNameAbsent())) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_FIRST_NAME_CODE)
                .message(FIRST_NAME_MANDATORY_MESSAGE)
                .field(FIRST_NAME_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addComplainantMobileValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!(model.isComplainantAbsent() && model.isComplainantPhoneAbsent())) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_PHONE_NUMBER_CODE)
                .message(PHONE_MANDATORY_MESSAGE)
                .field(PHONE_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

    private void addRawLocationValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!(model.isLocationAbsent() && model.isRawLocationAbsent())) {
            return;
        }
        final ErrorField latitudeErrorField = ErrorField.builder()
                .code(MANDATORY_LATITUDE_ERROR_CODE)
                .message(LOCATION_MANDATORY_MESSAGE)
                .field(LATITUDE_FIELD_NAME)
                .build();
        errorFields.add(latitudeErrorField);
        final ErrorField longitudeErrorField = ErrorField.builder()
                .code(MANDATORY_LONGITUDE_ERROR_CODE)
                .message(LOCATION_MANDATORY_MESSAGE)
                .field(LONGITUDE_FIELD_NAME)
                .build();
        errorFields.add(longitudeErrorField);
        final ErrorField crossHierarchyErrorField = ErrorField.builder()
                .code(MANDATORY_CROSS_HIERARCHY_ERROR_CODE)
                .message(LOCATION_MANDATORY_MESSAGE)
                .field(CROSS_HIERARCHY_FIELD_NAME)
                .build();
        errorFields.add(crossHierarchyErrorField);
    }

    private void addLocationIdValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!(model.isLocationAbsent() && model.isLocationIdAbsent())) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_LOCATION_ID_ERROR_CODE)
                .message(LOCATION_ID_MANDATORY_MESSAGE)
                .field(LOCATION_ID_FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }
    
    private void addDescriptionLengthValidationErrors(Complaint model, List<ErrorField> errorFields) {
        if (!model.descriptionLength()) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(DESCRIPTION_LENGTH_CODE)
                .message(DESCRIPTION_LENGTH_MESSAGE)
                .field(DESCRIPTION_LENGTH_FIELD)
                .build();
        errorFields.add(errorField);
    }
}
