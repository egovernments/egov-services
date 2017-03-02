package org.egov.pgr.persistence.repository;

import java.util.List;

import org.egov.pgr.web.contract.Department;
import org.egov.pgr.web.contract.DepartmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepartmentRepository {

	private final RestTemplate restTemplate;
	private final String departmentByIdUrl;
	private final String allDepartmentsUrl;

	@Autowired
	public DepartmentRepository(final RestTemplate restTemplate,
			@Value("${egov.services.eis.host}") final String eisServiceHostname,
			@Value("${egov.services.eis.department_by_id}") final String departmentByIdUrl,
			@Value("${egov.services.eis.all_departments}") final String allDepartmentsUrl) {

		this.restTemplate = restTemplate;
		this.departmentByIdUrl = eisServiceHostname + departmentByIdUrl;
		this.allDepartmentsUrl = eisServiceHostname + allDepartmentsUrl;
	}

	public List<Department> getById(final Long id) {
		final DepartmentResponse departmentResponse = restTemplate.getForObject(departmentByIdUrl,
				DepartmentResponse.class, id);
		return departmentResponse.getDepartment();
	}

	public List<Department> getAll() {
		final DepartmentResponse departmentResponse = restTemplate.getForObject(allDepartmentsUrl,
				DepartmentResponse.class);
		return departmentResponse.getDepartment();
	}

}
