package org.egov.eis.service;

import org.springframework.beans.factory.annotation.Value;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.PositionSync;
import org.egov.eis.web.contract.PositionSyncRequest;
import org.egov.eis.web.contract.PositionSyncResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionDataSyncService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${egov.services.data_sync_position_service.hostname}")
    private String hybridDataSyncServiceHostName;

    @Value("${egov.services.data_sync_position_service.basepath}")
    private String hybridDataSyncServiceBasePath;

    @Value("${egov.services.data_sync_position_service.createpath}")
    private String hybridDataSyncServiceCreatePath;

    public void createDataSync(PositionSyncRequest positionSyncRequest) {
        String url = hybridDataSyncServiceHostName
                + hybridDataSyncServiceBasePath
                + hybridDataSyncServiceCreatePath;

        System.err.print(url);
        restTemplate.postForObject(url, positionSyncRequest, PositionSyncResponse.class);

    }
}
