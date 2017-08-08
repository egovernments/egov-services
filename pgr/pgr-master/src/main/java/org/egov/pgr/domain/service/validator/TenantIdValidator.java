package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.exception.ServiceCodeMandatoryException;
import org.egov.pgr.domain.exception.TenantIdMandatoryException;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.springframework.stereotype.Service;

@Service
public class TenantIdValidator implements ServiceTypeConfigurationValidator {

    @Override
    public boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
        return true;
    }

    @Override
    public void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
        if(serviceTypeConfiguration.isTenantIdAbsent()){
            throw new TenantIdMandatoryException();
        }
    }
    
    @Override
    public boolean canValidater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
        return true;
    }

    @Override
    public void validater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
        if(serviceTypeConfigurationSearchCriteria.getTenantId().isEmpty() && serviceTypeConfigurationSearchCriteria.getTenantId()== null){
            throw new TenantIdMandatoryException();
        }
    }
    
}