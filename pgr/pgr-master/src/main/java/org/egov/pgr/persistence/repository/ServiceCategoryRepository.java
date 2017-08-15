package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.dto.ServiceCategory;
import org.egov.pgr.persistence.querybuilder.ServiceCategoryQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ServiceCategoryRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ServiceCategoryQueryBuilder serviceCategoryQueryBuilder;

    public ServiceCategoryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     ServiceCategoryQueryBuilder serviceCategoryQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.serviceCategoryQueryBuilder = serviceCategoryQueryBuilder;
    }

    public List<ServiceCategory> findByTenantIdAndId(String tenantId, Integer id){
        return namedParameterJdbcTemplate.query(serviceCategoryQueryBuilder.getCategoryByTenantIdAndId(),
                getSearchNamedQuery(tenantId, id), new BeanPropertyRowMapper<>(ServiceCategory.class));
    }

    private HashMap<String, Object> getSearchNamedQuery(String tenantId, Integer id) {
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("id", id);
        parametersMap.put("tenantid", tenantId);

        return parametersMap;
    }

}
