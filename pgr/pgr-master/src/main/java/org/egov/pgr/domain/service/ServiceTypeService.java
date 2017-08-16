package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.domain.model.ServiceTypeKeyword;
import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.domain.service.validator.serviceTypeCreateValidator.ServiceTypeCreateValidator;
import org.egov.pgr.domain.service.validator.servicetypesearchvalidators.ServiceTypeSearchValidator;
import org.egov.pgr.domain.service.validator.servicetypevalidators.ServiceTypeValidator;
import org.egov.pgr.persistence.repository.AttributeDefinitionRepository;
import org.egov.pgr.persistence.repository.ServiceTypeKeywordRepository;
import org.egov.pgr.persistence.repository.ServiceTypeMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceTypeRepository;
import org.egov.pgr.web.contract.ServiceTypeRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTypeService {

    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    private ServiceTypeMessageQueueRepository serviceTypeMessageQueueRepository;

    private ServiceTypeRepository serviceTypeRepository;

    private ServiceTypeKeywordRepository serviceTypeKeywordRepository;

    private AttributeDefinitionRepository attributeDefinitionRepository;

    private List<ServiceTypeValidator> validators;

    private List<ServiceTypeSearchValidator> searchValidators;

    private List<ServiceTypeCreateValidator> createValidators;

    public ServiceTypeService(ServiceTypeMessageQueueRepository serviceTypeMessageQueueRepository,
                              ServiceTypeRepository serviceTypeRepository, List<ServiceTypeValidator> validators,
                              List<ServiceTypeSearchValidator> searchValidators,
                              List<ServiceTypeCreateValidator> createValidators,
                              ServiceTypeKeywordRepository serviceTypeKeywordRepository,
                              AttributeDefinitionRepository attributeDefinitionRepository) {
        this.serviceTypeMessageQueueRepository = serviceTypeMessageQueueRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.validators = validators;
        this.searchValidators = searchValidators;
        this.createValidators=createValidators;
        this.serviceTypeKeywordRepository = serviceTypeKeywordRepository;
        this.attributeDefinitionRepository = attributeDefinitionRepository;
    }

    //When creating service type to push to kafka
    public void create(ServiceType serviceType, ServiceTypeRequest serviceTypeRequest){
    	createMandatoryFieldValidate(serviceType);
    	createLengthValidate(serviceType);
    	createUniqueCombinationValidate(serviceType);
        serviceTypeMessageQueueRepository.save(serviceTypeRequest, CREATE);
    }

    //Read from consumer and persist to database for create
    public void persistServiceType(ServiceType serviceType){
        serviceTypeRepository.save(serviceType.toDto());
        persistServiceTypeKeywords(serviceType, CREATE);
    }

    //When update service type to push to kafka
    public void update(ServiceType serviceType, ServiceTypeRequest serviceTypeRequest){
        serviceType.setAction(UPDATE);
        validate(serviceType);
        serviceTypeMessageQueueRepository.save(serviceTypeRequest, UPDATE);
    }

    //Read from consumer and persist to database for update
    public void persistForUpdate(ServiceType serviceType){
        serviceTypeRepository.update(serviceType.toDto());
        persistServiceTypeKeywords(serviceType, UPDATE);
    }

    //For search of service type
    public List<ServiceType> search(ServiceTypeSearchCriteria serviceTypeSearchCriteria){
        validateSearch(serviceTypeSearchCriteria);
        return serviceTypeRepository.search(serviceTypeSearchCriteria);
    }

    private void persistServiceTypeKeywords(ServiceType serviceType, String action){

        List<ServiceTypeKeyword> serviceTypeKeywordList =  serviceType.getKeywords().stream()
                                .map(keyword -> new ServiceTypeKeyword(serviceType.getServiceCode(),
                                            serviceType.getTenantId(), keyword))
                                .collect(Collectors.toList());

        if(CREATE.equalsIgnoreCase(action)){
            serviceTypeKeywordList.forEach(serviceTypeKeyword ->
                    serviceTypeKeywordRepository.save(serviceTypeKeyword.toDto(serviceType)));
        }

        if(UPDATE.equalsIgnoreCase(action)){
            serviceTypeKeywordList.forEach(serviceTypeKeyword ->
                    serviceTypeKeywordRepository.update(serviceTypeKeyword.toDto(serviceType)));
        }

    }

    private void validate(ServiceType serviceType){
        validators.stream()
                .filter(validator -> validator.canValidate(serviceType))
                .forEach(v -> v.validate(serviceType));
    }
    
    private void createMandatoryFieldValidate(ServiceType serviceType){
    	createValidators.stream()
                .filter(validator -> validator.canValidate(serviceType))
                .forEach(v -> v.checkMandatoryField(serviceType));
    }
    
    private void createUniqueCombinationValidate(ServiceType serviceType){
    	createValidators.stream()
                .filter(validator -> validator.canValidate(serviceType))
                .forEach(v -> v.validateUniqueCombinations(serviceType));
    }
    
    private void createLengthValidate(ServiceType serviceType){
    	createValidators.stream()
                .filter(validator -> validator.canValidate(serviceType))
                .forEach(v -> v.lengthValidate(serviceType));
    }
    
    private void validateSearch(ServiceTypeSearchCriteria serviceTypeSearchCriteria){
        searchValidators.stream()
                .filter(validator -> validator.canValidate(serviceTypeSearchCriteria))
                .forEach(v -> v.validate(serviceTypeSearchCriteria));
    }
}