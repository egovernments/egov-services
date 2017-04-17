package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.entity.Tenant;
import org.egov.tenant.persistence.repository.builder.TenantQueryBuilder;
import org.egov.tenant.persistence.rowmapper.TenantRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TenantRepository {

    private JdbcTemplate jdbcTemplate;

    private TenantQueryBuilder tenantQueryBuilder;
    private CityRepository cityRepository;

    public TenantRepository(JdbcTemplate jdbcTemplate, TenantQueryBuilder tenantQueryBuilder, CityRepository cityRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tenantQueryBuilder = tenantQueryBuilder;
        this.cityRepository = cityRepository;
    }


    public List<org.egov.tenant.domain.model.Tenant> find(TenantSearchCriteria tenantSearchCriteria) {
        final String queryStr = tenantQueryBuilder.getSearchQuery(tenantSearchCriteria);
        final List<Tenant> tenants = jdbcTemplate.query(queryStr, new TenantRowMapper());

        return tenants.stream()
                .map(Tenant::toDomain)
                .map(tenant -> {
                    City city = cityRepository.find(tenant.getCode());
                    tenant.setCity(city);
                    return tenant;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public org.egov.tenant.domain.model.Tenant save(final org.egov.tenant.domain.model.Tenant tenant) {
        final String tenantInsertQuery = tenantQueryBuilder.getInsertQuery();

        jdbcTemplate.update(tenantInsertQuery, tenant.getCode(), tenant.getDescription(), tenant.getDomainUrl(), tenant.getLogoId(), tenant.getImageId(), tenant.getType().toString(), 1L, new Date(), 1L, new Date());
        cityRepository.save(tenant.getCity(), tenant.getCode());
        return tenant;
    }
}
