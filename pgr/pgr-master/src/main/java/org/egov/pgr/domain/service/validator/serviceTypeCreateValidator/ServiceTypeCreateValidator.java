package org.egov.pgr.domain.service.validator.serviceTypeCreateValidator;

import org.egov.pgr.domain.model.ServiceType;

public interface ServiceTypeCreateValidator {
	boolean canValidate(ServiceType serviceType);

	void validateUniqueCombinations(ServiceType serviceType);

	void checkMandatoryField(ServiceType serviceType);

	void lengthValidate(ServiceType serviceType);

}
