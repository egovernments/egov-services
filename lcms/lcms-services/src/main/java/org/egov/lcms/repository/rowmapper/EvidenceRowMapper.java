package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Document;
import org.egov.lcms.models.ReferenceEvidence;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EvidenceRowMapper implements RowMapper<ReferenceEvidence> {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public ReferenceEvidence mapRow(ResultSet rs, int rowNum) throws SQLException {

		ReferenceEvidence referenceEvidence = new ReferenceEvidence();
		referenceEvidence.setCode(rs.getString("code"));
		referenceEvidence.setTenantId(getString(rs.getString("tenantid")));
		referenceEvidence.setReferenceType(getString(rs.getString("referencetype")));
		referenceEvidence.setCaseNo(getString(rs.getString("caseno")));
		referenceEvidence.setReferenceDate(getLong(rs.getLong("referencedate")));
		referenceEvidence.setDescription(getString(rs.getString("description")));
		referenceEvidence.setCaseCode(getString(rs.getString("casecode")));
		referenceEvidence.setReferenceCaseNo(getString(rs.getString("referencecaseno")));
		
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		referenceEvidence.setAuditDetails(auditDetails);

		TypeReference<List<Document>> documentreference = new TypeReference<List<Document>>() {
		};
		try {
			if (rs.getString("documents") != null)
				referenceEvidence.setDocuments(objectMapper.readValue(rs.getString("documents"), documentreference));
		} catch (IOException ex) {
			throw new CustomException(propertiesManager.getEvidenceResponseErrorCode(),
					propertiesManager.getEvidenceResponseErrorMsg());
		}

		return referenceEvidence;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}
