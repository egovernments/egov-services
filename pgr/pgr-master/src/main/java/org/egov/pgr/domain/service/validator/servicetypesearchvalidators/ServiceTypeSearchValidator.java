package org.egov.pgr.domain.service.validator.servicetypesearchvalidators;


import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;

public interface ServiceTypeSearchValidator {
    boolean canValidate(ServiceTypeSearchCriteria serviceTypeSearchCriteria);
    void validate(ServiceTypeSearchCriteria serviceTypeSearchCriteria);
}
