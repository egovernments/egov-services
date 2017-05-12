package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.AttributeDefinition;
import org.egov.pgrrest.common.entity.ServiceDefinitionKey;
import org.egov.pgrrest.common.entity.ValueDefinition;
import org.egov.pgrrest.common.model.ServiceDefinition;
import org.egov.pgrrest.common.repository.AttributeDefinitionJpaRepository;
import org.egov.pgrrest.common.repository.ServiceDefinitionJpaRepository;
import org.egov.pgrrest.common.repository.ValueDefinitionJpaRepository;
import org.egov.pgrrest.read.domain.exception.ServiceDefinitionNotFoundException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceDefinitionRepository {

    private ServiceDefinitionJpaRepository serviceDefinitionJpaRepository;
    private AttributeDefinitionJpaRepository attributeDefinitionJpaRepository;
    private ValueDefinitionJpaRepository valueDefinitionJpaRepository;

    public ServiceDefinitionRepository(ServiceDefinitionJpaRepository serviceDefinitionJpaRepository,
                                       AttributeDefinitionJpaRepository attributeDefinitionJpaRepository,
                                       ValueDefinitionJpaRepository valueDefinitionJpaRepository) {
        this.serviceDefinitionJpaRepository = serviceDefinitionJpaRepository;
        this.attributeDefinitionJpaRepository = attributeDefinitionJpaRepository;
        this.valueDefinitionJpaRepository = valueDefinitionJpaRepository;
    }

    public ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        final org.egov.pgrrest.common.entity.ServiceDefinition serviceDefinition =
            serviceDefinitionJpaRepository.findOne(new ServiceDefinitionKey(searchCriteria));
        if (serviceDefinition == null) {
            throw new ServiceDefinitionNotFoundException();
        }
        final List<AttributeDefinition> attributeDefinitions = getAttributeDefinitions(serviceDefinition);
        final Map<String, List<ValueDefinition>> valueDefinitions =
            getValueDefinitions(attributeDefinitions, searchCriteria.getTenantId());
        final List<org.egov.pgrrest.common.model.AttributeDefinition> domainAttributes =
            mapToDomainAttributeDefinitions(attributeDefinitions, valueDefinitions);
        return serviceDefinition.toDomain(domainAttributes);
    }

    private List<org.egov.pgrrest.common.model.AttributeDefinition> mapToDomainAttributeDefinitions(
        List<AttributeDefinition> attributeDefinitions, Map<String, List<ValueDefinition>> attributeCodeToValueMap) {
        if (attributeDefinitions == null) {
            return Collections.emptyList();
        }
        return attributeDefinitions.stream()
            .map(attributeDefinition -> mapToDomainAttribute(attributeDefinition, attributeCodeToValueMap))
            .collect(Collectors.toList());
    }

    private org.egov.pgrrest.common.model.AttributeDefinition mapToDomainAttribute(
        AttributeDefinition attributeDefinition, Map<String, List<ValueDefinition>> attributeCodeToValueMap) {
        final List<org.egov.pgrrest.common.model.ValueDefinition> domainValues = getDomainValueDefinitions
            (attributeCodeToValueMap, attributeDefinition.getCode());
        return attributeDefinition.toDomain(domainValues);
    }

    private List<org.egov.pgrrest.common.model.ValueDefinition> getDomainValueDefinitions(
        Map<String, List<ValueDefinition>> attributeCodeToValueDefinitionsMap, String attributeCode) {
        final List<ValueDefinition> entityValues = attributeCodeToValueDefinitionsMap
            .getOrDefault(attributeCode, new ArrayList<>());
        return entityValues.stream()
            .map(ValueDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private Map<String, List<ValueDefinition>> getValueDefinitions(List<AttributeDefinition> attributeDefinitions,
                                                                   String tenantId) {
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final List<ValueDefinition> values = valueDefinitionJpaRepository
            .findByAttributeCodesAndTenantId(attributeCodes, tenantId);
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        return values.stream()
            .collect(Collectors.groupingBy(ValueDefinition::getAttributeCode));
    }

    private List<AttributeDefinition> getAttributeDefinitions(org.egov.pgrrest.common.entity.ServiceDefinition
                                                                  serviceDefinition) {
        final String serviceCode = serviceDefinition.getId().getCode();
        final String tenantId = serviceDefinition.getId().getTenantId();
        return attributeDefinitionJpaRepository.findByServiceCodeAndTenantId(serviceCode, tenantId);
    }
}
