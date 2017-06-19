package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class EmployeeRes {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Employee")
    private List<Employee> employees;

    public org.egov.pgr.common.model.Employee toDomain() {
        if (CollectionUtils.isEmpty(employees)) {
            return null;
        }
        final Employee firstEmployee = getFirstEmployee();
        return org.egov.pgr.common.model.Employee.builder()
            .email(firstEmployee.getEmailId())
            .mobileNumber(firstEmployee.getMobileNumber())
            .name(firstEmployee.getName())
            .primaryDesignation(getPrimaryDesignation())
            .primaryPosition(getPrimaryPosition())
            .build();
    }

    private Employee getFirstEmployee() {
        return employees.get(0);
    }

    private Long getPrimaryPosition() {
        final List<Assignment> assignments = getAssignments();
        if (CollectionUtils.isEmpty(employees) || CollectionUtils.isEmpty(assignments)) {
            return null;
        }
        return assignments.stream()
            .filter(Assignment::getIsPrimary)
            .findFirst()
            .map(Assignment::getPosition)
            .orElse(null);
    }

    private List<Assignment> getAssignments() {
        return getFirstEmployee().getAssignments();
    }

    private Long getPrimaryDesignation() {
        final List<Assignment> assignments = getAssignments();
        if (CollectionUtils.isEmpty(employees) || CollectionUtils.isEmpty(assignments)) {
            return null;
        }
        return assignments.stream()
            .filter(Assignment::getIsPrimary)
            .findFirst()
            .map(Assignment::getDesignation)
            .orElse(null);
    }

}
