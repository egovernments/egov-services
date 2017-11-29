package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.OfflineStatusResponse;
import org.egov.works.workorder.web.contract.OfflineStatusSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class OfflineStatusRepository {

    private final RestTemplate restTemplate;

    private final String offlineStatusUrl;

    @Autowired
    public OfflineStatusRepository(final RestTemplate restTemplate,
                                   @Value("${egov.services.egov_works_services.hostname}") final String worksServiceHostname,
                                   @Value("${egov.services.egov_works_services.searchpath}") final String offlineStatusUrl) {

        this.restTemplate = restTemplate;
        this.offlineStatusUrl = worksServiceHostname + offlineStatusUrl;
    }

    public OfflineStatusResponse getOfflineStatus(
            final OfflineStatusSearchContract offlineStatusSearchContract, final String tenantId,
            final RequestInfo requestInfo) {

        StringBuilder url = new StringBuilder();
        url.append(offlineStatusUrl).append(tenantId).append("&")
                .append("detailedEstimateNumber=").append(offlineStatusSearchContract.getDetailedEstimateNumbers().get(0))
                .append("&objectType=").append(offlineStatusSearchContract.getObjectType())
                .append("&statuses=").append(offlineStatusSearchContract.getStatuses().get(0));

        return restTemplate.postForObject(url.toString(), requestInfo, OfflineStatusResponse.class);

    }

    public OfflineStatusResponse getOfflineStatusByLoaNumber(
            final OfflineStatusSearchContract offlineStatusSearchContract, final String tenantId,
            final RequestInfo requestInfo) {

        StringBuilder url = new StringBuilder();
        url.append(offlineStatusUrl).append(tenantId).append("&")
                .append("loaNumbers=").append(offlineStatusSearchContract.getLoaNumbers().get(0))
                .append("&objectType=").append(offlineStatusSearchContract.getObjectType())
                .append("&statuses=").append(offlineStatusSearchContract.getStatuses().get(0));

        return restTemplate.postForObject(url.toString(), requestInfo, OfflineStatusResponse.class);

    }

}
