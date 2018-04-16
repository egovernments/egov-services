package org.egov.eis.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.EmployeeEntityResponse;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
public class EmployeeCreateService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private RestTemplate restTemplate;

    public EmployeeEntityResponse createEmployee(EmployeeRequest employeeRequest) {
        EmployeeEntityResponse employeeEntityResponse;
        try {
            URI url = new URI(propertiesManager.getHrEmployeeServiceHostName()
                    + propertiesManager.getHrEmployeeServiceBasePath()
                    + propertiesManager.getHrEmployeeServiceEmployeeCreatePath());
            log.debug(url.toString());
            employeeEntityResponse = restTemplate.postForObject(url, employeeRequest, EmployeeEntityResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Following exception occurred while accessing Employee _create API : " + e.getMessage());
            return null;
        }
        return employeeEntityResponse;
    }
}
