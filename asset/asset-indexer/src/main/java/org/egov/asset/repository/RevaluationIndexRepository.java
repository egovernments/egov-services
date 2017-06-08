package org.egov.asset.repository;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.RevaluationIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class RevaluationIndexRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RevaluationIndexRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public void saveOrUpdateAssetRevaluation(final RevaluationIndex revaluationIndex) {
		final String url = applicationProperties.getIndexerHost() + applicationProperties.getIndexName() + "/"
				+ revaluationIndex.getRevaluationId();
		try {
			restTemplate.postForObject(url, revaluationIndex, Map.class);
		} catch (final Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService save Asset Revaluation in elasticsearch : " + revaluationIndex);
	}
}
