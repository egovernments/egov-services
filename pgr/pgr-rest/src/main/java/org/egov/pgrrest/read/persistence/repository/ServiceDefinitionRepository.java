package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.*;
import org.egov.pgrrest.common.persistence.repository.*;
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
    private ConstraintDefinitionJpaRepository constraintDefinitionJpaRepository;

    public ServiceDefinitionRepository(ServiceDefinitionJpaRepository serviceDefinitionJpaRepository,
                                       AttributeDefinitionJpaRepository attributeDefinitionJpaRepository,
                                       ValueDefinitionJpaRepository valueDefinitionJpaRepository,
                                       AttributeRolesDefinitionJpaRepository attributeRolesDefinitionJpaRepository,
                                       AttributeActionsDefinitionJpaRepository attributeActionsDefinitionJpaRepository,
                                       ConstraintDefinitionJpaRepository constraintDefinitionJpaRepository) {
        this.serviceDefinitionJpaRepository = serviceDefinitionJpaRepository;
        this.attributeDefinitionJpaRepository = attributeDefinitionJpaRepository;
        this.valueDefinitionJpaRepository = valueDefinitionJpaRepository;
        this.attributeRolesDefinitionJpaRepository = attributeRolesDefinitionJpaRepository;
        this.attributeActionsDefinitionJpaRepository = attributeActionsDefinitionJpaRepository;
        this.constraintDefinitionJpaRepository = constraintDefinitionJpaRepository;
    }

    public org.egov.pgrrest.common.domain.model.ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        final ServiceDefinition serviceDefinition =
            serviceDefinitionJpaRepository.findOne(new ServiceDefinitionKey(searchCriteria));
        if (serviceDefinition == null) {
            throw new ServiceDefinitionNotFoundException();
        }
        final List<AttributeDefinition> attributes = getAttributeDefinitions(serviceDefinition);
        final Map<String, List<ValueDefinition>> values =
            getValueDefinitions(attributes, searchCriteria.getTenantId());
        final Map<String, List<AttributeRolesDefinition>> roles =
            getAttributeRolesDefinitions(attributes, searchCriteria.getTenantId());
        final Map<String, List<AttributeActionsDefinition>> actions =
            getAttributeActionsDefinitions(attributes, searchCriteria.getTenantId());
        final Map<String, List<ConstraintDefinition>> constraints =
            getConstraints(attributes, searchCriteria.getTenantId());
        final List<org.egov.pgrrest.common.domain.model.AttributeDefinition> domainAttributes =
            mapToDomainAttributeDefinitions(attributes, values, roles, actions, constraints);

        return serviceDefinition.toDomain(domainAttributes);
    }

    private List<org.egov.pgrrest.common.domain.model.AttributeDefinition> mapToDomainAttributeDefinitions(
        List<AttributeDefinition> attributeDefinitions,
        Map<String, List<ValueDefinition>> attributeCodeToValueMap,
        Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap,
        Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap,
        Map<String, List<ConstraintDefinition>> attributeCodeToConstraintsMap) {
        if (attributeDefinitions == null) {
            return Collections.emptyList();
        }
        return attributeDefinitions.stream()
            .map(attributeDefinition -> mapToDomainAttribute(attributeDefinition, attributeCodeToValueMap,
                attributeCodeToAttributeRolesMap, attributeCodeToAttributeActionsMap, attributeCodeToConstraintsMap))
            .collect(Collectors.toList());
    }

    private org.egov.pgrrest.common.domain.model.AttributeDefinition mapToDomainAttribute(
        AttributeDefinition attributeDefinition, Map<String, List<ValueDefinition>> attributeCodeToValueMap,
        Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap,
        Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap, Map<String,
        List<ConstraintDefinition>> attributeCodeToConstraintsMap) {
        final List<org.egov.pgrrest.common.domain.model.ValueDefinition> domainValues =
            getDomainValueDefinitions(attributeCodeToValueMap, attributeDefinition.getCode());
        final List<org.egov.pgrrest.common.domain.model.AttributeRolesDefinition> domainAttributeRoles =
            getDomainAttributeRolesDefinitions(attributeCodeToAttributeRolesMap, attributeDefinition.getCode());
        final List<org.egov.pgrrest.common.domain.model.AttributeActionsDefinition> domainAttributeActions =
            getDomainAttributeActionsDefinitions(attributeCodeToAttributeActionsMap, attributeDefinition.getCode());
        final List<org.egov.pgrrest.common.domain.model.ConstraintDefinition> domainConstraints =
            getDomainConstraintDefinitions(attributeCodeToConstraintsMap, attributeDefinition.getCode());

        return attributeDefinition
            .toDomain(domainValues, domainAttributeRoles, domainAttributeActions, domainConstraints);
    }

    private List<org.egov.pgrrest.common.domain.model.ValueDefinition> getDomainValueDefinitions(
        Map<String, List<ValueDefinition>> attributeCodeToValueDefinitionsMap, String attributeCode) {
        final List<ValueDefinition> entityValues =
            attributeCodeToValueDefinitionsMap.getOrDefault(attributeCode, new ArrayList<>());
        return entityValues.stream()
            .map(ValueDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.common.domain.model.AttributeRolesDefinition> getDomainAttributeRolesDefinitions(
        Map<String, List<AttributeRolesDefinition>> attributeCodeToAttributeRolesMap, String attributeCode) {
        final List<AttributeRolesDefinition> entityAttributeRolesDefinition =
            attributeCodeToAttributeRolesMap.getOrDefault(attributeCode, new ArrayList<>());
        return entityAttributeRolesDefinition.stream()
            .map(AttributeRolesDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.common.domain.model.AttributeActionsDefinition> getDomainAttributeActionsDefinitions(
        Map<String, List<AttributeActionsDefinition>> attributeCodeToAttributeActionsMap, String attributeCode) {
        final List<AttributeActionsDefinition> entityAttributeActionsDefinition =
            attributeCodeToAttributeActionsMap.getOrDefault(attributeCode, new ArrayList<>());
        return entityAttributeActionsDefinition.stream()
            .map(AttributeActionsDefinition::toDomain)
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.common.domain.model.ConstraintDefinition> getDomainConstraintDefinitions(
        Map<String, List<ConstraintDefinition>> attributeCodeToConstraintsMap, String attributeCode) {
        final List<ConstraintDefinition> entityConstraints =
            attributeCodeToConstraintsMap.getOrDefault(attributeCode, new ArrayList<>());
        return entityConstraints.stream()
            .map(ConstraintDefinition::toDomain)
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
        final String serviceCode = attributeDefinitions.get(0).getId().getServiceCode();
        final List<ValueDefinition> values = valueDefinitionJpaRepository
            .find(attributeCodes, tenantId, serviceCode);
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        return values.stream()
            .collect(Collectors.groupingBy(ValueDefinition::getAttributeCode));
    }

    private Map<String, List<AttributeRolesDefinition>> getAttributeRolesDefinitions(List<AttributeDefinition>
                                                                                         attributeDefinitions,
                                                                                     String tenantId) {
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final String serviceCode = attributeDefinitions.get(0).getId().getServiceCode();
        final List<AttributeRolesDefinition> rolesDefinitions = attributeRolesDefinitionJpaRepository
            .find(attributeCodes, tenantId, serviceCode);
        if (CollectionUtils.isEmpty(rolesDefinitions))
            return new HashMap<>();

        return rolesDefinitions.stream()
            .collect(Collectors.groupingBy(AttributeRolesDefinition::getAttributeCode));
    }

    private Map<String, List<AttributeActionsDefinition>> getAttributeActionsDefinitions(List<AttributeDefinition>
                                                                                             attributeDefinitions,
                                                                                         String tenantId) {
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final String serviceCode = attributeDefinitions.get(0).getId().getServiceCode();
        final List<AttributeActionsDefinition> actionsDefinitions = attributeActionsDefinitionJpaRepository
            .find(attributeCodes, tenantId, serviceCode);
        if (CollectionUtils.isEmpty(actionsDefinitions))
            return new HashMap<>();

        return actionsDefinitions.stream()
            .collect(Collectors.groupingBy(AttributeActionsDefinition::getAttributeCode));
    }

    private Map<String, List<ConstraintDefinition>> getConstraints(List<AttributeDefinition> attributeDefinitions,
                                                                   String tenantId) {
        if (CollectionUtils.isEmpty(attributeDefinitions)) {
            return new HashMap<>();
        }
        final List<String> attributeCodes = attributeDefinitions.stream()
            .map(a -> a.getId().getCode())
            .collect(Collectors.toList());
        final String serviceCode = attributeDefinitions.get(0).getId().getServiceCode();
        final List<ConstraintDefinition> actionsDefinitions = constraintDefinitionJpaRepository
            .findBy(serviceCode, attributeCodes, tenantId);
        if (CollectionUtils.isEmpty(actionsDefinitions))
            return new HashMap<>();

        return actionsDefinitions.stream()
            .collect(Collectors.groupingBy(ConstraintDefinition::getAttributeCode));
    }

    private List<AttributeDefinition> getAttributeDefinitions(ServiceDefinition serviceDefinition) {
        final String serviceCode = serviceDefinition.getId().getCode();
        final String tenantId = serviceDefinition.getId().getTenantId();
        return attributeDefinitionJpaRepository.findByServiceCodeAndTenantId(serviceCode, tenantId);
    }
}
