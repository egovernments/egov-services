package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.persistence.rowmapper.CityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CityRepository {

    private JdbcTemplate jdbcTemplate;
    private final String INSERT_QUERY = "INSERT INTO city(id, name, localname, districtcode, districtname, regionname, longitude, latitude, tenantcode, createdby, createddate, lastmodifiedby, lastmodifieddate) " +
        "VALUES (nextval('seq_city'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_QUERY = "SELECT * FROM city WHERE tenantcode = '%s'";

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public City save(City city, String tenantCode) {
        jdbcTemplate.update(INSERT_QUERY, city.getName(), city.getLocalName(), city.getDistrictCode(), city.getDistrictName(), city.getRegionName(), city.getLongitude(), city.getLatitude(), tenantCode, 1L, new Date(), 1L, new Date());

        return city;
    }

    public City find(String tenantCode) {
        return this.jdbcTemplate.query(String.format(SELECT_QUERY, tenantCode), new CityRowMapper()).get(0).toDomain();
    }
}
