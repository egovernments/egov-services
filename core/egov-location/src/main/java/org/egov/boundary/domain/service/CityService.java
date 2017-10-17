package org.egov.boundary.domain.service;

import org.egov.boundary.persistence.repository.CityRepository;
import org.egov.boundary.web.contract.City;
import org.egov.boundary.web.contract.CityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CityService {

	private final CityRepository cityRepository;

	@Autowired
	public CityService(final CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	public City getCityByCityReq(CityRequest cityRequest) {
		City city = new City();
		if (cityRequest.getCity().getId() != null) {
			city = (cityRepository.findByIdAndTenantId(Long.valueOf(cityRequest.getCity().getId()),cityRequest.getCity().getTenantId()));

		} else if (!StringUtils.isEmpty(cityRequest.getCity().getCode())) {
			city = (cityRepository.findByCodeAndTenantId(cityRequest.getCity().getCode(),cityRequest.getCity().getTenantId()));
		}
		return city;
	}
}
