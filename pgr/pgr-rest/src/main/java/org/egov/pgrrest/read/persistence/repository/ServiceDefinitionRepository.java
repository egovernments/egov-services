package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ServiceDefinition;
import org.egov.pgrrest.common.persistence.entity.ServiceDefinitionKey;
import org.egov.pgrrest.common.persistence.repository.ServiceDefinitionJpaRepository;
import org.egov.pgrrest.read.domain.exception.ServiceDefinitionNotFoundException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDefinitionRepository {

    private ServiceDefinitionJpaRepository serviceDefinitionJpaRepository;
    private GroupDefinitionRepository groupDefinitionRepository;
    private AttributeDefinitionRepository attributeDefinitionRepository;

    public ServiceDefinitionRepository(ServiceDefinitionJpaRepository serviceDefinitionJpaRepository,
                                       GroupDefinitionRepository groupDefinitionRepository,
                                       AttributeDefinitionRepository attributeDefinitionRepository) {
        this.serviceDefinitionJpaRepository = serviceDefinitionJpaRepository;
        this.groupDefinitionRepository = groupDefinitionRepository;
        this.attributeDefinitionRepository = attributeDefinitionRepository;
    }

    public org.egov.pgrrest.common.domain.model.ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        final ServiceDefinition serviceDefinition =
            serviceDefinitionJpaRepository.findOne(new ServiceDefinitionKey(searchCriteria));
        if (serviceDefinition == null) {
            throw new ServiceDefinitionNotFoundException();
        }
        final List<org.egov.pgrrest.common.domain.model.AttributeDefinition> domainAttributes =
            getDomainAttributeDefinitions(searchCriteria, serviceDefinition);
        List<org.egov.pgrrest.common.domain.model.GroupDefinition> groupDefinitions =
            getGroupDefinitions(searchCriteria, serviceDefinition);

        return serviceDefinition.toDomain(domainAttributes, groupDefinitions);
    }

    private List<org.egov.pgrrest.common.domain.model.GroupDefinition> getGroupDefinitions(
        ServiceDefinitionSearchCriteria searchCriteria, ServiceDefinition serviceDefinition) {
        return groupDefinitionRepository.find(serviceDefinition.getCode(), searchCriteria.getTenantId());
    }

    private List<org.egov.pgrrest.common.domain.model.AttributeDefinition> getDomainAttributeDefinitions(
        ServiceDefinitionSearchCriteria searchCriteria, ServiceDefinition serviceDefinition) {
        return attributeDefinitionRepository.find(serviceDefinition.getCode(), searchCriteria.getTenantId());
    }

}
