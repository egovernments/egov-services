package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.City;

public interface CityService {

	City fetchCityById(Long id);
}
