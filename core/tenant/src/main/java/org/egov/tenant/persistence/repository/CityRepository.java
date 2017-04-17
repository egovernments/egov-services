package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.persistence.repository.builder.CityQueryBuilder;
import org.egov.tenant.persistence.rowmapper.CityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CityRepository {

    private JdbcTemplate jdbcTemplate;
    private CityQueryBuilder cityQueryBuilder;

    public CityRepository(JdbcTemplate jdbcTemplate, CityQueryBuilder cityQueryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.cityQueryBuilder = cityQueryBuilder;
    }

    public City save(City city, String tenantCode) {
        String query = cityQueryBuilder.getInsertQuery();
        jdbcTemplate.update(query, city.getName(), city.getLocalName(), city.getDistrictCode(), city.getDistrictName(), city.getRegionName(), city.getLongitude(), city.getLatitude(), tenantCode, 1L, new Date(), 1L, new Date());

        return city;
    }

    public City find(String tenantCode) {
        String selectQuery = cityQueryBuilder.getSelectQuery(tenantCode);
        return this.jdbcTemplate.query(selectQuery, new CityRowMapper()).get(0).toDomain();
    }
}
