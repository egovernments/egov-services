package org.egov.pg.repository;

import org.egov.pg.config.AppProperties;
import org.egov.pg.models.IdGenerationRequest;
import org.egov.pg.models.IdGenerationResponse;
import org.egov.pg.models.IdRequest;
import org.egov.pg.web.models.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IdGenRepository {

    private AppProperties appProperties;
    private RestTemplate restTemplate;

    @Autowired
    IdGenRepository(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }


    public IdGenerationResponse getId(RequestInfo requestInfo, String tenantId, String name, String format, int count) {

        List<IdRequest> reqList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reqList.add(new IdRequest(name, tenantId, format));
        }
        IdGenerationRequest req = new IdGenerationRequest(requestInfo, reqList);
        try {
            return restTemplate.postForObject(appProperties.getIdGenHost() + appProperties
                            .getIdGenPath(), req,
                    IdGenerationResponse.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put(e.getCause().getClass().getName(), e.getMessage());
            throw new CustomException(map);
        }
    }


}
