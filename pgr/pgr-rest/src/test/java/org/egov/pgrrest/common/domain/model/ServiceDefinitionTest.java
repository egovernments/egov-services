package org.egov.pgrrest.common.domain.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ServiceDefinitionTest {

    @Test
    public void test_should_return_groups_with_constraints_matching_given_action_and_role() {
        final GroupConstraint constraint1 = GroupConstraint.builder()
            .action(ServiceStatus.CITIZEN_SERVICE_NEW)
            .role("CITIZEN")
            .constraintType(GroupConstraintType.ALL_REQUIRED)
            .build();
        final GroupConstraint constraint2 = GroupConstraint.builder()
            .action(ServiceStatus.CITIZEN_SERVICE_PROGRESS)
            .role("EMPLOYEE")
            .constraintType(GroupConstraintType.AT_LEAST_ONE_REQUIRED)
            .build();
        final GroupConstraint constraint3 = GroupConstraint.builder()
            .action(ServiceStatus.CITIZEN_SERVICE_REJECTED)
            .role("EMPLOYEE")
            .constraintType(GroupConstraintType.AT_LEAST_ONE_REQUIRED)
            .build();
        final GroupDefinition group1 = GroupDefinition.builder()
            .code("groupCode1")
            .constraints(Collections.singletonList(constraint1))
            .build();
        final GroupDefinition group2 = GroupDefinition.builder()
            .code("groupCode2")
            .constraints(Arrays.asList(constraint2, constraint1))
            .build();
        final GroupDefinition group3 = GroupDefinition.builder()
            .code("groupCode3")
            .constraints(Collections.emptyList())
            .build();
        final GroupDefinition group4 = GroupDefinition.builder()
            .code("groupCode4")
            .constraints(Collections.singletonList(constraint3))
            .build();
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .groups(Arrays.asList(group1, group2, group3))
            .build();
        final List<String> roleCodes = Collections.singletonList("CITIZEN");
        final ServiceStatus action = ServiceStatus.CITIZEN_SERVICE_NEW;

        final List<GroupDefinition> actualGroups = serviceDefinition.getGroupsWithConstraints(action, roleCodes);

        assertEquals(2, actualGroups.size());
        assertEquals("groupCode1", actualGroups.get(0).getCode());
        assertEquals("groupCode2", actualGroups.get(1).getCode());
    }

}