package org.egov.eis.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.service.helper.EmployeeSearchURLHelper;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.egov.eis.web.contract.LeaveSearchRequest;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeRepository {

    private final RestTemplate restTemplate;
    private final String employeeServiceHost;
    private final String employeeServiceUrl;

    @Autowired
    private EmployeeSearchURLHelper employeeSearchURLHelper;

    public EmployeeRepository(final RestTemplate restTemplate,
                              @Value("${egov.services.hr_employee.host}") final String employeeServiceHost, @Value("${egov.services.hr_employee_service.searchpath}") final String employeeServiceUrl) {
        this.restTemplate = restTemplate;
        this.employeeServiceHost = employeeServiceHost;
        this.employeeServiceUrl = employeeServiceUrl;

    }

    public EmployeeInfoResponse getEmployeesForLeaveRequest(final LeaveSearchRequest leaveSearchRequest, RequestInfo requestInfo) {
        String url = String.format("%s%s", employeeServiceHost, employeeServiceUrl);
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        final String searchUrl = employeeSearchURLHelper.searchURL(leaveSearchRequest, url);
        return restTemplate.postForObject(searchUrl, requestInfoWrapper, EmployeeInfoResponse.class);
    }

    public EmployeeInfoResponse getEmployees(RequestInfo requestInfo, String tenantId) {
        String url = String.format("%s%s", employeeServiceHost, employeeServiceUrl) + "?tenantId=" + tenantId + "&pageSize=500";
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(url, requestInfoWrapper, EmployeeInfoResponse.class);
    }

    public List<EmployeeInfo> getEmployeeById(RequestInfo requestInfo, String tenantId, Long employeeId) {
        String url = String.format("%s%s", employeeServiceHost, employeeServiceUrl) + "?tenantId=" + tenantId + "&id=" + employeeId;
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        EmployeeInfoResponse employeeInfoResponse = restTemplate.postForObject(url, requestInfoWrapper, EmployeeInfoResponse.class);
        return employeeInfoResponse.getEmployees();
    }

}