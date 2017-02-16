package org.egov.pgr.persistence;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrnRepository {

    private RestTemplate restTemplate;
    private String crnServiceUrl;

    @Autowired
    public CrnRepository(RestTemplate restTemplate,
                         @Value("${crn.service.url}") String crnServiceUrl) {

        this.restTemplate = restTemplate;
        this.crnServiceUrl = crnServiceUrl;
    }

    public ComplaintRegistrationNumber getCrn() {
        return restTemplate.getForObject(crnServiceUrl, ComplaintRegistrationNumber.class);
    }
}
