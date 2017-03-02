package org.egov.pgr.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EmployeeResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("Employees")
    private List<Employee> employees = new ArrayList<Employee>();

    public EmployeeResponse responseInfo(final ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    public EmployeeResponse employees(final List<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public EmployeeResponse addEmployeeItem(final Employee employeeItem) {
        employees.add(employeeItem);
        return this;
    }
}
