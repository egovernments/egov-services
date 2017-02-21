package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.Department;
import org.egov.workflow.domain.model.PositionResponse;

public interface PositionService {

    PositionResponse getById(Long id);

    List<PositionResponse> getByEmployeeCode(String code);

    Department getDepartmentByPosition();

}
