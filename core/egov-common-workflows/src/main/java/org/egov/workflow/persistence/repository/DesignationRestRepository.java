package org.egov.workflow.persistence.repository;

import java.util.Date;

import org.egov.workflow.web.contract.DesignationResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DesignationRestRepository {

    private final RestTemplate restTemplate;

    private final String designationByNameUrl;

    @Autowired
    public DesignationRestRepository(final RestTemplate restTemplate,
                                    @Value("${egov.services.hr-masters.host}") final String designationServiceHostname,
                                    @Value("${egov.services.hr_masters.designation}") final String designationByNameUrl) {

        this.restTemplate = restTemplate;
        this.designationByNameUrl = designationServiceHostname + designationByNameUrl;
    }

    public DesignationResponse getDesignationByName(final String designationName, final String tenantId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().apiId("apiID").ver("ver").ts(new Date()).msgId("msgID").build()).build();
       return restTemplate.postForObject(designationByNameUrl,wrapper,DesignationResponse.class, designationName, tenantId);
    }
}
