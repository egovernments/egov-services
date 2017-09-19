package org.egov.lams.repository;

import org.egov.lams.model.DocumentType;
import org.egov.lams.repository.builder.DocumentTypeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DocumentTypeRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<DocumentType> getDocumentTypes(DocumentType documentType) {

        Map params = new HashMap();
        String searchQuery = DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, params);
        return namedParameterJdbcTemplate.query(searchQuery, params, new BeanPropertyRowMapper<>(DocumentType.class));
    }
}
