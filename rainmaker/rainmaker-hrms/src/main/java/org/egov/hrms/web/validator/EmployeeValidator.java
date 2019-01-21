package org.egov.hrms.web.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.util.CollectionUtils;

public class EmployeeValidator {
	
	public void validateCreateEmployee(EmployeeRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		Map<String, List<String>> mdmsData = new HashMap<>();
		validateEmployee(request, errorMap);
		validateAssignments(request, errorMap);
		validateJurisdiction(request, errorMap);
		validateServiceHistory(request, errorMap);
		validateEducationalDetails(request, errorMap);
		validateDepartmentalTest(request, errorMap);
		
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}

}
