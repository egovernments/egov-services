package org.egov.pgr.domain.service.validator;

import java.util.HashMap;
import java.util.List;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.exception.ServiceCodeMandatoryException;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceCodeValidator implements ServiceTypeConfigurationValidator {

	@Autowired
	private ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

	@Override
	public boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
		return true;
	}

	@Override
	public void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
		if (null == serviceTypeConfiguration.getServiceCode() && serviceTypeConfiguration.isServiceCodeAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "ServiceCodeValidator.2");
			error.put("field", "serviceTypeConfiguration.serviceCode");
			error.put("message", "serviceCode Required");

			throw new PGRMasterException(error);

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

	@Override
	public void checkCode(ServiceTypeConfiguration serviceTypeConfiguration) {
		org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria searchCriteria = org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria
				.builder().serviceCode(serviceTypeConfiguration.getServiceCode())
				.tenantId(serviceTypeConfiguration.getTenantId()).build();
		List<org.egov.pgr.domain.model.ServiceTypeConfiguration> serviceTypeList = serviceTypeConfigurationRepository
				.search(searchCriteria);

		if (!serviceTypeList.isEmpty()
				&& serviceTypeList.get(0).getServiceCode().equalsIgnoreCase(searchCriteria.getServiceCode())) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "ServiceCodeVaalidator.1");
			error.put("field", "serviceTypeConfiguration.serviceCode");
			error.put("message", "Data already exists");
			throw new PGRMasterException(error);
		}
	}

	@Override
	public void updateCode(ServiceTypeConfiguration serviceTypeConfiguration) {
		org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria searchCriteria = org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria
				.builder().serviceCode(serviceTypeConfiguration.getServiceCode())
				.tenantId(serviceTypeConfiguration.getTenantId()).build();
		List<org.egov.pgr.domain.model.ServiceTypeConfiguration> serviceTypeList = serviceTypeConfigurationRepository
				.search(searchCriteria);

		if (serviceTypeList.isEmpty()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "ServiceCodeVaalidator.5");
			error.put("field", "serviceTypeConfiguration.serviceCode");
			error.put("message", "serviceCode not exists");
			throw new PGRMasterException(error);
		}
	}

}
