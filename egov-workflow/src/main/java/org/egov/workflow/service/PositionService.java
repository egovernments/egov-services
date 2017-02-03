package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.PositionResponse;

public interface PositionService {

	PositionResponse getById(Long id);
	
	List<PositionResponse> getByEmployeeCode(String code);

}
