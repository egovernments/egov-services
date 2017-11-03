package org.egov.inv.persistence.repository;

import io.swagger.model.MaterialStoreMapping;
import io.swagger.model.MaterialStoreMappingSearchRequest;
import io.swagger.model.Pagination;
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

    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
        String searchQuery = "select * from materialstoremapping :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (materialStoreMappingSearchRequest.getSortBy() != null && !materialStoreMappingSearchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(materialStoreMappingSearchRequest.getSortBy());
            validateEntityFieldName(materialStoreMappingSearchRequest.getSortBy(), MaterialStoreMappingSearchRequest.class);
        }
        String orderBy = "order by store";
        if (materialStoreMappingSearchRequest.getSortBy() != null && !materialStoreMappingSearchRequest.getSortBy().isEmpty()) {
            orderBy = "order by " + materialStoreMappingSearchRequest.getSortBy();
        }
        if (materialStoreMappingSearchRequest.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialStoreMappingSearchRequest.getIds());
        }
        if (materialStoreMappingSearchRequest.getStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("store = :store");
            paramValues.put("store", materialStoreMappingSearchRequest.getStore());
        }
        if (materialStoreMappingSearchRequest.getMaterial() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("material = :material");
            paramValues.put("material", materialStoreMappingSearchRequest.getMaterial());
        }
        if (materialStoreMappingSearchRequest.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active = :active");
            paramValues.put("active", materialStoreMappingSearchRequest.getActive());
        }
        if (materialStoreMappingSearchRequest.getGlCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("glcode = :glCode");
            paramValues.put("glCode", materialStoreMappingSearchRequest.getGlCode());
        }
        if (materialStoreMappingSearchRequest.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialStoreMappingSearchRequest.getTenantId());
        }
        Pagination<MaterialStoreMapping> page = new Pagination<>();
        if (materialStoreMappingSearchRequest.getPageSize() != null)
            page.setPageSize(materialStoreMappingSearchRequest.getPageSize());
        if (materialStoreMappingSearchRequest.getOffset() != null)
            page.setOffset(materialStoreMappingSearchRequest.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
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
