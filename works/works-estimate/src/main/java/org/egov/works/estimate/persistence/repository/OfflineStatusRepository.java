package org.egov.works.estimate.persistence.repository;

import org.egov.works.estimate.web.contract.OfflineStatus;
import org.egov.works.estimate.web.contract.OfflineStatusResponse;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.ScheduleOfRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.message.callback.PrivateKeyCallback;
import java.util.List;

@Service
public class OfflineStatusRepository {


    @Autowired
    private RestTemplate restTemplate;

    private String searchOfflineStatusUrl;

    private OfflineStatusRepository(final RestTemplate restTemplate, @Value("${egov.works.services.hostname}") final String worksServiceHost,
                                    @Value("${egov.works.services.offlinestatus.search}") final String searchOfflineStatusUrl) {
        this.restTemplate = restTemplate;
        this.searchOfflineStatusUrl = worksServiceHost + searchOfflineStatusUrl;
    }

    public List<OfflineStatus>  searchOfflineStatus(final String tenantId, final String objectType, final String offlineStatus, final RequestInfo requestInfo) {
        return restTemplate.postForObject(searchOfflineStatusUrl,
                requestInfo, OfflineStatusResponse.class, tenantId, objectType, offlineStatus).getOfflineStatuses();
    }

}