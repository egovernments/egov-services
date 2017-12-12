package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.ContractorResponse;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WorksMastersRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String searchContractorUrl;
    
    private String searchContractorByCodeUrl;

    public WorksMastersRepository(final RestTemplate restTemplate, @Value("${egov.works.mastershost}") final String worksMastersHost,
                                  @Value("${egov.works.masters.searchcontractorsurl}") final String searchContractorUrl,
                                  @Value("${egov.works.masters.searchcontractorbycodeurl}") final String searchContractorByCodeUrl) {
        this.restTemplate = restTemplate;
        this.searchContractorUrl = worksMastersHost + searchContractorUrl;
        this.searchContractorByCodeUrl = worksMastersHost + searchContractorByCodeUrl;
    }

    public List<Contractor> searchContractors(final String tenantId, final List<String> contractorNames,final RequestInfo requestInfo) {
        String name = contractorNames.get(0);
        return restTemplate.postForObject(searchContractorUrl,
                requestInfo, ContractorResponse.class, tenantId, name).getContractors();
    }
    
    public List<Contractor> searchContractorsByCodes(final String tenantId, final String code,final RequestInfo requestInfo) {
        return restTemplate.postForObject(searchContractorByCodeUrl,
                requestInfo, ContractorResponse.class, tenantId, code).getContractors();
    }
}
