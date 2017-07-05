package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.BoundaryResponse;
import org.egov.lams.contract.CityResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Asset;
import org.egov.lams.model.Boundary;
import org.egov.lams.model.City;
import org.egov.lams.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public City getCity(Long boundaryId) {
		// TODO make api call to boundary repository

		String url = propertiesManager.getBoundaryApiHostUrl() + propertiesManager.getBoundaryApiCitySearchPath() + "?"
				+ "code=" + boundaryId;
		CityResponse cityResponse = null;
		try {
			cityResponse = restTemplate.getForObject(url, CityResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;// FIXME throw custom exception
		}
		return cityResponse.getCity();
	}

	public Map<Long, Boundary> getBoundariesById(Agreement agreement,Asset asset) {

		Location location = asset.getLocationDetails();
		BoundaryResponse boundaryResponse = null;
		List<Long> boundaryList = getBoundaryLists(location);
		Map<Long, Boundary> BoundaryMap = new HashMap<>();

		String url = propertiesManager.getBoundaryApiHostUrl() + propertiesManager.getBoundaryApiSearchPath()
				+ "?Boundary.tenantId=" + agreement.getTenantId() + "&Boundary.id=";
		for (Long id : boundaryList) {

			try {
				boundaryResponse = restTemplate.getForObject(url + id, BoundaryResponse.class);
				Boundary boundary = boundaryResponse.getBoundarys().get(0);
				BoundaryMap.put(boundary.getId(), boundary);
			} catch (HttpClientErrorException e) {
				LOGGER.info("Following exception occurred: " + e.getResponseBodyAsString());
			} catch (Exception e) {
				LOGGER.error("Exception Occurred While Calling demandReason Service : " + e.getMessage());
				throw e;
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
		LOGGER.info("the list of boundaries present "+ BoundaryLists);
		return BoundaryLists;
	}
}
