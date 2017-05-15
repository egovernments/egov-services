package org.egov.eis.repository;

import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeRepository {

	private final RestTemplate restTemplate;
	private final String employeeServiceHost;

	public EmployeeRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr_employee.host}") final String employeeServiceHost) {
		this.restTemplate = restTemplate;
		this.employeeServiceHost = employeeServiceHost;
	}

	public EmployeeInfoResponse getEmployees(RequestInfo requestInfo, String tenantId) {
		final String url = employeeServiceHost + "/hr-employee/employees/_search?tenantId=" + tenantId
				+ "&pageSize=500";
		return restTemplate.postForObject(url, requestInfo, EmployeeInfoResponse.class);
	}

}