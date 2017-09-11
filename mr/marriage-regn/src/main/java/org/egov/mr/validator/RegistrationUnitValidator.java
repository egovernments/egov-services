package org.egov.mr.validator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.RegistrationUnitRepository;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.egov.mr.web.errorhandler.Error;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationUnitValidator implements Validator {

	@Autowired
	private ErrorHandler error;

	@Autowired
	private RegistrationUnitRepository registrationUnitRepository;

	@Override
	public boolean supports(Class<?> clazz) {

		return RegnUnitRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegnUnitRequest regnUnitRequest = null;
		if (target instanceof RegnUnitRequest) {
			regnUnitRequest = (RegnUnitRequest) target;
		} else {
//			throw new RuntimeException("Invalid Object Type for Registration Unit");
			errors.rejectValue("Invalid Object Type", "Registration Unit validator");
		}
		validateForDuplicateRegnUnit(regnUnitRequest.getRegnUnit(), errors,
				regnUnitRequest.getRegnUnit().getTenantId());
	}

	private void validateForDuplicateRegnUnit(RegistrationUnit registrationUnit, Errors errors, String tenantid) {
		System.err.println("Inside Validator with ID : " + registrationUnit.getId());
		RegistrationUnitSearchCriteria registrationUnitSearchCriteria = RegistrationUnitSearchCriteria.builder()
				.name(registrationUnit.getName()).tenantId(tenantid).build();
		List<RegistrationUnit> regnUnit = registrationUnitRepository.search(registrationUnitSearchCriteria);
		log.info(" MarriageRegnValidator validateRegnUnit  regnUnit" + regnUnit.size());
		if (!(regnUnit.isEmpty())) {
			 errors.rejectValue("regnUnit","","registrationunit exists already");
//			throw new RuntimeException("registrationunit exists already");
		}
	}

}
