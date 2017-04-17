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
        List<Object> values = new ArrayList<Object>() {{
            add(city.getName());
            add(city.getLocalName());
            add(city.getDistrictCode());
            add(city.getDistrictName());
            add(city.getRegionName());
            add(city.getLongitude());
            add(city.getLatitude());
            add(tenantCode);
            add(1L);
            add(new Date());
            add(1L);
            add(new Date());
        }};

        jdbcTemplate.update(query, values);

        return city;
    }

    public City find(String tenantCode) {
        String selectQuery = cityQueryBuilder.getSelectQuery(tenantCode);
        return this.jdbcTemplate.query(selectQuery, new CityRowMapper()).get(0).toDomain();
    }
}
