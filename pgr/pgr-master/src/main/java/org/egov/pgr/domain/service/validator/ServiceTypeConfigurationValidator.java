package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;

public interface ServiceTypeConfigurationValidator {

	boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration);

	void validate(ServiceTypeConfiguration serviceTypeConfiguration);

	boolean canValidater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria);

	void validater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria);
	
	void checkCode(ServiceTypeConfiguration serviceTypeConfiguration);
	
	void updateCode(ServiceTypeConfiguration serviceTypeConfiguration);
}