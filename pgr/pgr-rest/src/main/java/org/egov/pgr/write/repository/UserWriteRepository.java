package org.egov.pgr.write.repository;

import org.egov.pgr.write.contracts.grievance.RequestInfo;
import org.egov.pgr.write.contracts.user.GetUserByIdRequest;
import org.egov.pgr.write.contracts.user.GetUserByIdResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Repository
public class UserWriteRepository {

    private RestTemplate restTemplate;
    private String userHost;
    private String userServiceUrl;

    public UserWriteRepository(RestTemplate restTemplate,
                               @Value("${user.service.host}") final String userHostUrl,
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
