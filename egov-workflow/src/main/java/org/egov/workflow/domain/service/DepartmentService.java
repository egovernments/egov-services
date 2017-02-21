package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.Department;

public interface DepartmentService {

    Department getDepartmentForUser();

    /* Department getDepartmentForUser(Long userId, Date asOnDate); */

}
