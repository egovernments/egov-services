package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.web.contract.OfflineStatusResponse;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OfflineStatusRepository {

    private RestTemplate restTemplate;

    private String offlineStatusSearchUrl;
    
    public OfflineStatusRepository(final RestTemplate restTemplate,@Value("${egov.services.egov_offlinestatus.hostname}") final String offlineStatusHostname,
                               @Value("${egov.services.egov_offlinestatus.searchpath}") final String offlineStatusSearchUrl) {
        this.restTemplate = restTemplate;
        this.offlineStatusSearchUrl = offlineStatusHostname + offlineStatusSearchUrl;
    }

	public OfflineStatusResponse searchOfflineStatus(String workOrderNumber, String tenantId,
			RequestInfo requestInfo) {
		offlineStatusSearchUrl += "?tenantId=" + tenantId + "&workOrderNumbers=" + workOrderNumber + "&statuses=WORK_COMMENCED";
		return restTemplate.postForObject(offlineStatusSearchUrl, requestInfo, OfflineStatusResponse.class);
	}
}
