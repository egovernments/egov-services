package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.domain.model.GroupDefinition;
import org.egov.pgrrest.common.persistence.entity.GroupConstraint;
import org.egov.pgrrest.common.persistence.repository.GroupConstraintJpaRepository;
import org.egov.pgrrest.common.persistence.repository.GroupDefinitionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupDefinitionRepository {

    private GroupDefinitionJpaRepository groupDefinitionJpaRepository;
    private GroupConstraintJpaRepository groupConstraintJpaRepository;

    public GroupDefinitionRepository(GroupDefinitionJpaRepository groupDefinitionJpaRepository,
                                     GroupConstraintJpaRepository groupConstraintJpaRepository) {
        this.groupDefinitionJpaRepository = groupDefinitionJpaRepository;
        this.groupConstraintJpaRepository = groupConstraintJpaRepository;
    }

    public List<GroupDefinition> find(String serviceCode, String tenantId) {
        final List<org.egov.pgrrest.common.persistence.entity.GroupDefinition> groupDefinitions =
            groupDefinitionJpaRepository.find(serviceCode, tenantId);
        if (CollectionUtils.isEmpty(groupDefinitions)) {
            return Collections.emptyList();
        }
        return mapToDomainGroupDefinitions(groupDefinitions, serviceCode, tenantId);
    }

    private List<GroupDefinition> mapToDomainGroupDefinitions(
        List<org.egov.pgrrest.common.persistence.entity.GroupDefinition> groupDefinitions,
        String serviceCode,
        String tenantId) {
        final Map<String, List<GroupConstraint>> groupCodeToConstraintsMap = getGroupConstraints(serviceCode, tenantId);

        return groupDefinitions.stream()
            .map(groupDefinition -> {
                final List<GroupConstraint> constraints = groupCodeToConstraintsMap.get(groupDefinition.getCode());
                return toDomain(groupDefinition, constraints);
            })
            .collect(Collectors.toList());
    }

    private Map<String, List<GroupConstraint>> getGroupConstraints(String serviceCode, String tenantId) {
        final List<GroupConstraint> groupConstraints = groupConstraintJpaRepository.find(serviceCode, tenantId);
        return groupConstraints.stream()
            .collect(Collectors.groupingBy(GroupConstraint::getGroupCode));
    }

    private GroupDefinition toDomain(org.egov.pgrrest.common.persistence.entity.GroupDefinition groupDefinition,
                                     List<GroupConstraint> groupConstraints) {
        final List<org.egov.pgrrest.common.domain.model.GroupConstraint> domainGroupConstraints =
            toDomainGroupConstraints(groupConstraints);

        return groupDefinition.toDomain(domainGroupConstraints);
    }

    private List<org.egov.pgrrest.common.domain.model.GroupConstraint> toDomainGroupConstraints(
        List<GroupConstraint> groupConstraints) {
        return groupConstraints.stream()
            .map(GroupConstraint::toDomain)
            .collect(Collectors.toList());
    }
}
