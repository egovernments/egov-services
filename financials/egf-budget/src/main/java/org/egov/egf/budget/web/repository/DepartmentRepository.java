package org.egov.egf.budget.web.repository;

import java.util.Date;

import org.egov.common.web.contract.RequestInfo;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.egov.egf.budget.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepartmentRepository {

	private final RestTemplate restTemplate;

	private final String departmentByIdUrl;

	@Autowired
	public DepartmentRepository(final RestTemplate restTemplate,
			@Value("${egov.services.commonmasters.host}") final String departmentServiceHostname,
			@Value("${egov.services.common_masters.department}") final String departmentByIdUrl) {

		this.restTemplate = restTemplate;
		this.departmentByIdUrl = departmentServiceHostname + departmentByIdUrl;
	}

	public DepartmentRes getDepartmentById(final String departmentId, final String tenantId) {
		final RequestInfo requestInfo = new RequestInfo();
		requestInfo.setTs(new Date());
		RequestInfoWrapper wrapper = new RequestInfoWrapper();
		wrapper.setRequestInfo(requestInfo);
		return restTemplate.postForObject(departmentByIdUrl, wrapper, DepartmentRes.class, departmentId, tenantId);
	}
}
