package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.GetUserByIdRequest;
import org.egov.workflow.web.contract.GetUserByIdResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Repository
public class UserRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String userServiceUrl;

    public UserRepository(RestTemplate restTemplate,
                          @Value("${user.service.url}") final String userHostUrl,
                          @Value("${egov.services.user_by_id}") final String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userHost = userHostUrl;
        this.userServiceUrl = userServiceUrl;
    }

    public GetUserByIdResponse findUserById(Long userId) {
        String url = String.format("%s%s", userHost, userServiceUrl);
        GetUserByIdRequest userRequest = GetUserByIdRequest.builder()
                .requestInfo(new RequestInfo())
                .id(Collections.singletonList(userId))
                .build();

        return restTemplate.postForObject(url,userRequest, GetUserByIdResponse.class);
    }

}
