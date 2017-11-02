package org.pgr.batch.repository;

import org.egov.common.contract.request.User;
import org.pgr.batch.repository.contract.UserRequest;
import org.pgr.batch.repository.contract.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String getUserByUserNameUrl;

    public UserRepository(RestTemplate restTemplate,
                          @Value("${egov.service.user.service.host}") String userHostUrl,
                          @Value("${egov.services.user.get_user_by_username}") String getUserByUserNameUrl) {
        this.restTemplate = restTemplate;
        this.userHost = userHostUrl;
        this.getUserByUserNameUrl = getUserByUserNameUrl;
    }

    public User getUserByUserName(String userName,String tenantId) {
        UserRequest request = new UserRequest();
        request.setUserName(userName);
        request.setTenantId(tenantId);
        String url = userHost + getUserByUserNameUrl;

        List<User> userResponse = restTemplate.postForObject(url, request, UserResponse.class).getUser();

        return (!userResponse.isEmpty()) ? userResponse.get(0) : User.builder().build();

    }
}
