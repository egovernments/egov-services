package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.entity.Tenant;
import org.egov.tenant.persistence.rowmapper.TenantRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.egov.tenant.persistence.entity.Tenant.*;

@Repository
public class TenantRepository {
    private static final String TENANT_BASE_QUERY = "SELECT distinct id, code, description, domainurl, logoid, imageid, type, createdby, createddate, lastmodifiedby, lastmodifieddate from tenant WHERE code in (:code) ORDER BY ID";

    private static final String INSERT_QUERY = "INSERT INTO tenant (id, code, description, domainurl, logoid, imageid, type, createdby, createddate, lastmodifiedby, lastmodifieddate) " +
        "VALUES (nextval('seq_tenant'), :code, :description, :domainurl, :logoid, :imageid, :type, :createdby, :createddate, :lastmodifiedby, :lastmodifieddate)";

    private static final String COUNT_WITH_TENANT_CODE_QUERY = "SELECT COUNT(id) as count FROM tenant WHERE code = :code";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private CityRepository cityRepository;

    public TenantRepository(CityRepository cityRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.cityRepository = cityRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public List<org.egov.tenant.domain.model.Tenant> find(TenantSearchCriteria tenantSearchCriteria) {
        final Map<String, Object> parametersMap = new HashMap<String, Object>() {{
            put("code", tenantSearchCriteria.getTenantCodes());
        }};

        final List<Tenant> tenants = namedParameterJdbcTemplate.query(TENANT_BASE_QUERY, parametersMap, new TenantRowMapper());

        return tenants.stream()
            .map(Tenant::toDomain)
            .map(this::getCityForTenant)
            .collect(Collectors.toList());
    }

    private org.egov.tenant.domain.model.Tenant getCityForTenant(org.egov.tenant.domain.model.Tenant tenant) {
        City city = cityRepository.find(tenant.getCode());
        tenant.setCity(city);
        return tenant;
    }

    @Transactional
    public org.egov.tenant.domain.model.Tenant save(final org.egov.tenant.domain.model.Tenant tenant) {
        final Map<String, Object> parametersMap = new HashMap<String, Object>() {{
            put(CODE, tenant.getCode());
            put(DESCRIPTION, tenant.getDescription());
            put(DOMAIN_URL, tenant.getDomainUrl());
            put(LOGO_ID, tenant.getLogoId());
            put(IMAGE_ID, tenant.getImageId());
            put(TYPE, tenant.getType().toString());
            put(CREATED_BY, 1L);
            put(CREATED_DATE, new Date());
            put(LAST_MODIFIED_BY, 1L);
            put(LAST_MODIFIED_DATE, new Date());
        }};

        namedParameterJdbcTemplate.update(INSERT_QUERY, parametersMap);
        cityRepository.save(tenant.getCity(), tenant.getCode());
        return tenant;
    }

    public Long isTenantPresent(String tenantCode) {

        final Map<String, Object> parameterMap = new HashMap<String, Object>() {{
            put(CODE, tenantCode);
        }};

        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(COUNT_WITH_TENANT_CODE_QUERY, parameterMap);
        sqlRowSet.next();
        return sqlRowSet.getLong("count");
    }
}
