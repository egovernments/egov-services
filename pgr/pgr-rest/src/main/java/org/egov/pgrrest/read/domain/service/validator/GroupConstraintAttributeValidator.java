package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.GroupConstraintViolationException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class GroupConstraintAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<String> roleCodes = getUserRoles(serviceRequest);
        final List<GroupDefinition> groups = serviceDefinition.getGroupsWithConstraints(action, roleCodes);
        if (CollectionUtils.isEmpty(groups)) {
            return;
        }
        groups.forEach(group -> validateAttributeEntriesForGroup(group, serviceDefinition, serviceRequest, action));
    }

    private List<String> getUserRoles(ServiceRequest serviceRequest) {
        return serviceRequest.getAuthenticatedUser().getRoleCodes();
    }

    private void validateAttributeEntriesForGroup(GroupDefinition group, ServiceDefinition serviceDefinition,
                                                  ServiceRequest serviceRequest, ServiceStatus action) {
        final List<String> attributeCodes = getAttributeCodesForGivenGroup(group, serviceDefinition);
        final Map<String, List<AttributeEntry>> keyToAttributeEntriesMap = getCodeToAttributeEntriesMap(serviceRequest);
        final List<GroupConstraint> constraints = getMatchingGroupConstraints(group, serviceRequest, action);

        final boolean isUnSatisfiedConstraintPresent = constraints.stream()
            .anyMatch(constraint -> !validateConstraint(constraint, attributeCodes, keyToAttributeEntriesMap));

        if (isUnSatisfiedConstraintPresent) {
            throw new GroupConstraintViolationException(group.getCode());
        }
    }

    private boolean validateConstraint(GroupConstraint constraint, List<String> attributeCodes,
                                       Map<String, List<AttributeEntry>> keyToAttributeEntriesMap) {
        if (constraint.getConstraintType() == GroupConstraintType.ALL_REQUIRED) {
            return validateAllAttributeEntriesPresent(attributeCodes, keyToAttributeEntriesMap);
        } else if (constraint.getConstraintType() == GroupConstraintType.AT_LEAST_ONE_REQUIRED) {
            return validateAtLeastOneAttributeEntryPresent(attributeCodes, keyToAttributeEntriesMap);
        }
        return false;
    }

    private List<GroupConstraint> getMatchingGroupConstraints(GroupDefinition group, ServiceRequest serviceRequest,
                                                              ServiceStatus action) {
        final List<String> userRoles = getUserRoles(serviceRequest);
        return group.getMatchingConstraints(action, userRoles);
    }

    private Map<String, List<AttributeEntry>> getCodeToAttributeEntriesMap(ServiceRequest serviceRequest) {
        return serviceRequest.getAttributeEntries()
            .stream().collect(Collectors.groupingBy(AttributeEntry::getKey));
    }

    private List<String> getAttributeCodesForGivenGroup(GroupDefinition group, ServiceDefinition serviceDefinition) {
        final List<AttributeDefinition> attributes = serviceDefinition.getAttributesWithGroupCode(group.getCode());
        return attributes.stream()
            .map(AttributeDefinition::getCode)
            .collect(Collectors.toList());
    }

    private boolean validateAtLeastOneAttributeEntryPresent(
        List<String> attributeCodes, Map<String, List<AttributeEntry>> keyToAttributeEntriesMap) {
        return attributeCodes.stream().anyMatch(code -> {
            final List<AttributeEntry> matchingAttributeEntries =
                keyToAttributeEntriesMap.getOrDefault(code, Collections.emptyList());
            return matchingAttributeEntries.stream().anyMatch(entry -> isNotEmpty(entry.getCode()));
        });
    }

    private boolean validateAllAttributeEntriesPresent(List<String> attributeCodes,
                                                       Map<String, List<AttributeEntry>> keyToAttributeEntriesMap) {
        return attributeCodes.stream().anyMatch(code -> {
            final List<AttributeEntry> matchingAttributeEntries = keyToAttributeEntriesMap.get(code);
            if (matchingAttributeEntries == null) {
                return false;
            }
            return matchingAttributeEntries.stream().anyMatch(entry -> isNotEmpty(entry.getCode()));
        });
    }
}
