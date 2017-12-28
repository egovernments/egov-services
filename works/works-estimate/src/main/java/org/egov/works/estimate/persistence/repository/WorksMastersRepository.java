package org.egov.works.estimate.persistence.repository;

import java.util.List;

import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.web.contract.Contractor;
import org.egov.works.estimate.web.contract.ContractorResponse;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.ScheduleOfRate;
import org.egov.works.estimate.web.contract.ScheduleOfRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorksMastersRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String searchSorUrl;

    private String searchSorByCodeUrl;
    
    private String searchContractorByCodeUrl;

    public WorksMastersRepository(final RestTemplate restTemplate, @Value("${egov.works.mastershost}")final String worksMastersHost,
                                  @Value("${egov.works.masters.searchsorurl}") final String searchSorUrl,
                                  @Value("${egov.works.masters.searchsorbycodeurl}") final String searchSorByCodeUrl,
                                  @Value("${egov.works.masters.searchcontractorbycodeurl}") final String searchContractorByCodeUrl) {
        this.restTemplate = restTemplate;
        this.searchSorUrl = worksMastersHost + searchSorUrl;
        this.searchSorByCodeUrl = worksMastersHost + searchSorByCodeUrl;
        this.searchContractorByCodeUrl = worksMastersHost + searchContractorByCodeUrl;
    }

    public List<ScheduleOfRate> searchScheduleOfRatesById(final String tenantId, final List<String> sorId,final RequestInfo requestInfo) {
        String ids = String.join(",", sorId);
        return restTemplate.postForObject(searchSorUrl,
                requestInfo, ScheduleOfRateResponse.class, tenantId, ids).getScheduleOfRates();
    }

    public List<ScheduleOfRate> searchScheduleOfRatesByCodeAndCategory(final String tenantId, final List<String> codes, final List<String> categoryCodes, final RequestInfo requestInfo) {
        String sorCodes = String.join(",", codes);
        String scheduleCategoryCodes = String.join(",", categoryCodes);
        return restTemplate.postForObject(searchSorByCodeUrl,
                requestInfo, ScheduleOfRateResponse.class, tenantId, sorCodes, scheduleCategoryCodes).getScheduleOfRates();
    }
    
    public List<Contractor> searchContractorByCode(final String tenantId, final String contractorCode, final RequestInfo requestInfo) {
        final String statuses = CommonConstants.CONTRACTOR_STATUS;
        return restTemplate.postForObject(searchContractorByCodeUrl,
                requestInfo, ContractorResponse.class, tenantId, contractorCode, statuses ).getContractors();
    }
    
}
