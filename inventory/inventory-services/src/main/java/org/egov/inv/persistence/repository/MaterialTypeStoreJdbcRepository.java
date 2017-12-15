package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialStoreMappingSearch;
import org.egov.inv.model.MaterialTypeStoreMapping;
import org.egov.inv.model.MaterialTypeStoreMappingSearch;
import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.egov.inv.persistence.entity.MaterialTypeStoreMappingEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialTypeStoreJdbcRepository extends JdbcRepository {

    static {
        init(MaterialTypeStoreMappingEntity.class);
    }


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MaterialTypeStoreJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Pagination<MaterialTypeStoreMapping> search(MaterialTypeStoreMappingSearch materialTypeStoreMappingSearch) {
        String searchQuery = "select * from materialtypestoremapping :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (materialTypeStoreMappingSearch.getSortBy() != null && !materialTypeStoreMappingSearch.getSortBy().isEmpty()) {
            validateSortByOrder(materialTypeStoreMappingSearch.getSortBy());
            validateEntityFieldName(materialTypeStoreMappingSearch.getSortBy(), MaterialStoreMappingSearch.class);
        }
        String orderBy = "order by store";
        if (materialTypeStoreMappingSearch.getSortBy() != null && !materialTypeStoreMappingSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + materialTypeStoreMappingSearch.getSortBy();
        }
        if (materialTypeStoreMappingSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialTypeStoreMappingSearch.getIds());
        }
        if (materialTypeStoreMappingSearch.getStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("store = :store");
            paramValues.put("store", materialTypeStoreMappingSearch.getStore());
        }
        if (materialTypeStoreMappingSearch.getMaterialType() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("materialtype = :materialType");
            paramValues.put("materialType", materialTypeStoreMappingSearch.getMaterialType());
        }
        if (materialTypeStoreMappingSearch.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active = :active");
            paramValues.put("active", materialTypeStoreMappingSearch.getActive());
        }
        if (materialTypeStoreMappingSearch.getGlCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("glcode = :glCode");
            paramValues.put("glCode", materialTypeStoreMappingSearch.getGlCode());
        }
        if (materialTypeStoreMappingSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialTypeStoreMappingSearch.getTenantId());
        }
        Pagination<MaterialTypeStoreMapping> page = new Pagination<>();
        if (materialTypeStoreMappingSearch.getPageSize() != null)
            page.setPageSize(materialTypeStoreMappingSearch.getPageSize());
        if (materialTypeStoreMappingSearch.getOffset() != null)
            page.setOffset(materialTypeStoreMappingSearch.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialTypeStoreMapping>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialTypeStoreMappingEntity.class);

        List<MaterialTypeStoreMappingEntity> typeStoreMappingEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<MaterialTypeStoreMapping> materialStoreMappings = typeStoreMappingEntities.stream()
                .map(MaterialTypeStoreMappingEntity::toDomain)
                .collect(Collectors.toList());

        page.setTotalResults(materialStoreMappings.size());

        page.setPagedData(materialStoreMappings);

        return page;
    }

}
