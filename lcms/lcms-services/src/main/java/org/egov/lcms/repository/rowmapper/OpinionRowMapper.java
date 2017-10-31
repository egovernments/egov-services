package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Document;
import org.egov.lcms.models.Opinion;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OpinionRowMapper implements RowMapper<Opinion> {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public Opinion mapRow(ResultSet rs, int rowNum) throws SQLException {

		Opinion opinion = new Opinion();
		opinion.setCode(rs.getString("code"));
		opinion.setOpinionRequestDate(getLong(rs.getLong("opinionrequestdate")));

		opinion.setDepartmentName(getString(rs.getString("departmentname")));
		opinion.setOpinionOn(getString(rs.getString("opinionon")));
		opinion.setOpinionDescription(getString(rs.getString("opinionDescription")));
		opinion.setInWardDate(getLong(rs.getLong("inwarddate")));
		opinion.setTenantId(getString(rs.getString("tenantid")));
		opinion.setStateId(getString(rs.getString("stateid")));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		opinion.setAuditDetails(auditDetails);

		List<Document> documents = new ArrayList<Document>();
		Advocate opinionBy = new Advocate();
		TypeReference<List<Document>> documentReference = new TypeReference<List<Document>>() {
		};
		TypeReference<Advocate> advocateReference = new TypeReference<Advocate>() {
		};
		try {
			if (rs.getString("documents") != null)
				documents = objectMapper.readValue(rs.getString("documents"), documentReference);
			if (rs.getString("opinionsby") != null)
				opinionBy = objectMapper.readValue(rs.getString("opinionsby"), advocateReference);
		} catch (IOException ex) {
			throw new CustomException(propertiesManager.getJsonStringError(), ex.getMessage());
		}

		opinion.setDocuments(documents);
		opinion.setOpinionsBy(opinionBy);
		return opinion;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}