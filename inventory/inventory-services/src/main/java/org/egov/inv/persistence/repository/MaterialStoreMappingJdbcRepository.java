package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialStoreMapping;
import org.egov.inv.model.MaterialStoreMappingSearch;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialStoreMappingJdbcRepository extends JdbcRepository {

    static {
        init(MaterialStoreMappingEntity.class);
    }


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MaterialStoreMappingJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearch materialStoreMappingSearch) {
        String searchQuery = "select * from materialstoremapping :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (materialStoreMappingSearch.getSortBy() != null && !materialStoreMappingSearch.getSortBy().isEmpty()) {
            validateSortByOrder(materialStoreMappingSearch.getSortBy());
            validateEntityFieldName(materialStoreMappingSearch.getSortBy(), MaterialStoreMappingSearch.class);
        }
        String orderBy = "order by store";
        if (materialStoreMappingSearch.getSortBy() != null && !materialStoreMappingSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + materialStoreMappingSearch.getSortBy();
        }
        if (materialStoreMappingSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialStoreMappingSearch.getIds());
        }
        if (materialStoreMappingSearch.getStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("store = :store");
            paramValues.put("store", materialStoreMappingSearch.getStore());
        }
        if (materialStoreMappingSearch.getMaterial() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("material = :material");
            paramValues.put("material", materialStoreMappingSearch.getMaterial());
        }
        if (materialStoreMappingSearch.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active = :active");
            paramValues.put("active", materialStoreMappingSearch.getActive());
        }
        if (materialStoreMappingSearch.getGlCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("glcode = :glCode");
            paramValues.put("glCode", materialStoreMappingSearch.getGlCode());
        }
        if (materialStoreMappingSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialStoreMappingSearch.getTenantId());
        }
        Pagination<MaterialStoreMapping> page = new Pagination<>();
        if (materialStoreMappingSearch.getPageSize() != null)
            page.setPageSize(materialStoreMappingSearch.getPageSize());
        if (materialStoreMappingSearch.getOffset() != null)
            page.setOffset(materialStoreMappingSearch.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where isdeleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialStoreMapping>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialStoreMappingEntity.class);

        List<MaterialStoreMappingEntity> materialStoreMappingEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<MaterialStoreMapping> materialStoreMappings = materialStoreMappingEntities.stream()
                .map(MaterialStoreMappingEntity::toDomain)
                .collect(Collectors.toList());

        page.setTotalResults(materialStoreMappings.size());

        page.setPagedData(materialStoreMappings);

        return page;
    }

}
