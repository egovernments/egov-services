package org.egov.pgr.repository;

import org.egov.pgr.model.User;
import org.springframework.web.client.RestTemplate;

public class UserRepository {

    private RestTemplate restTemplate;
    private String userServiceUrl;

    public UserRepository(RestTemplate restTemplate, String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public User findUser(String token) {
        String url = String.format("%s%s", userServiceUrl, token);
        return restTemplate.getForObject(url, User.class);
    }

}
