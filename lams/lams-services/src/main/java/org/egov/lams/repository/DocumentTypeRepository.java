package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.DocumentType;
import org.egov.lams.repository.builder.DocumentTypeQueryBuilder;
import org.egov.lams.repository.rowmapper.DocumentTypeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentTypeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<DocumentType> getDocumentTypes(DocumentType documentType) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchQuery = DocumentTypeQueryBuilder.getDocumentTypeQuery(documentType, preparedStatementValues);
		return jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), new DocumentTypeRowMapper());
	}
}
