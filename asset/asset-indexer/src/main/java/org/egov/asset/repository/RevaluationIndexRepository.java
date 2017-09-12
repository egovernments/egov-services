package org.egov.asset.repository;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.RevaluationIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RevaluationIndexRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public void saveAssetRevaluation(final RevaluationIndex revaluationIndex) {
        final String url = applicationProperties.getIndexerHost() + applicationProperties.getRevaluationIndexName() + "/"
                + revaluationIndex.getRevaluationId();
        log.info("Save Asset Revaluation ES URL ::" + url);
        try {
            restTemplate.postForObject(url, revaluationIndex, Map.class);
        } catch (final Exception e) {
            log.error(e.toString());
            throw e;
        }
        log.info("ElasticSearchService save Asset Revaluation in elasticsearch : " + revaluationIndex);
    }
}
