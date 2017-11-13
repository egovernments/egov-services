package org.egov.inv.persistense.repository;

import io.swagger.model.Material;
import io.swagger.model.MaterialSearchRequest;
import io.swagger.model.Pagination;
import org.egov.inv.persistence.repository.JdbcRepository;
import org.egov.inv.persistense.repository.entity.MaterialEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialJdbcRepository extends JdbcRepository {

    static {
        init(MaterialEntity.class);
    }

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MaterialJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Pagination<Material> search(MaterialSearchRequest materialSearchRequest) {
        String searchQuery = "select * from material :condition  :orderby ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (!isEmpty(materialSearchRequest.getSortBy())) {
            validateSortByOrder(materialSearchRequest.getSortBy());
            validateEntityFieldName(materialSearchRequest.getSortBy(), MaterialSearchRequest.class);
        }

        String orderBy = "order by code";
        if (!isEmpty(materialSearchRequest.getSortBy())) {
            orderBy = "order by " + materialSearchRequest.getSortBy();
        }

        if (materialSearchRequest.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantid =:tenantId");
            paramValues.put("tenantId", materialSearchRequest.getTenantId());
        }

        if (materialSearchRequest.getIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id in (:ids)");
            paramValues.put("ids", materialSearchRequest.getIds());
        }

        if (materialSearchRequest.getCode() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("code =:code");
            paramValues.put("code", materialSearchRequest.getCode());
        }

        if (materialSearchRequest.getStore() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("code in (select material from materialstoremapping where store=:storeCode and tenantid=:tenantId)");
            paramValues.put("storeCode", materialSearchRequest.getStore());
            paramValues.put("tenantid", materialSearchRequest.getTenantId());

        }

        if (materialSearchRequest.getName() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("name =:name");
            paramValues.put("name", materialSearchRequest.getName());
        }

        
        if (materialSearchRequest.getDescription() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("description =:description");
            paramValues.put("description", materialSearchRequest.getDescription());
        }

        if (!isEmpty(materialSearchRequest.getOldCode())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("oldcode =:oldCode");
            paramValues.put("oldCode", materialSearchRequest.getOldCode());
        }

        if (null != materialSearchRequest.getMaterialType()) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("materialtype =:materialType");
            paramValues.put("materialType", materialSearchRequest.getMaterialType());
        }

        if (!isEmpty(materialSearchRequest.getInventoryType())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("inventorytype =:inventoryType");
            paramValues.put("inventoryType", materialSearchRequest.getInventoryType());
        }

        if (!isEmpty(materialSearchRequest.getStatus())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("status =:status");
            paramValues.put("status", materialSearchRequest.getStatus());
        }

        if (!isEmpty(materialSearchRequest.getMaterialClass())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("materialclass =:materialClass");
            paramValues.put("materialClass", materialSearchRequest.getMaterialClass());
        }


        if (!isEmpty(materialSearchRequest.getMaterialControlType())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("materialcontroltype =:materialControlType");
            paramValues.put("materialControlType", materialSearchRequest.getMaterialControlType());
        }


        if (!isEmpty(materialSearchRequest.getModel())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("model =:model");
            paramValues.put("model", materialSearchRequest.getModel());
        }


        if (!isEmpty(materialSearchRequest.getManufacturePartNo())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("manufacturepartno =:manufacturePartNo");
            paramValues.put("manufacturePartNo", materialSearchRequest.getManufacturePartNo());
        }


        Pagination<Material> page = new Pagination<>();
        if (materialSearchRequest.getOffSet() != null) {
            page.setOffset(materialSearchRequest.getOffSet());
        }
        if (materialSearchRequest.getPageSize() != null) {
            page.setPageSize(materialSearchRequest.getPageSize());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<Material>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialEntity.class);

        List<MaterialEntity> materialEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);


        List<Material> materialList = materialEntities.stream().map(MaterialEntity::toDomain)
                .collect(Collectors.toList());

        page.setTotalResults(materialList.size());

        page.setPagedData(materialList);

        return page;
    }


    public Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {
        String countQuery = "select count(*) from (" + searchQuery + ") as x";
        Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
        Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
        page.setTotalPages(totalpages);
        page.setCurrentPage(page.getOffset());
        return page;
    }

    public void validateSortByOrder(final String sortBy) {
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        for (String s : sortByList) {
            if (s.contains(" ")
                    && (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {

                throw new CustomException(s.split(" ")[0],
                        "Please send the proper sortBy order for the field " + s.split(" ")[0]);
            }
        }

    }

    public void validateEntityFieldName(String sortBy, final Class<?> object) {
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        Boolean isFieldExist = Boolean.FALSE;
        for (String s : sortByList) {
            for (int i = 0; i < object.getDeclaredFields().length; i++) {
                if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
                    isFieldExist = Boolean.TRUE;
                    break;
                } else {
                    isFieldExist = Boolean.FALSE;
                }
            }
            if (!isFieldExist) {
                throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");

            }
        }

    }
}
