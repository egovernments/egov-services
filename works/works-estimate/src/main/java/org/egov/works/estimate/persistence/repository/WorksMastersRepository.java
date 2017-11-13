package org.egov.works.estimate.persistence.repository;

import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.ScheduleOfRate;
import org.egov.works.estimate.web.contract.ScheduleOfRateResponse;
import org.egov.works.estimate.web.contract.ScheduleOfRateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class WorksMastersRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String searchSorUrl;

    public WorksMastersRepository(final RestTemplate restTemplate, @Value("${egov.works.mastershost}")final String worksMastersHost,
                                  @Value("${egov.works.masters.searchsorurl}") final String searchSorUrl) {
        this.restTemplate = restTemplate;
        this.searchSorUrl = worksMastersHost + searchSorUrl;
    }

    public List<ScheduleOfRate> searchScheduleOfRates(final String tenantId, final List<String> sorId,final RequestInfo requestInfo) {
        String ids = String.join(",", sorId);
        return restTemplate.postForObject(searchSorUrl,
                requestInfo, ScheduleOfRateResponse.class, tenantId, ids).getScheduleOfRates();
    }
}
