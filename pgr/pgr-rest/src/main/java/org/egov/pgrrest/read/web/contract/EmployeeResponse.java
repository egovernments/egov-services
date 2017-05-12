package org.egov.pgrrest.read.web.contract;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EmployeeResponse {

    private ResponseInfo responseInfo = null;

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
