package org.egov.boundary.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.boundary.persistence.repository.querybuilder.CityQueryBuilder;
import org.egov.boundary.persistence.repository.rowmapper.CityRowMapper;
import org.egov.boundary.web.contract.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepository {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public CityRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
	this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;	
	}
   
	public City findByCodeAndTenantId(String code, String tenantId){
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("code", code);
		parametersMap.put("tenantId", tenantId);
		City city =null;
		List<City> cities = namedParameterJdbcTemplate.query(CityQueryBuilder.getCityByCodeAndTenantId(), parametersMap,new CityRowMapper());
		if(cities!=null && !cities.isEmpty())
			city = cities.get(0);
		return city;
	}

	public City findByIdAndTenantId(Long id, String tenantId){
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("id", id);
		parametersMap.put("tenantId", tenantId);
		City city =null;
		List<City> cities = namedParameterJdbcTemplate.query(CityQueryBuilder.getCityIdAndTenantId(), parametersMap,new CityRowMapper());
		if(cities!=null && !cities.isEmpty())
			city = cities.get(0);
		return city;
	}
}