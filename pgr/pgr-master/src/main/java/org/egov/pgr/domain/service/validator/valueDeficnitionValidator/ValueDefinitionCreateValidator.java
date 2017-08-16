package org.egov.pgr.domain.service.validator.valueDeficnitionValidator;

import java.util.HashMap;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ValueDefinition;
import org.springframework.stereotype.Service;

@Service
public class ValueDefinitionCreateValidator implements ValueDefinitionValidator {

	@Override
	public boolean canValidate(ValueDefinition valueDefinition) {
		return true;
	}

	@Override
	public void checkMandatoryField(ValueDefinition valueDefinition) {

		if (valueDefinition.isKeyAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "valueDefinition Vaalidator.1");
			error.put("field", "valueDefinition.key");
			error.put("message", "key Required");

			throw new PGRMasterException(error);
		}

		if (valueDefinition.isNameAbsent()) {
			HashMap<String, String> error = new HashMap<>();

			error.put("code", "valueDefinition Vaalidator.2");
			error.put("field", "valueDefinition.Name");
			error.put("message", "Name Required ");

			throw new PGRMasterException(error);
		}
	}

	@Override
	public void validateLength(ValueDefinition valueDefinition) {

		if (!valueDefinition.isKeyLengthMatch()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "valueDefinition Vaalidator.3");
			error.put("field", "valueDefinition.Key");
			error.put("message", "key should be >0 and <=50");
			throw new PGRMasterException(error);

		}

		if (!valueDefinition.isNameLengthMatch()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "valueDefinition Vaalidator.4");
			error.put("field", "valueDefinition.Name");
			error.put("message", "Name should be >0 and <=100");
			throw new PGRMasterException(error);

		}
	}

}