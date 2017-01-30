package org.egov.pgr.validators;

import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.SevaRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.hibernate.annotations.common.util.StringHelper.isEmpty;

public class SevaRequestValidator implements Validator {

    private boolean isByAnonymousUser = true;

    @Override
    public boolean supports(Class<?> aClass) {
        return SevaRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ServiceRequest serviceRequest = ((SevaRequest) target).getServiceRequest();
        validatePresenceOfUserFields(serviceRequest, errors);
        validateLocationDetailsPresent(serviceRequest, errors);
    }

    private void validateLocationDetailsPresent(ServiceRequest serviceRequest, Errors errors) {
        if (latLngProvided(serviceRequest) || crossHierarchyIdProvided(serviceRequest)) return;
        errors.rejectValue("serviceRequest.lat", "latlng_crshrchy_req","lat/lng or cross hierarcy id is required");
        errors.rejectValue("serviceRequest.lng", "latlng_crshrchy_req","lat/lng or cross hierarcy id is required");
        errors.rejectValue("serviceRequest.crossHierarchyId", "latlng_crshrchy_req","lat/lng or cross hierarcy id is required");
    }

    private boolean crossHierarchyIdProvided(ServiceRequest serviceRequest) {
        return isNotEmpty(serviceRequest.getCrossHierarchyId());
    }

    private boolean latLngProvided(ServiceRequest serviceRequest) {
        return serviceRequest.getLat() != null && serviceRequest.getLat() > 0 && serviceRequest.getLng() != null && serviceRequest.getLng() > 0;
    }

    private void validatePresenceOfUserFields(ServiceRequest servRequest, Errors errors) {
        if (isByAnonymousUser) {
            if (isEmpty(servRequest.getFirstName())) errors.rejectValue("serviceRequest.firstName", "name_req","is required for anonymous complaint");
            if (isEmpty(servRequest.getPhone())) errors.rejectValue("serviceRequest.phone", "phone_req","is required for anonymous complaint");
            if (isEmpty(servRequest.getEmail())) errors.rejectValue("serviceRequest.email", "email_req","is required for anonymous complaint");
        }
    }

}
