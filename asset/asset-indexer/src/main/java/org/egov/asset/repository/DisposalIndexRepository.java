package org.egov.asset.repository;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DisposalIndexRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public void saveAssetDisposal(final DisposalIndex disposalIndex) {
        final String url = applicationProperties.getIndexerHost() + applicationProperties.getDisposalIndexName() 
        + "/" + disposalIndex.getDisposalId();

        log.info("Save Asset Disposal to ES URL ::" + url);

        try {
            restTemplate.postForObject(url, disposalIndex, Map.class);
        } catch (final Exception e) {
            log.error(e.toString());
            throw e;
        }
        log.info("ElasticSearchService save Asset Disposal in elasticsearch : " + disposalIndex);
    }
}
