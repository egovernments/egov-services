package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

public class UserRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String userServiceUrl;

    public UserRepository(RestTemplate restTemplate,
                          String userHostUrl,String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userHost = userHostUrl;
        this.userServiceUrl = userServiceUrl;
    }

    public AuthenticatedUser findUser(String token) {
        String url = String.format("%s%s", userHost + userServiceUrl, token);
        System.err.println(url);
        return restTemplate.postForObject(url,null, AuthenticatedUser.class);
    }

}
