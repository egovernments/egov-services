package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.City;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService{

	@Override
	public City fetchCityById(Long id) {
		// TODO : need to call city rest service
		return new City();
	}

}
