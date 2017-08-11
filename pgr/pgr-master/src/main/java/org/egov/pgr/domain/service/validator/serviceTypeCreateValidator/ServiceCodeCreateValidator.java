package org.egov.pgr.domain.service.validator.serviceTypeCreateValidator;

import java.util.HashMap;
import java.util.List;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.persistence.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceCodeCreateValidator implements ServiceTypeCreateValidator {

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Override
	public boolean canValidate(ServiceType serviceType) {
		return true;
	}


	@Override
	public void checkMandatoryField(ServiceType serviceType) {
		
		if (serviceType.isTenantIdAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "tanantId Vaalidator.1");
			error.put("field", "serviceType.tanantId");
			error.put("message", "tanantId mandatory ");
			throw new PGRMasterException(error);
		}
		
		if (serviceType.isServiceCodeAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "serviceCode Vaalidator.2");
			error.put("field", "serviceType.serviceCode");
			error.put("message", "serviceCode mandatory ");
			throw new PGRMasterException(error);
		}

		if (serviceType.isServiceNameAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "serviceName Vaalidator.3");
			error.put("field", "serviceType.serviceName");
			error.put("message", "serviceName mandatory ");
			throw new PGRMasterException(error);
		}
		
	}

	@Override
	public void validateUniqueCombinations(ServiceType serviceType) {

		List<org.egov.pgr.domain.model.ServiceType> serviceTypeList = serviceTypeRepository.getCodeTenantData(serviceType);
		
		if (!serviceTypeList.isEmpty()
				&& (serviceTypeList.get(0).getServiceCode().equalsIgnoreCase(serviceType.getServiceCode())
				&& serviceTypeList.get(0).getTenantId().equalsIgnoreCase(serviceType.getTenantId()))) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "ServiceCodeVaalidator.1");
			error.put("field", "serviceTypeConfiguration.serviceCode");
			error.put("message", "serviceCode already exists");
			throw new PGRMasterException(error);
		}
		
		if (!serviceTypeList.isEmpty()
				&& (serviceTypeList.get(0).getServiceCode().equalsIgnoreCase(serviceType.getServiceCode())
				&& serviceTypeList.get(0).getCategory() == serviceType.getCategory()
				&& (serviceTypeList.get(0).getServiceName().equalsIgnoreCase(serviceType.getServiceName()))
				&& serviceTypeList.get(0).getTenantId().equalsIgnoreCase(serviceType.getTenantId()))) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "ServiceCodeVaalidator.1");
			error.put("field", "serviceTypeConfiguration.serviceCode");
			error.put("message", "serviceCode already exists");
			throw new PGRMasterException(error);
		}
		
		
	}

}
