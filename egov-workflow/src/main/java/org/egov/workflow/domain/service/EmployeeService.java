package org.egov.workflow.domain.service;

import java.util.Date;
import java.util.List;

import org.egov.workflow.domain.model.EmployeeResponse;

public interface EmployeeService {

    List<EmployeeResponse> getByRoleName(String roleName);

    EmployeeResponse getUserForPosition(Long posId, Date asOnDate);
}
