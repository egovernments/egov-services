package org.egov.collection.repository;

import org.egov.collection.web.contract.Employee;
import org.egov.collection.web.contract.EmployeeResponse;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeRepository {

    private RestTemplate restTemplate;

    private String hrEmployeePositonsUrl;

    public EmployeeRepository(final RestTemplate restTemplate,@Value("${egov.hremployee.hostname}")final String hrEmployeeServiceHost,@Value("${positionforuser.get.uri}")final String hrEmployeePositonsUrl) {
        this.restTemplate = restTemplate;
        this.hrEmployeePositonsUrl = hrEmployeeServiceHost + hrEmployeePositonsUrl;
    }

    public List<Employee> getPositionsForEmployee(final RequestInfo requestInfo,final Long employeeId,final String tenantId) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(hrEmployeePositonsUrl,requestInfoWrapper, EmployeeResponse.class,tenantId,employeeId).getEmployees();
    }
}
