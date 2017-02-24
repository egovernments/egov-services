package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.Employee;
import org.egov.workflow.domain.model.EmployeeRes;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final RestTemplate restTemplate;
    private final String employeesByUserIdUrl;
    private final String employeesByPositionIdurl;

    private final String employeesByRoleNameurl;

    @Autowired
    public EmployeeServiceImpl(final RestTemplate restTemplate,
            @Value("${egov.services.eis.hostname}") final String eisServiceHostname,
            @Value("${egov.services.eis.employee_by_userid}") final String employeesByUserIdUrl,
            @Value("${egov.services.eis.employee_by_position}") final String employeesByPositionIdurl,
            @Value("${egov.services.eis.employee_by_role}") final String employeesByRoleNameurl) {

        this.restTemplate = restTemplate;
        this.employeesByUserIdUrl = eisServiceHostname + employeesByUserIdUrl;
        this.employeesByPositionIdurl = eisServiceHostname + employeesByPositionIdurl;
        this.employeesByRoleNameurl = eisServiceHostname + employeesByRoleNameurl;
    }

    @Override
    public List<Employee> getByRoleName(final String roleName) {
        final EmployeeRes employeeRes = restTemplate.getForObject(employeesByRoleNameurl, EmployeeRes.class, roleName);
        return employeeRes.getEmployees();
    }

    @Override
    public EmployeeRes getEmployeeForPosition(final Long posId, final LocalDate asOnDate) {
        return restTemplate.getForObject(employeesByPositionIdurl, EmployeeRes.class, posId, asOnDate);
    }

    @Override
    public EmployeeRes getEmployeeForUserId(final Long userId) {
        return restTemplate.getForObject(employeesByUserIdUrl, EmployeeRes.class, userId);
    }
}
