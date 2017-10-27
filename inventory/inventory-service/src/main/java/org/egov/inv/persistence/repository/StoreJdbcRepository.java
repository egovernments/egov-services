package org.egov.inv.persistence.repository;

import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.persistence.entity.StoresEntity;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreJdbcRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Pagination<Store> search(StoreGetRequest storeGetRequest) {
        String searchQuery = "select * from stores :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (storeGetRequest.getSortBy() != null && !storeGetRequest.getSortBy().isEmpty()) {
            validateSortByOrder(storeGetRequest.getSortBy());
            validateEntityFieldName(storeGetRequest.getSortBy(), StoreGetRequest.class);
        }
        String orderBy = "order by code";
        if (storeGetRequest.getSortBy() != null && !storeGetRequest.getSortBy().isEmpty()) {
            orderBy = "order by " + storeGetRequest.getSortBy();
        }
        if (storeGetRequest.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", storeGetRequest.getIds());
        }
        if (storeGetRequest.getCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("code = :code");
            paramValues.put("code", storeGetRequest.getCode());
        }
        if (storeGetRequest.getName() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("name = :name");
            paramValues.put("name", storeGetRequest.getName());
        }
        if (storeGetRequest.getDescription() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("description = :description");
            paramValues.put("description", storeGetRequest.getDescription());
        }
        if (storeGetRequest.getDepartment() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("departmentcode = :department");
            paramValues.put("department", storeGetRequest.getDepartment());
        }
        if (storeGetRequest.getContactNo1() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("contactnumber1 = :contactno1");
            paramValues.put("contactno1", storeGetRequest.getContactNo1());
        }
        if (storeGetRequest.getEmail() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("emailid = :email");
            paramValues.put("email", storeGetRequest.getEmail());
        }
        if (storeGetRequest.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active = :active");
            paramValues.put("active", storeGetRequest.getActive());

        }
        if (storeGetRequest.getIsCentralStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("iscentralstore = :iscentralstore");
            paramValues.put("iscentralstore", storeGetRequest.getIsCentralStore());
        }
        if (storeGetRequest.getStoreInCharge() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("storeinchargecode = :storeincharge");
            paramValues.put("storeincharge", storeGetRequest.getStoreInCharge());
        }
        if (storeGetRequest.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", storeGetRequest.getTenantId());
        }
        Pagination<Store> page = new Pagination<>();
        if (storeGetRequest.getPageSize() != null)
            page.setPageSize(storeGetRequest.getPageSize());
        if (storeGetRequest.getOffset() != null)
            page.setOffset(storeGetRequest.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<Store>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(StoresEntity.class);

        List<Store> storesList = new ArrayList<>();

        List<StoresEntity> storesEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (StoresEntity storesEntity : storesEntities) {

            storesList.add(storesEntity.toDomain());
        }

        page.setTotalResults(storesList.size());

        page.setPagedData(storesList);

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

    public String checkCodeExistsQuery() {
        return "select id from stores where code = :code and tenantid = :tenantid";
    }

}
