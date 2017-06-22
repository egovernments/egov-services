package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.*;
import org.egov.pgrrest.common.repository.*;
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
    private AttributeRolesDefinitionJpaRepository attributeRolesDefinitionJpaRepository;
    private AttributeActionsDefinitionJpaRepository attributeActionsDefinitionJpaRepository;

    public ServiceDefinitionRepository(ServiceDefinitionJpaRepository serviceDefinitionJpaRepository,
                                       AttributeDefinitionJpaRepository attributeDefinitionJpaRepository,
                                       ValueDefinitionJpaRepository valueDefinitionJpaRepository,
                                       AttributeRolesDefinitionJpaRepository attributeRolesDefinitionJpaRepository,
                                       AttributeActionsDefinitionJpaRepository attributeActionsDefinitionJpaRepository) {
        this.serviceDefinitionJpaRepository = serviceDefinitionJpaRepository;
        this.attributeDefinitionJpaRepository = attributeDefinitionJpaRepository;
        this.valueDefinitionJpaRepository = valueDefinitionJpaRepository;
        this.attributeRolesDefinitionJpaRepository = attributeRolesDefinitionJpaRepository;
        this.attributeActionsDefinitionJpaRepository = attributeActionsDefinitionJpaRepository;
    }

    public org.egov.pgrrest.common.model.ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        final ServiceDefinition serviceDefinition =
            serviceDefinitionJpaRepository.findOne(new ServiceDefinitionKey(searchCriteria));
        if (serviceDefinition == null) {
            throw new ServiceDefinitionNotFoundException();
        }
        final List<AttributeDefinition> attributeDefinitions = getAttributeDefinitions(serviceDefinition);
        final Map<String, List<ValueDefinition>> valueDefinitions =
            getValueDefinitions(attributeDefinitions, searchCriteria.getTenantId());
        final Map<String, List<AttributeRolesDefinition>> attributeRolesDefinitions =
            getAttributeRolesDefinitions(attributeDefinitions,searchCriteria.getTenantId());
        final Map<String, List<AttributeActionsDefinition>> attributeActionsDefinitions =
            getAttributeActionsDefinitions(attributeDefinitions,searchCriteria.getTenantId());
        final List<org.egov.pgrrest.common.model.AttributeDefinition> domainAttributes =
            mapToDomainAttributeDefinitions(attributeDefinitions, valueDefinitions,attributeRolesDefinitions,attributeActionsDefinitions);

        return serviceDefinition.toDomain(domainAttributes);
    }

    private List<org.egov.pgrrest.common.model.AttributeDefinition> mapToDomainAttributeDefinitions(
        List<AttributeDefinition> attributeDefinitions, Map<String, List<ValueDefinition>> attributeCodeToValueMap,
              Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap,
              Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap) {
        if (attributeDefinitions == null) {
            return Collections.emptyList();
        }
        return attributeDefinitions.stream()
            .map(attributeDefinition -> mapToDomainAttribute(attributeDefinition, attributeCodeToValueMap,
                attributeCodeToAttributeRolesMap,attributeCodeToAttributeActionsMap))
            .collect(Collectors.toList());
    }

    private org.egov.pgrrest.common.model.AttributeDefinition mapToDomainAttribute(
        AttributeDefinition attributeDefinition, Map<String, List<ValueDefinition>> attributeCodeToValueMap,
        Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap,
        Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap) {
        final List<org.egov.pgrrest.common.model.ValueDefinition> domainValues = getDomainValueDefinitions
            (attributeCodeToValueMap, attributeDefinition.getCode());
        final List<org.egov.pgrrest.common.model.AttributeRolesDefinition> domainAttributeRoles = getDomainAttributeRolesDefinitions
            (attributeCodeToAttributeRolesMap, attributeDefinition.getCode());
        final List<org.egov.pgrrest.common.model.AttributeActionsDefinition> domainAttributeActions = getDomainAttributeActionsDefinitions
            (attributeCodeToAttributeActionsMap, attributeDefinition.getCode());

        return attributeDefinition.toDomain(domainValues, domainAttributeRoles, domainAttributeActions);
    }

    private List<org.egov.pgrrest.common.model.ValueDefinition> getDomainValueDefinitions(
        Map<String, List<ValueDefinition>> attributeCodeToValueDefinitionsMap, String attributeCode) {
        final List<ValueDefinition> entityValues = attributeCodeToValueDefinitionsMap
            .getOrDefault(attributeCode, new ArrayList<>());
        return entityValues.stream()
            .map(ValueDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.common.model.AttributeRolesDefinition> getDomainAttributeRolesDefinitions(
        Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap, String attributeCode){
        final List<AttributeRolesDefinition> entityAttributeRolesDefinition = attributeCodeToAttributeRolesMap
            .getOrDefault(attributeCode, new ArrayList<>());
        return entityAttributeRolesDefinition.stream()
            .map(AttributeRolesDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.common.model.AttributeActionsDefinition> getDomainAttributeActionsDefinitions(
        Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap, String attributeCode){
        final List<AttributeActionsDefinition> entityAttributeActionsDefinition = attributeCodeToAttributeActionsMap
            .getOrDefault(attributeCode, new ArrayList<>());
        return entityAttributeActionsDefinition.stream()
            .map(AttributeActionsDefinition::toDomain)
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
        final String serviceCode=attributeDefinitions.get(0).getId().getServiceCode();
        final List<ValueDefinition> values = valueDefinitionJpaRepository
            .find(attributeCodes, tenantId ,serviceCode);
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        return values.stream()
            .collect(Collectors.groupingBy(ValueDefinition::getAttributeCode));
    }

    private Map<String, List<AttributeRolesDefinition>> getAttributeRolesDefinitions(List<AttributeDefinition> attributeDefinitions,
                                                                                     String tenantId){
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final String serviceCode=attributeDefinitions.get(0).getId().getServiceCode();
        final List<AttributeRolesDefinition> rolesDefinitions = attributeRolesDefinitionJpaRepository
            .find(attributeCodes, tenantId, serviceCode);
        if(CollectionUtils.isEmpty(rolesDefinitions))
            return Collections.EMPTY_MAP;

        return rolesDefinitions.stream()
            .collect(Collectors.groupingBy(AttributeRolesDefinition::getAttributeCode));
    }

    private Map<String, List<AttributeActionsDefinition>> getAttributeActionsDefinitions(List<AttributeDefinition> attributeDefinitions,
                                                                                       String tenantId){
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final String serviceCode=attributeDefinitions.get(0).getId().getServiceCode();
        final List<AttributeActionsDefinition> actionsDefinitions = attributeActionsDefinitionJpaRepository
            .find(attributeCodes, tenantId, serviceCode);
        if(CollectionUtils.isEmpty(actionsDefinitions))
            return Collections.EMPTY_MAP;

        return actionsDefinitions.stream()
            .collect(Collectors.groupingBy(AttributeActionsDefinition::getAttributeCode));
    }

    private List<AttributeDefinition> getAttributeDefinitions(ServiceDefinition serviceDefinition) {
        final String serviceCode = serviceDefinition.getId().getCode();
        final String tenantId = serviceDefinition.getId().getTenantId();
        return attributeDefinitionJpaRepository.findByServiceCodeAndTenantId(serviceCode, tenantId);
    }
}
