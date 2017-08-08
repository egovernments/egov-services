package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgr.domain.service.validator.servicetypesearchvalidators.ServiceTypeSearchValidator;
import org.egov.pgr.domain.service.validator.servicetypevalidators.ServiceTypeValidator;
import org.egov.pgr.persistence.repository.ServiceTypeMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceTypeRepository;
import org.egov.pgr.web.contract.ServiceTypeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

    public static final String CREATE = "CREATE";
    private ServiceTypeMessageQueueRepository serviceTypeMessageQueueRepository;

    private ServiceTypeRepository serviceTypeRepository;

    private List<ServiceTypeValidator> validators;

    private List<ServiceTypeSearchValidator> searchValidators;

    public ServiceTypeService(ServiceTypeMessageQueueRepository serviceTypeMessageQueueRepository,
                              ServiceTypeRepository serviceTypeRepository, List<ServiceTypeValidator> validators,
                              List<ServiceTypeSearchValidator> searchValidators) {
        this.serviceTypeMessageQueueRepository = serviceTypeMessageQueueRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.validators = validators;
        this.searchValidators = searchValidators;
    }


    public void create(ServiceType serviceType, ServiceTypeRequest serviceTypeRequest){
        validate(serviceType);
        serviceTypeMessageQueueRepository.save(serviceTypeRequest, CREATE);
    }

    public void persistServiceType(ServiceType serviceType){
        serviceTypeRepository.save(serviceType.toDto());
    }

    public List<ServiceType> search(ServiceTypeSearchCriteria serviceTypeSearchCriteria){
        validateSearch(serviceTypeSearchCriteria);
        return serviceTypeRepository.search(serviceTypeSearchCriteria);
    }

    private void validate(ServiceType serviceType){
        validators.stream()
                .filter(validator -> validator.canValidate(serviceType))
                .forEach(v -> v.validate(serviceType));
    }

    private void validateSearch(ServiceTypeSearchCriteria serviceTypeSearchCriteria){
        searchValidators.stream()
                .filter(validator -> validator.canValidate(serviceTypeSearchCriteria))
                .forEach(v -> v.validate(serviceTypeSearchCriteria));
    }
}