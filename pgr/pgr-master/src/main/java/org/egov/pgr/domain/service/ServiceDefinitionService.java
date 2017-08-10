package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ServiceDefinition;
import org.egov.pgr.persistence.dto.AttributeDefinition;
import org.egov.pgr.persistence.dto.ValueDefinition;
import org.egov.pgr.persistence.repository.AttributeDefinitionRepository;
import org.egov.pgr.persistence.repository.ServiceDefinitionMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceDefinitionRepository;
import org.egov.pgr.persistence.repository.ValueDefinitionRepository;
import org.egov.pgr.web.contract.ServiceDefinitionRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceDefinitionService {

    private static final String CREATE = "CREATE";
    private ServiceDefinitionMessageQueueRepository serviceDefinitionMessageQueueRepository;
    private ServiceDefinitionRepository serviceDefinitionRepository;
    private AttributeDefinitionRepository attributeDefinitionRepository;
    private ValueDefinitionRepository valueDefinitionRepository;

    public ServiceDefinitionService(ServiceDefinitionMessageQueueRepository serviceDefinitionMessageQueueRepository,
                                    ServiceDefinitionRepository serviceDefinitionRepository,
                                    AttributeDefinitionRepository attributeDefinitionRepository,
                                    ValueDefinitionRepository valueDefinitionRepository) {
        this.serviceDefinitionMessageQueueRepository = serviceDefinitionMessageQueueRepository;
        this.serviceDefinitionRepository = serviceDefinitionRepository;
        this.attributeDefinitionRepository = attributeDefinitionRepository;
        this.valueDefinitionRepository = valueDefinitionRepository;
    }

    public void create(ServiceDefinition serviceDefinition, ServiceDefinitionRequest request){
        serviceDefinitionMessageQueueRepository.save(request, CREATE);
    }

    public void persist(ServiceDefinition serviceDefinition){
        serviceDefinitionRepository.save(serviceDefinition.toDto());
        persistServiceTypeAttributes(serviceDefinition);
    }

    private void persistServiceTypeAttributes(ServiceDefinition serviceDefinition){

        List<AttributeDefinition> attributeDefinitionList = serviceDefinition.getAttributes().stream()
                .map(attributeDefinition -> attributeDefinition.toDto(serviceDefinition))
                .collect(Collectors.toList());

        attributeDefinitionList.forEach(this::persistAttribute);
    }

    private void persistAttribute(AttributeDefinition attributeDefinition){
        attributeDefinitionRepository.save(attributeDefinition);

        attributeDefinition.getValueDefinitions().forEach(this::persistValueDefinition);
    }

    private void persistValueDefinition(ValueDefinition valueDefinition){
        valueDefinitionRepository.save(valueDefinition);
    }
}