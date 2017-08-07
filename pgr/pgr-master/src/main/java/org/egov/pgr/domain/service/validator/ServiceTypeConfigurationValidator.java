package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;

public interface ServiceTypeConfigurationValidator {
	
	
    boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration);
    void validate(ServiceTypeConfiguration serviceTypeConfiguration);
    
}