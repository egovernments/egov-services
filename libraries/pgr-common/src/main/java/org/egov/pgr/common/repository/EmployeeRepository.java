package org.egov.pgr.common.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.dto.EmployeeRes;
import org.egov.pgr.common.repository.dto.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeRepository {

    private final RestTemplate restTemplate;
    private final String url;
    private final static String EMPLOYEE_BY_POSITION =
        "/hr-employee/employees/_search?positionId={positionId}&tenantId={tenantId}";

    @Autowired
    public EmployeeRepository(final RestTemplate restTemplate,
                              @Value("${hremployee.host}") final String employeeServiceHostname) {
        this.restTemplate = restTemplate;
        this.url = employeeServiceHostname + EMPLOYEE_BY_POSITION;
    }

    public Employee getEmployeeByPosition(final Long positionId, final String tenantId) {
        RequestInfoWrapper requestBody = buildRequestInfo();
        final EmployeeRes response = restTemplate
            .postForObject(url, requestBody, EmployeeRes.class, positionId, tenantId);
        return response != null ? response.toDomain() : null;
    }

    private RequestInfoWrapper buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return RequestInfoWrapper.builder()
            .RequestInfo(requestInfo)
            .build();
    }
}
