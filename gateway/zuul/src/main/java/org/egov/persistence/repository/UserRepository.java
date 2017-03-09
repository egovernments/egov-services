package org.egov.persistence.repository;

import org.egov.model.AuthenticatedUser;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String getUserDetailsUrl;

    public UserRepository(RestTemplate restTemplate, String userHostUrl, String getUserDetailsUrl) {
        this.restTemplate = restTemplate;
        this.userHost = userHostUrl;
        this.getUserDetailsUrl = getUserDetailsUrl;
    }

    public AuthenticatedUser findUser(String token) {
        String url = String.format("%s%s", userHost + getUserDetailsUrl, token);
        return restTemplate.postForObject(url, null, AuthenticatedUser.class);
    }

}
