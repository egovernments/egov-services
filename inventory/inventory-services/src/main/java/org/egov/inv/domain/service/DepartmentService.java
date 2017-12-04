package org.egov.inv.domain.service;

import org.egov.common.MdmsRepository;
import org.egov.inv.model.Department;
import org.egov.inv.model.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class DepartmentService {
	
	private static final String MODULE_CODE = "common-masters";

	private static final String DEPARTMENT_MASTER_NAME = "Department";

	@Autowired
	private MdmsRepository mdmsRepository;

	public Department getDepartment(final String tenantId, final String code,
			final RequestInfo requestInfo) {

		JSONArray responseJSONArray;
		final ObjectMapper mapper = new ObjectMapper();

		responseJSONArray = mdmsRepository.getByCriteria(tenantId, MODULE_CODE,
				DEPARTMENT_MASTER_NAME, "code", code, requestInfo);

		if (responseJSONArray != null && responseJSONArray.size() > 0)
			return mapper.convertValue(responseJSONArray.get(0), Department.class);
		else
			throw new CustomException("Department", "Given Department is invalid: " + code);

	}	

}
