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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class AssetIndexRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AssetIndexRepository.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public void saveAsset(AssetIndex assetIndex) {
		String url = applicationProperties.getIndexerHost() 
					+ applicationProperties.getIndexName()
					+ "/"+assetIndex.getAssetCode();;
		try {
			restTemplate.postForObject(url, assetIndex, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService save ASSEET in elasticsearch : " + assetIndex);
	}
	
	public void updateAsset(AssetIndex assetIndex) {
		String url = applicationProperties.getIndexerHost() 
					+ applicationProperties.getIndexName()
					+ "/"+assetIndex.getAssetCode();
		try {
			restTemplate.postForObject(url, assetIndex,Map.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService save ASSEET in elasticsearch : " + assetIndex);
	}
	
	public Map<Long,Boundary> getlocationsById(Asset asset) {
		
		Location location = asset.getLocationDetails();
		BoundaryResponse boundaryResponse = null;
		List<Long> boundaryList = getBoundaryLists(location);
		Map<Long, Boundary>  BoundaryMap = new HashMap<>();
		
		String url = applicationProperties.getBoundaryServiceHostName()
				   + applicationProperties.getBoundaryServiceSearchPath()
				   + "Boundary.tenantId="+asset.getTenantId()
				   + "&Boundary.id=";		
		for (Long id : boundaryList) {
			URI uri;
			try{
				uri = new URI(url+id);
				LOGGER.info("the url is :: "+url+id + "the uri is "+uri);
				boundaryResponse = restTemplate.getForObject(uri,BoundaryResponse.class);
				Boundary boundary = boundaryResponse.getBoundarys().get(0);
				BoundaryMap.put(boundary.getId(), boundary);
			} catch (URISyntaxException uriSyntaxException) {
				LOGGER.info("exception caught in asset for uri ::"+uriSyntaxException);
				uriSyntaxException.printStackTrace();
			}catch (Exception e) {
				LOGGER.info("exception caught in asset repo boundary api call ::"+e);
				e.printStackTrace();
			}
		}
		return BoundaryMap;
		
	}

	private List<Long> getBoundaryLists(Location location) {
		List<Long> BoundaryLists = new ArrayList<>();
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
