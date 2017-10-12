package org.egov.mr.web.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Fee;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.model.Witness;
import org.egov.mr.model.enums.ServiceConfigurationKeys;
import org.egov.mr.repository.FeeRepository;
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.service.RegistrationUnitService;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.util.ValidateDobUtil;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarriageRegnValidator implements Validator  {

	@Autowired
	private RegistrationUnitService registrationUnitService;
	
	@Autowired
	private MarriageRegnService marriageRegnService;
	
	@Autowired
	private FeeRepository feeRepository;
	
	@Autowired
	private ServiceConfigurationService serviceConfigurationService;
	
	@Autowired
	private ValidateDobUtil validateDobUtil;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return MarriageRegnRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MarriageRegnRequest marriageRegnRequest = null;
		if (target instanceof MarriageRegnRequest)
			marriageRegnRequest = (MarriageRegnRequest) target;
		else
			throw new RuntimeException("Invalid Object type for MarriageRegn validator");
//		validateAge(marriageRegnRequest, errors);
		validateRegnUnit(marriageRegnRequest, errors);
		validateFee(marriageRegnRequest, errors);
		validateWitnessCount(marriageRegnRequest, errors);
	}

	public void validateFee(MarriageRegnRequest marriageRegnRequest,Errors error){
		Fee fee=marriageRegnRequest.getMarriageRegn().getFee();
		Set<String> ids= new HashSet<>();
		ids.add(fee.getId());
		FeeCriteria feeCriteria=FeeCriteria.builder().id(ids).tenantId(fee.getTenantId()).feeCriteria(fee.getFeeCriteria()).build();
		
		List<Fee> marriagefee = feeRepository.findForCriteria(feeCriteria);
		log.info(" MarriageRegnValidator validateFee  marriagefee"+marriagefee.size());
		if(marriagefee.isEmpty())
		{
			error.rejectValue("MarriageRegn","INVALID_FEE","provided Fee details Does not exist");
		}
	}
	
	public void validateAge(MarriageRegnRequest marriageRegnRequest, Errors error) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		List<Witness> witnesses = marriageRegn.getWitnesses();
		int stdDate = Integer.parseInt(propertiesManager.getStandardAgeOfMarryingPerson());
		int brideAge=validateDobUtil.getAge(marriageRegn.getMarriageDate(), marriageRegn.getBride().getDob());
		int brideGroomAge=validateDobUtil.getAge(marriageRegn.getMarriageDate(), marriageRegn.getBridegroom().getDob());
		if (stdDate > brideAge);
			error.rejectValue("MarriageRegn", "", "Bride age should not be less than " + stdDate);
		if (stdDate > brideGroomAge);
			error.rejectValue("MarriageRegn", "", "Bride groom age should not be less than " + stdDate);

		for (Witness witness : witnesses) {
			if (stdDate > validateDobUtil.getAge(marriageRegn.getMarriageDate(), witness.getDob()))
				error.rejectValue("MarriageRegn", "", "witness:" + witness.getName() + " age should not be less than " + stdDate);
		}
	}
	
	public void validateRegnUnit(MarriageRegnRequest marriageRegnRequest,Errors error){
		RegistrationUnit unit =marriageRegnRequest.getMarriageRegn().getRegnUnit();
		MarriageRegn marriageRegn=marriageRegnRequest.getMarriageRegn();
		RegistrationUnitSearchCriteria criteria=RegistrationUnitSearchCriteria.builder().id(unit.getId()).tenantId(unit.getTenantId()).build();
		
		List<RegistrationUnit>  registrationUnitsList = registrationUnitService.search(criteria);
		
		if(registrationUnitsList.isEmpty())
			error.rejectValue("MarriageRegn", "","The registration unit provided is not exists");
		MarriageRegnCriteria marriageRegnCriteria=MarriageRegnCriteria.builder().tenantId(marriageRegn.getTenantId()).build();
		/*List<MarriageRegn> regn= marriageRegnService.getMarriageRegns(marriageRegnCriteria,marriageRegnRequest.getRequestInfo());
		
		if(!regn.isEmpty())
			error.rejectValue("MarriageRegn", "","This Record exists already");*/
	}
	
	private void validateWitnessCount(MarriageRegnRequest marriageRegnRequest, Errors error) {
		List<Witness> witnessess = marriageRegnRequest.getMarriageRegn().getWitnesses();
		int witnessSizeConfigured = Integer.parseInt(serviceConfigurationService
				.getServiceConfigValueByKeyAndTenantId(ServiceConfigurationKeys.MARRIAGEWITNESSSIZE,
						marriageRegnRequest.getMarriageRegn().getTenantId())
				.toString());
		if (witnessess.size() == 0 || witnessess.size() < witnessSizeConfigured)
			error.rejectValue("MarriageRegn", "",
					"There are no witnesses or the number of witnessess are lesser than configured");

	}
	
	
}