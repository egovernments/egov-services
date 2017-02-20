package org.egov.workflow.domain.service;

import java.util.Date;

import org.egov.workflow.domain.model.Department;

public interface DepartmentService {

    Department getDepartmentForUser(Long userId, Date asOnDate);

}
