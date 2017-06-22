package org.egov.web.indexer.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.EmployeeResponse;
import org.egov.web.indexer.contract.RequestInfoWrapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeRepository {

    private final String hrMasterPositionUrl;
    private RestTemplate restTemplate;

    public EmployeeRepository(RestTemplate restTemplate,
                              @Value("${egov.services.hremployee.host}") String employeeServiceHost,
                              @Value("${egov.services.hr_employee.positionhierarchys}") String hrMasterHost) {
        this.restTemplate = restTemplate;
        this.hrMasterPositionUrl = employeeServiceHost + hrMasterHost;
    }

    public Employee fetchEmployeeByPositionId(final Long positionId, final LocalDate asOnDate, final String tenantId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(RequestInfo.builder().build()).build();
        final String date = asOnDate.toString("dd/MM/yyyy");
        final EmployeeResponse employeeResponse = restTemplate
            .postForObject(hrMasterPositionUrl, wrapper, EmployeeResponse.class, positionId, date, tenantId);
        return employeeResponse.getEmployees().get(0);
    }

}
