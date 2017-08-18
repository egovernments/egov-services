package org.egov.asset.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.BoundaryResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetIndex;
import org.egov.asset.model.Boundary;
import org.egov.asset.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetIndexRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public void saveAsset(final AssetIndex assetIndex) {
        final String url = applicationProperties.getIndexerHost() + applicationProperties.getAssetIndexName() + "/"
                + assetIndex.getAssetCode();
        log.info("Asset Save ES Index Push URL :: " + url);
        try {
            restTemplate.postForObject(url, assetIndex, Map.class);
        } catch (final Exception e) {
            log.error(e.toString());
            throw e;
        }
        log.info("ElasticSearchService save ASSET in elasticsearch : " + assetIndex);
    }

    public Map<Long, Boundary> getlocationsById(final Asset asset) {
        final Location location = asset.getLocationDetails();
        final List<Long> boundaryList = getBoundaryLists(location);
        final Map<Long, Boundary> boundaryMap = new HashMap<>();

        final String url = applicationProperties.getBoundaryServiceHostName()
                + applicationProperties.getBoundaryServiceSearchPath() + "?tenantId=" + asset.getTenantId() + "&id=";
        for (final Long id : boundaryList)
            try {
                final URI uri = new URI(url + id);
                log.info("Boundary URL is :: " + uri);
                final BoundaryResponse boundaryResponse = restTemplate.getForObject(uri, BoundaryResponse.class);
                final List<Boundary> boundaries = boundaryResponse.getBoundarys();
                if (boundaries != null && !boundaries.isEmpty()) {
                    final Boundary boundary = boundaries.get(0);
                    boundaryMap.put(boundary.getId(), boundary);
                }
            } catch (final URISyntaxException uriSyntaxException) {
                log.info("exception caught in asset for uri ::" + uriSyntaxException);
                uriSyntaxException.printStackTrace();
            } catch (final Exception e) {
                log.info("exception caught in asset repo boundary api call ::" + e);
                e.printStackTrace();
            }
        log.info("Boundary Map :: " + boundaryMap);
        return boundaryMap;

    }

    private List<Long> getBoundaryLists(final Location location) {
        final List<Long> BoundaryLists = new ArrayList<>();
        if (location.getBlock() != null)
            BoundaryLists.add(location.getBlock());
        if (location.getElectionWard() != null)
            BoundaryLists.add(location.getElectionWard());
        if (location.getLocality() != null)
            BoundaryLists.add(location.getLocality());
        if (location.getRevenueWard() != null)
            BoundaryLists.add(location.getRevenueWard());
        if (location.getZone() != null)
            BoundaryLists.add(location.getZone());
        return BoundaryLists;
    }
}
