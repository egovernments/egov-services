package org.egov.pg.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.pg.config.AppProperties;
import org.egov.pg.models.ReceiptReq;
import org.egov.pg.models.ReceiptRes;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@Slf4j
public class CollectionsRepository {


    private AppProperties appProperties;
    private RestTemplate restTemplate;

    @Autowired
    CollectionsRepository(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }


    public ReceiptRes generateReceipt(ReceiptReq receiptReq) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(appProperties.getCollectionServiceHost())
                .path(appProperties.getCollectionServicePath())
                .build()
                .toUriString();


        try {
            return restTemplate.postForObject(uri, receiptReq, ReceiptRes.class);
        } catch (HttpClientErrorException e) {
            log.error("Error occurred while persisting receipt", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unknown error occurred while generating receipt.", e);
            throw new CustomException("COLLECTION_SERVICE_ERROR", "Unknown error occurred while generating receipt");
        }
    }


}
