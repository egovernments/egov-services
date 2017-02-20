package org.egov.workflow.domain.service;

import java.util.Date;
import java.util.List;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.EmployeeResponse;
import org.egov.workflow.domain.model.EmployeeServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Override
    public List<EmployeeResponse> getByRoleName(String roleName) {
        String url = propertiesManager.getEmployeeByRoleNameUrl();
        return getEmployeeServiceResponseByRoleName(url, roleName).getEmployee();
    }

    private EmployeeServiceResponse getEmployeeServiceResponseByRoleName(final String url, String roleName) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, EmployeeServiceResponse.class, roleName);
    }

    @Override
    public EmployeeResponse getUserForPosition(Long posId, Date asOnDate) {
        String url = propertiesManager.getUserByPositionUrl();
        return getUserForPosition(url, posId, asOnDate);
    }

    private EmployeeResponse getUserForPosition(final String url, Long posId, Date asOnDate) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, null, EmployeeResponse.class);
    }

}
