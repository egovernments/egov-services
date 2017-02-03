package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.EmployeeResponse;
import org.egov.workflow.model.EmployeeServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Value("${egov.services.employee_service.host}")
	private String positionServiceHost;

	@Override
	public List<EmployeeResponse> getByRoleName(String roleName) {
		String url = positionServiceHost + "eis/employee?tenantId=ap.public&roleName={roleName}";
		return getEmployeeServiceResponseByRoleName(url, roleName).getEmployee();
	}

	private EmployeeServiceResponse getEmployeeServiceResponseByRoleName(final String url, String roleName) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, EmployeeServiceResponse.class, roleName);
	}

}
