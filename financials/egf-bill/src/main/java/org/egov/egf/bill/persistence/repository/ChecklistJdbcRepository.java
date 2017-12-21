package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class ChecklistJdbcRepository extends JdbcRepository {

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(ChecklistEntity.TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<Checklist> search(final ChecklistSearch searchRequest) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null
                && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(),
                    ChecklistEntity.class);
        }

        String orderBy = "order by code";

        if (searchRequest.getSortBy() != null
                && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        searchQuery = searchQuery.replace(":tablename",
                ChecklistEntity.TABLE_NAME);
        searchQuery = searchQuery.replace(":selectfields", " * ");

        // implement jdbc specfic search

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code =:code");
            paramValues.put("code", searchRequest.getCode());
        }

        if (searchRequest.getKey() != null) {
            addAnd(params);
            params.append("key =:key");
            paramValues.put("key", searchRequest.getKey());
        }

        if (searchRequest.getType() != null) {
            addAnd(params);
            params.append("type =:type");
            paramValues.put("type", searchRequest.getType());
        }

        if (searchRequest.getSubType() != null) {
            addAnd(params);
            params.append("subType =:subType");
            paramValues.put("subType", searchRequest.getSubType());
        }

        Pagination<Checklist> page = new Pagination<>();

        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());

        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition",
                    " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<Checklist>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset()
                        * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(ChecklistEntity.class);

        final List<ChecklistEntity> checklistEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues,
                row);

        page.setTotalResults(checklistEntities.size());
        final List<Checklist> checklists = new ArrayList<>();

        for (final ChecklistEntity checklistEntity : checklistEntities)
            checklists.add(checklistEntity.toDomain());

        page.setPagedData(checklists);
        return page;
    }
}