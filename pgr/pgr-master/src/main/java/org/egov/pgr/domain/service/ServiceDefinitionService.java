package org.egov.pgr.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.domain.model.ServiceDefinition;
import org.egov.pgr.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgr.domain.service.validator.AttributedefinitionValidator.AttributeDefinitionCreateValidator;
import org.egov.pgr.domain.service.validator.serviceDefinitionCreateValidator.ServiceDefinitionCreateValidator;
import org.egov.pgr.domain.service.validator.valueDeficnitionValidator.ValueDefinitionCreateValidator;
import org.egov.pgr.domain.service.validator.valuedefinitionUniqueValidator.ValueDefinitionUniqueValuesValidator;
import org.egov.pgr.domain.service.validatorAttributedefinitionUniqueValidator.AttributeDefinitionUniqueValuesValidator;
import org.egov.pgr.persistence.dto.AttributeDefinition;
import org.egov.pgr.persistence.dto.ValueDefinition;
import org.egov.pgr.persistence.repository.AttributeDefinitionRepository;
import org.egov.pgr.persistence.repository.ServiceDefinitionMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceDefinitionRepository;
import org.egov.pgr.persistence.repository.ValueDefinitionRepository;
import org.egov.pgr.web.contract.ServiceDefinitionRequest;
import org.springframework.stereotype.Service;

@Service
public class ServiceDefinitionService {

	public static final String UPDATE = "UPDATE";
	private List<ServiceDefinitionCreateValidator> createValidators;
	private List<AttributeDefinitionCreateValidator> attributeValidate;
	private List<ValueDefinitionCreateValidator> valueDefinitionValidate;
	private List<AttributeDefinitionUniqueValuesValidator> attribUniqValidate;
	private List<ValueDefinitionUniqueValuesValidator> valueDefinValidate;


	private static final String CREATE = "CREATE";
	private ServiceDefinitionMessageQueueRepository serviceDefinitionMessageQueueRepository;
	private ServiceDefinitionRepository serviceDefinitionRepository;
	private AttributeDefinitionRepository attributeDefinitionRepository;
	private ValueDefinitionRepository valueDefinitionRepository;
	

	public ServiceDefinitionService(ServiceDefinitionMessageQueueRepository serviceDefinitionMessageQueueRepository,
			ServiceDefinitionRepository serviceDefinitionRepository,
			AttributeDefinitionRepository attributeDefinitionRepository,
			ValueDefinitionRepository valueDefinitionRepository,
			List<ServiceDefinitionCreateValidator> createValidators,
			List<AttributeDefinitionCreateValidator> attributeValidate,
			List<ValueDefinitionCreateValidator> valueDefinitionValidate,
			List<AttributeDefinitionUniqueValuesValidator> attribUniqValidate,
			List<ValueDefinitionUniqueValuesValidator> valueDefinValidate) {

		this.serviceDefinitionMessageQueueRepository = serviceDefinitionMessageQueueRepository;
		this.serviceDefinitionRepository = serviceDefinitionRepository;
		this.attributeDefinitionRepository = attributeDefinitionRepository;
		this.valueDefinitionRepository = valueDefinitionRepository;
		this.createValidators = createValidators;
		this.attributeValidate = attributeValidate;
		this.valueDefinitionValidate = valueDefinitionValidate; 
		this.attribUniqValidate = attribUniqValidate;
		this.valueDefinitionRepository = valueDefinitionRepository;
		this.valueDefinValidate = valueDefinValidate;

	}
	
	public void create(ServiceDefinition serviceDefinition, ServiceDefinitionRequest request) {
		validateForCreate(serviceDefinition);
		serviceDefinitionMessageQueueRepository.save(request, CREATE);
	}

	public void update(ServiceDefinition serviceDefinition, ServiceDefinitionRequest request){
		serviceDefinitionMessageQueueRepository.save(request, UPDATE);
	}

	public void persist(ServiceDefinition serviceDefinition) {
		serviceDefinitionRepository.save(serviceDefinition.toDto());
		persistServiceTypeAttributes(serviceDefinition);
	}

	public void persistForUpdate(ServiceDefinition serviceDefinition){
        serviceDefinitionRepository.update(serviceDefinition.toDto());

	}

	public List<ServiceDefinition> search(ServiceDefinitionSearchCriteria serviceDefinitionSearchCriteria) {

		List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository
				.search(serviceDefinitionSearchCriteria);
		setAttributes(serviceDefinitionList);

		return serviceDefinitionList;
	}

	private void validateForCreate(ServiceDefinition serviceDefinition) {
		createMandatoryFieldValidate(serviceDefinition);
		attributeMandatoryFieldValidation(serviceDefinition);
		valueDefinMandatoryFieldValidation(serviceDefinition);
		matchAttributeAndServiceCode(serviceDefinition);
		ServiceDefinitionFieldLengthValidate(serviceDefinition);
		valueDefLengthValidation(serviceDefinition);
		valueDefinDataTypeValidation(serviceDefinition);
		attributeLengthValidation(serviceDefinition);


		createUniqueConstraintValidation(serviceDefinition);
		attribUniqueConstraintValidation(serviceDefinition);
		valueDefinUniqueConstraintValidation(serviceDefinition);

	}

	private void persistServiceTypeAttributes(ServiceDefinition serviceDefinition) {
		List<AttributeDefinition> attributeDefinitionList = serviceDefinition.getAttributes().stream()
				.map(attributeDefinition -> attributeDefinition.toDto(serviceDefinition)).collect(Collectors.toList());

		attributeDefinitionList.forEach(this::persistAttribute);
	}

	private void persistAttribute(AttributeDefinition attributeDefinition) {
		attributeDefinitionRepository.save(attributeDefinition);

		attributeDefinition.getValueDefinitions().forEach(this::persistValueDefinition);
	}

	private void persistValueDefinition(ValueDefinition valueDefinition) {
		valueDefinitionRepository.save(valueDefinition);
	}

	private void createMandatoryFieldValidate(ServiceDefinition serviceDefinition) {
		createValidators.stream().filter(validator -> validator.canValidate(serviceDefinition))
				.forEach(v -> v.checkMandatoryField(serviceDefinition));
	}
	
	private void ServiceDefinitionFieldLengthValidate(ServiceDefinition serviceDefinition) {
		createValidators.stream().filter(validator -> validator.canValidate(serviceDefinition))
				.forEach(v -> v.checkLength(serviceDefinition));
	}

	private void createUniqueConstraintValidation(ServiceDefinition serviceDefinition) {
		createValidators.stream().filter(validator -> validator.canValidate(serviceDefinition))
				.forEach(v -> v.checkConstraints(serviceDefinition));
	}
	
	private void attributeLengthValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
		{
			attributeValidate.stream().filter(validator -> 
			validator.canValidate(attributeDefinition))
			.forEach(v -> v.validatingLength(attributeDefinition));
		});
		
	}
	
	
	private void attributeMandatoryFieldValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
		{
			attributeValidate.stream().filter(validator -> 
			validator.canValidate(attributeDefinition))
			.forEach(v -> v.checkMandatoryField(attributeDefinition));
		});
		
	}
	
	private void valueDefLengthValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
		{
			attributeDefinition.getValueDefinitions().stream().forEach(valueDefinition ->
			{
			valueDefinitionValidate.stream().filter(validator -> 
			validator.canValidate(valueDefinition))
			.forEach(v -> v.validateLength(valueDefinition));
		});
		});
		
	}
	
	private void valueDefinMandatoryFieldValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
		{
			attributeDefinition.getValueDefinitions().stream().forEach(valueDefinition ->
			{
			valueDefinitionValidate.stream().filter(validator -> 
			validator.canValidate(valueDefinition))
			.forEach(v -> v.checkMandatoryField(valueDefinition));
		});
		});
		
	}
	
	private void valueDefinUniqueConstraintValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
	{			
		attributeDefinition.getValueDefinitions().stream().forEach(valueDefinition ->
		{
				valueDefinValidate.stream().filter(validator -> 
			validator.canValidate(valueDefinition))
				.forEach(v -> v.validateUniqueConstratint(valueDefinition,serviceDefinition.getCode(),attributeDefinition.getCode(),serviceDefinition.getTenantId()));
			});
			});
	}
	
	private void valueDefinDataTypeValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
	{			
		
		attributeValidate.stream().filter(validator -> 
			validator.canValidate(attributeDefinition))
				.forEach(v -> v.validateDataType(attributeDefinition));
		
			});
	}
	
		
	private void attribUniqueConstraintValidation(ServiceDefinition serviceDefinition) {
		serviceDefinition.getAttributes().stream().forEach(attributeDefinition -> 
		{			
		
			attribUniqValidate.stream().filter(validator -> validator.canValidate(attributeDefinition))
				.forEach(v -> v.validateUniqueConstratint(attributeDefinition));
	});
	}
	
	
	private void matchAttributeAndServiceCode(ServiceDefinition serviceDefinition) {
		createValidators.stream().filter(validator -> validator.canValidate(serviceDefinition))
				.forEach(v -> v.matchServiceandAttributeCodes(serviceDefinition));
	} 
	
	private void setAttributes(List<ServiceDefinition> serviceDefinitions) {
		serviceDefinitions.forEach(serviceDefinition -> serviceDefinition.setAttributes(attributeDefinitionRepository
				.searchByCodeAndTenant(serviceDefinition.getCode(), serviceDefinition.getTenantId())));
	}
}