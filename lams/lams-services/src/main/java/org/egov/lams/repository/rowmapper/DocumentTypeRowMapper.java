package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.springframework.jdbc.core.RowMapper;

public class DocumentTypeRowMapper implements RowMapper<DocumentType> {

	@Override
	public DocumentType mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DocumentType documentType = new DocumentType();
		
		documentType.setId(rs.getLong("id"));
		documentType.setName(rs.getString("name"));
		documentType.setApplication(Application.fromValue(rs.getString("application")));
		
		return documentType;
	}

}
