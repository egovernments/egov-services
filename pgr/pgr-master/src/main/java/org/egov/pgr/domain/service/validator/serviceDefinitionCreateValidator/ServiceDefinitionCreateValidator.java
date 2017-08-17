package org.egov.pgr.domain.service.validator.serviceDefinitionCreateValidator;

import java.util.HashMap;
import java.util.List;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ServiceDefinition;
import org.egov.pgr.persistence.repository.ServiceDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDefinitionCreateValidator implements ServiceDefinitionValidator {

	@Autowired
	private ServiceDefinitionRepository serviceDefinitionRepository;

	@Override
	public boolean canValidate(ServiceDefinition serviceDefinition) {
		return true;
	}

	@Override
	public void checkMandatoryField(ServiceDefinition serviceDefinition) {

		if (serviceDefinition.isTenantIdAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "tanantId Vaalidator.1");
			error.put("field", "ServiceDefinition.tanantId");
			error.put("message", "tanantId Required");

			throw new PGRMasterException(error);
		}

		if (serviceDefinition.isCodeAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "DefinitionCode Vaalidator.2");
			error.put("field", "ServiceDefinition.DefinitionCode");
			error.put("message", "Code Required ");

			throw new PGRMasterException(error);
		}
		
	}
	@Override
	public void matchServiceandAttributeCodes(ServiceDefinition serviceDefinition)
	{
		if(serviceDefinition.valueDefinMandatoryFieldValidation(serviceDefinition))
		{
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "DefinitionCode Vaalidator.7");
			error.put("field", "DefinitionCode.codeMissMatch");
			error.put("message", "Miss Match in Attribute code And Service code  ");

			throw new PGRMasterException(error);
		}
	}

	@Override
	public void checkConstraints(ServiceDefinition serviceDefinition) {

		List<org.egov.pgr.domain.model.ServiceDefinition> complaintCodeList = serviceDefinitionRepository
				.getData(serviceDefinition);

		List<org.egov.pgr.domain.model.ServiceDefinition> definitionList = serviceDefinitionRepository
				.getDefinitionCode(serviceDefinition);

		if (complaintCodeList.isEmpty()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "DefinitionCode Vaalidator.3");
			error.put("field", "DefinitionTypeConfiguration.ServiceCode");
			error.put("message", "Mapping not exist ");
			throw new PGRMasterException(error);

		}

		if (!definitionList.isEmpty() && (definitionList.get(0).getCode().equalsIgnoreCase(serviceDefinition.getCode())
				&& definitionList.get(0).getTenantId().equalsIgnoreCase(serviceDefinition.getTenantId()))) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "DefinitionCode Vaalidator.4");
			error.put("field", "ServiceDefinitionConfiguration.code");
			error.put("message", "Data Present in Service Definition");
			throw new PGRMasterException(error);

		}
		

	}

	@Override
	public void checkLength(ServiceDefinition serviceDefinition) {
			
			if(!serviceDefinition.isTenantIdLengthMatch())	
			{
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "DefinitionCode Vaalidator.5");
			error.put("field", "DefinitionTypeConfiguration.tenant");
			error.put("message", "tenantId length should Min: 1 Characters. Max: 256 Characters");
			throw new PGRMasterException(error);
				
			}
			if(!serviceDefinition.isCodeLengthMatch())	
			{
				HashMap<String, String> error = new HashMap<>();
				error.put("code", "DefinitionCode Vaalidator.6");
				error.put("field", "DefinitionTypeConfiguration.code");
				error.put("message", "code length should  Min: 1 Characters. Max: 20 Characters");
				throw new PGRMasterException(error);
				
			}	}

}