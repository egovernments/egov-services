package org.egov.eis.service;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeDataSyncService {

    @Autowired
    private PropertiesManager propertiesManager;

    public void createDataSync(UserRequest userRequest) {
        String url = propertiesManager.getHybridDataSyncServiceHostName()
                + propertiesManager.getHybridDataSyncServiceBasePath()
                + propertiesManager.getHybridDataSyncServiceCreatePath();

        System.err.print(url);
        new RestTemplate().postForObject(url, userRequest, UserResponse.class);
    }
}
