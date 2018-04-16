package org.egov.eis.service;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.EmployeeSyncRequest;
import org.egov.eis.web.contract.EmployeeSyncResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeDataSyncService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    public void createDataSync(EmployeeSyncRequest employeeSyncRequest) {
        String url = propertiesManager.getHybridDataSyncServiceHostName()
                + propertiesManager.getHybridDataSyncServiceBasePath()
                + propertiesManager.getHybridDataSyncServiceCreatePath();

        System.err.print(url);
        restTemplate.postForObject(url, employeeSyncRequest, EmployeeSyncResponse.class);
    }
}
