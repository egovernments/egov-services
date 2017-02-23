package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.City;
import org.springframework.stereotype.Service;

@Service
public class CityRepository {

	public CityRepository() {
	}

	public City fetchCityById(Long id) {
		// TODO : need to call city rest service
		return new City();
	}

}
