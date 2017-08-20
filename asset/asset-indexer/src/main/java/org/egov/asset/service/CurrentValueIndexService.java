package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.model.AssetCurrentValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrentValueIndexService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    public void indexCurrentValue(final AssetCurrentValueRequest assetCurrentValueRequest) {

        final StringBuilder bulkRequestBody = new StringBuilder();
        final String format = "{ \"index\" : { \"_id\" : \"%s\" } }%n";

        for (final AssetCurrentValue assetCurrentValue : assetCurrentValueRequest.getAssetCurrentValues()) {

            final String actionMetaData = String.format(format, "" + assetCurrentValue.getId());
            bulkRequestBody.append(actionMetaData);
            try {
                bulkRequestBody.append(mapper.writeValueAsString(assetCurrentValue));
            } catch (final JsonProcessingException e) {
                log.info(e.toString());
            }
            bulkRequestBody.append("\n");
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(bulkRequestBody.toString(), headers);
        final String url = applicationProperties.getIndexerHost() + applicationProperties.getCurrentValueIndexUrl();
        restTemplate.postForEntity(url, entity, String.class);
    }
}
