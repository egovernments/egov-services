package org.egov.egf.bill.domain.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.service.DateFactory;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.bill.web.contract.DepartmentResponse;
import org.egov.egf.bill.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepartmentRepository {

	private final RestTemplate restTemplate;

	private final String departmentByIdUrl;

	private final DateFactory dateFactory;

	@Autowired
	public DepartmentRepository(final RestTemplate restTemplate,
			@Value("${egov.services.commonmasters.host}") final String departmentServiceHostname,
			@Value("${egov.services.common_masters.department}") final String departmentByIdUrl,
			final DateFactory dateFactory) {

		this.restTemplate = restTemplate;
		this.departmentByIdUrl = departmentServiceHostname + departmentByIdUrl;
		this.dateFactory = dateFactory;
	}

	public DepartmentResponse getDepartmentById(final String departmentId, final String tenantId) {
		final RequestInfo requestInfo = new RequestInfo();
		requestInfo.setTs(dateFactory.create());
		final RequestInfoWrapper wrapper = new RequestInfoWrapper();
		wrapper.setRequestInfo(requestInfo);
		return restTemplate.postForObject(departmentByIdUrl, wrapper, DepartmentResponse.class, departmentId, tenantId);
	}

	public Department findById(Department department) {
		return getDepartmentById(department.getId(), department.getTenantId()).getDepartment().get(0);
	}
}
