package org.egov.workflow.domain.service;

import org.egov.workflow.config.PropertiesManager;
import org.egov.workflow.domain.model.User;
import org.egov.workflow.domain.model.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Override
    public User getById(Long id) {
        String url = propertiesManager.getUsersUrl();
        return getUserServiceResponseById(url);
    }

    private User getUserServiceResponseById(final String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, null, UserRes.class).getUser();
    }

}
