package org.egov.asset.repository;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class DisposalIndexRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(DisposalIndexRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public void saveOrUpdateAssetDisposal(final DisposalIndex disposalIndex) {
		final String url = applicationProperties.getIndexerHost() + applicationProperties.getIndexName() + "/"
				+ disposalIndex.getDisposalId();
		try {
			restTemplate.postForObject(url, disposalIndex, Map.class);
		} catch (final Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService save Asset Disposal in elasticsearch : " + disposalIndex);
	}
}
