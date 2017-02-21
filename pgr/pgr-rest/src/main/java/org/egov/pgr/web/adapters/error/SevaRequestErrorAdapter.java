package org.egov.pgr.web.adapters.error;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.web.contract.Error;
import org.egov.pgr.web.contract.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class SevaRequestErrorAdapter implements ErrorAdapter<Complaint> {

    private static final String LOCATION_MANDATORY_MESSAGE = "latitude/longitude or cross hierarcy id is required";
    private static final String COMPLAINANT_MANDATORY_MESSAGE = "is required for anonymous complaint";
    private static final String MANDATORY_LOCATION_ERROR_CODE = "latlng_crshrchy_req";
    private static final String LATITUDE_FIELD_NAME = "service_request.latitude";
    private static final String LONGITUDE_FIELD_NAME = "service_request.long";
    private static final String CROSS_HIERARCHY_FIELD_NAME = "service_request.cross_hierarchy_id";
    private static final String MANDATORY_FIRST_NAME_CODE = "name_req";
    private static final String MANDATORY_PHONE_NUMBER_CODE = "phone_req";
    private static final String MANDATORY_EMAIL_CODE = "email_req";
    private static final String FIRST_NAME_FIELD_NAME = "service_request.firstName";
    private static final String PHONE_FIELD_NAME = "service_request.phone";
    private static final String EMAIL_FIELD_NAME = "service_request.email";

    @Override
    public ErrorResponse adapt(Complaint model) {
        List<Error> errors = new ArrayList<>();
        addLocationValidationErrors(model, errors);
        addComplainantValidationErrors(model, errors);
        return new ErrorResponse(null, errors);
    }

    private void addComplainantValidationErrors(Complaint model, List<Error> errors) {
        if (model.isMandatoryFieldsAbsentForAnonymousComplaint()) {
            errors.add(new Error(MANDATORY_FIRST_NAME_CODE, COMPLAINANT_MANDATORY_MESSAGE, FIRST_NAME_FIELD_NAME));
            errors.add(new Error(MANDATORY_PHONE_NUMBER_CODE, COMPLAINANT_MANDATORY_MESSAGE, PHONE_FIELD_NAME));
            errors.add(new Error(MANDATORY_EMAIL_CODE, COMPLAINANT_MANDATORY_MESSAGE, EMAIL_FIELD_NAME));
        }
    }

    private void addLocationValidationErrors(Complaint model, List<Error> errors) {
        if (model.isLocationAbsent()) {
            errors.add(new Error(MANDATORY_LOCATION_ERROR_CODE, LOCATION_MANDATORY_MESSAGE, LATITUDE_FIELD_NAME));
            errors.add(new Error(MANDATORY_LOCATION_ERROR_CODE, LOCATION_MANDATORY_MESSAGE, LONGITUDE_FIELD_NAME));
            errors.add(new Error(MANDATORY_LOCATION_ERROR_CODE, LOCATION_MANDATORY_MESSAGE,
                    CROSS_HIERARCHY_FIELD_NAME));
        }
    }
}
