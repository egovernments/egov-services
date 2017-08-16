package org.egov.pgr.domain.service.validator;

import java.util.HashMap;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.exception.TenantIdMandatoryException;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantIdValidator implements ServiceTypeConfigurationValidator {

	@Autowired
	private ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

	@Override
	public boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
		return true;
	}

	@Override
	public void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
		if (serviceTypeConfiguration.isTenantIdAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "TenantIdValidator.3");
			error.put("field", "serviceTypeConfiguration.tenantid");
			error.put("message", "tenantid Required");

			throw new PGRMasterException(error);
		}
	}

	@Override
	public boolean canValidater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
		return true;
	}

	@Override
	public void validater(ServiceTypeConfigurationSearchCriteria serviceTypeConfigurationSearchCriteria) {
		if (serviceTypeConfigurationSearchCriteria.getTenantId().isEmpty()
				&& serviceTypeConfigurationSearchCriteria.getTenantId() == null) {
			throw new TenantIdMandatoryException();
		}
	}

	@Override
	public void checkCode(ServiceTypeConfiguration serviceTypeConfiguration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCode(ServiceTypeConfiguration serviceTypeConfiguration) {
		// TODO Auto-generated method stub

	}

}