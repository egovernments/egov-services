package org.egov.workflow.repository;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.UserRes;
import org.springframework.web.client.RestTemplate;

public class UserRepository {

    private RestTemplate restTemplate;
    private PropertiesManager propertiesManager;

    public UserRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserRes findUser(Long id) {
        String url = String.format("%s%s", propertiesManager.getUsersUrl(), id);
        return restTemplate.getForObject(url, UserRes.class);
    }
}
