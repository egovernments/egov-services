package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.exception.ServiceCodeMandatoryException;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.springframework.stereotype.Service;

@Service
public class ServiceCodeValidator implements ServiceTypeConfigurationValidator {
	@Override
	public boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
		return true;
	}

	@Override
	public void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
		if (null == serviceTypeConfiguration.getServiceCode() && serviceTypeConfiguration.isServiceCodeAbsent()) {
			throw new ServiceCodeMandatoryException();
		}
	}

	@Override
	public boolean canValidater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
		return true;
	}

	@Override
	public void validater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
		if (null == serviceTypeConfigurationSearchCriteria.getServiceCode()
				&& serviceTypeConfigurationSearchCriteria.getServiceCode().isEmpty()) {
			throw new ServiceCodeMandatoryException();
		}
	}

}
