package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.Employee;
import org.egov.workflow.domain.model.EmployeeRes;
import org.egov.workflow.domain.model.EmployeeServiceResponse;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Override
	public List<Employee> getByRoleName(String roleName) {
		String url = propertiesManager.getEmployeeByRoleNameUrl();
		return getEmployeeServiceResponseByRoleName(url, roleName).getEmployee();
	}

	private EmployeeServiceResponse getEmployeeServiceResponseByRoleName(final String url, String roleName) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, EmployeeServiceResponse.class, roleName);
	}

	@Override
	public EmployeeRes getEmployeeForPosition(Long posId, LocalDate asOnDate) {
		String url = propertiesManager.getEmployeeByPositionUrl();
		return getEmployeeForPosition(url, posId, asOnDate);
	}

	private EmployeeRes getEmployeeForPosition(final String url, final Long positionId, LocalDate asOnDate) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, EmployeeRes.class, positionId, asOnDate);
	}

	@Override
	public EmployeeRes getEmployeeForUserId(Long userId) {
		String url = propertiesManager.getEmployeeByUserIdUrl();
		return getEmployeeForUserId(url, userId);
	}

	private EmployeeRes getEmployeeForUserId(final String url, final Long userId) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, EmployeeRes.class, userId);
	}
}
