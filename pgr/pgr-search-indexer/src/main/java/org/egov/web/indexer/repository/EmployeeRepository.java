package org.egov.web.indexer.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.EmployeeResponse;
import org.egov.web.indexer.contract.RequestInfoWrapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

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
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(RequestInfo.builder().apiId("apiId").ver("ver").ts(new Date()).build()).build();
        return restTemplate.postForObject(hrMasterPositionUrl, wrapper, EmployeeResponse.class, positionId,asOnDate.toString("dd/MM/yyyy"),tenantId).getEmployees().get(0);
    }

}
