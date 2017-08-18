package org.egov.asset.repository;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetCategoryIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetCategoryIndexRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public void saveAssetCategory(final AssetCategoryIndex assetCategoryIndex) {

        final String url = applicationProperties.getIndexerHost() + applicationProperties.getAssetCategoryIndex() + "/"
                + assetCategoryIndex.getCode();
        log.info("Asset Category Save ES Index Push URL :: " + url);
        try {
            restTemplate.postForObject(url, assetCategoryIndex, Map.class);
        } catch (final Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

}
