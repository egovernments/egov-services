package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.persistence.rowmapper.CityRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static org.egov.tenant.persistence.entity.City.*;

@Repository
public class CityRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String INSERT_QUERY = "INSERT INTO city(id, name, localname, districtcode, districtname, regionname, longitude, latitude, tenantcode, createdby, createddate, lastmodifiedby, lastmodifieddate) " +
        "VALUES (nextval('seq_city'), :name, :localname, :districtcode, :districtname, :regionname, :longitude, :latitude, :tenantcode, :createdby, :createddate, :lastmodifiedby, :lastmodifieddate)";
    private final String SELECT_QUERY = "SELECT * FROM city WHERE tenantcode = :tenantcode";

    public CityRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public City save(City city, String tenantCode) {
        Map<String, Object> parametersMap = new HashMap<String, Object>() {{
            put(NAME, city.getName());
            put(LOCAL_NAME, city.getLocalName());
            put(DISTRICT_CODE, city.getDistrictCode());
            put(DISTRICT_NAME, city.getDistrictName());
            put(REGION_NAME, city.getRegionName());
            put(LONGITUDE, city.getLongitude());
            put(LATITUDE, city.getLatitude());
            put(TENANT_CODE, tenantCode);
            put(CREATED_BY, city.getCreatedBy());
            put(CREATED_DATE, city.getCreatedDate());
            put(LAST_MODIFIED_BY, city.getLastModifiedBy());
            put(LAST_MODIFIED_DATE, city.getLastModifiedDate());
        }};

        namedParameterJdbcTemplate.update(INSERT_QUERY, parametersMap);

        return city;
    }

    public City find(String tenantCode) {
        Map<String, Object> parameterMap = new HashMap<String, Object>() {{
            put(TENANT_CODE, tenantCode);
        }};
        return namedParameterJdbcTemplate.query(SELECT_QUERY, parameterMap, new CityRowMapper()).get(0).toDomain();
    }
}
