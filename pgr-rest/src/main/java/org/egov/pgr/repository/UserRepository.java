package org.egov.pgr.repository;

import org.egov.pgr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRepository {

    private RestTemplate restTemplate;
    private String userServiceUrl;

    @Autowired
    public UserRepository(RestTemplate restTemplate, @Value("${user.service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public User findUser(String token) {
        String url = String.format("%s", token);
        return restTemplate.getForObject(url, User.class);
    }

}
