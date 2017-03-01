package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.EmployeeResponse;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeRepository {

	private final String employeeServiceHost;
	private RestTemplate restTemplate;

	public EmployeeRepository(RestTemplate restTemplate,
			@Value("${egov.services.eis.host}") String employeeServiceHost) {
		this.restTemplate = restTemplate;
		this.employeeServiceHost = employeeServiceHost;
	}

	public Employee fetchEmployeeByPositionId(final Long id, final LocalDate asOnDate, final String tenantId) {
		String url = this.employeeServiceHost + "eis/employee?positionId={id}&asOnDate={asOnDate}&tenantId={tenantId}";
		return getEmployeeServiceResponse(url, id, asOnDate, tenantId).getEmployees().get(0);
	}

	private EmployeeResponse getEmployeeServiceResponse(final String url, Long id, LocalDate asOnDate,
			String tenantId) {
		return restTemplate.getForObject(url, EmployeeResponse.class, id, asOnDate, tenantId);
	}

}
