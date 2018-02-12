package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.DocumentSearch;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_document";

    @Transactional
    public void delete(final String tenantId, final String refCode) {
        delete(TABLE_NAME, tenantId, "refCode", refCode);
    }

    public List<Document> search(final DocumentSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getId() != null) {
            addAnd(params);
            params.append("id =:id");
            paramValues.put("id", searchRequest.getId());
        }

        if (searchRequest.getRefCode() != null) {
            addAnd(params);
            params.append("refCode =:refCode");
            paramValues.put("refCode", searchRequest.getRefCode());
        }

        if (searchRequest.getRefCodes() != null) {
            addAnd(params);
            params.append("refCode in (:refCodes)");
            paramValues.put("refCodes", new ArrayList<>(Arrays.asList(searchRequest.getRefCodes().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(Document.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}