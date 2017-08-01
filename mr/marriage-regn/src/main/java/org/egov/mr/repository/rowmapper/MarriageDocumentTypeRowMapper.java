package org.egov.mr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.model.enums.DocumentProof;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MarriageDocumentTypeRowMapper implements RowMapper<MarriageDocumentType> {

	@Override
	public MarriageDocumentType mapRow(ResultSet rs, int rowNum) throws SQLException {

		MarriageDocumentType marriageDocumentType = new MarriageDocumentType();
		marriageDocumentType.setId(rs.getLong("id"));
		marriageDocumentType.setName(rs.getString("name"));
		marriageDocumentType.setCode(rs.getString("code"));
		marriageDocumentType.setIsActive(rs.getBoolean("isactive"));
		marriageDocumentType.setIsIndividual(rs.getBoolean("isindividual"));
		marriageDocumentType.setIsRequired(rs.getBoolean("isrequired"));
		marriageDocumentType.setProof(DocumentProof.fromValue(rs.getString("proof")));
		marriageDocumentType.setApplicationType(ApplicationType.fromValue(rs.getString("appltype")));// ApplicationType.fromValue(rs.getString("applicationtype")));
		marriageDocumentType.setTenantId(rs.getString("tenantid"));

		return marriageDocumentType;
	}
}
