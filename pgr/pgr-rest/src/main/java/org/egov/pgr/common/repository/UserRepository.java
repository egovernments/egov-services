package org.egov.pgr.common.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.contract.GetUserByIdResponse;
import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.read.web.contract.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.springframework.util.StringUtils.isEmpty;

public class UserRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String getUserDetailsUrl;
    private String getUserByUserNameUrl;

    public UserRepository(RestTemplate restTemplate,
                          String userHostUrl,
                          String getUserDetailsUrl,
                          String getUserByUserNameUrl) {
        this.restTemplate = restTemplate;
        this.userHost = userHostUrl;
        this.getUserDetailsUrl = getUserDetailsUrl;
        this.getUserByUserNameUrl = getUserByUserNameUrl;
    }

    public AuthenticatedUser getUser(String token) {
        if (isEmpty(token)) {
            return AuthenticatedUser.createAnonymousUser();
        }
        return findUser(token);
    }

    public AuthenticatedUser findUser(String token) {
        String url = String.format("%s%s", userHost + getUserDetailsUrl, token);
        return restTemplate.postForObject(url, null, AuthenticatedUser.class);
    }

    public User getUserByUserName(String userName) {
        UserRequest request = new UserRequest();
        request.setUserName(userName);
        String url = userHost + getUserByUserNameUrl;
        return restTemplate.postForObject(url, request, UserResponse.class).getUser().get(0);
    }

    public GetUserByIdResponse findUserById(Long userId) {
        String url = String.format("%s", userHost + getUserByUserNameUrl);
        GetUserByIdRequest userRequest = GetUserByIdRequest.builder().requestInfo(new RequestInfo())
            .id(Collections.singletonList(userId)).build();
        return restTemplate.postForObject(url, userRequest, GetUserByIdResponse.class);
    }
}
