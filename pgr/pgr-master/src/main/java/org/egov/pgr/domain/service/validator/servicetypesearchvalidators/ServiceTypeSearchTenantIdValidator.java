package org.egov.pgr.domain.service.validator.servicetypesearchvalidators;

import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeSearchTenantIdValidator implements ServiceTypeSearchValidator {
    @Override
    public boolean canValidate(ServiceTypeSearchCriteria serviceTypeSearchCriteria) {
        return true;
    }

    @Override
    public void validate(ServiceTypeSearchCriteria serviceTypeSearchCriteria) {
    }
}