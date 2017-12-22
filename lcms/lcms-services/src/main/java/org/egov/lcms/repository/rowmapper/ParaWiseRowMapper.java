package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Document;
import org.egov.lcms.models.ParaWiseComment;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Veswanth		01st Nov 2017						Initial commit for parawiseComments RowMapper
* Veswanth		02nd Nov 2017						Renamed assgnedDate to assignDate in opinionSearch Result
*/
@Component
public class ParaWiseRowMapper implements RowMapper<ParaWiseComment> {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public ParaWiseComment mapRow(ResultSet rs, int rowNum) throws SQLException {

		ParaWiseComment paraWiseComment = new ParaWiseComment();
		paraWiseComment.setCode(getString(rs.getString("code")));
		paraWiseComment.setParawiseCommentsAskedDate(getLong(rs.getLong("parawisecommentsaskeddate")));
		paraWiseComment.setParawiseCommentsReceivedDate(getLong(rs.getLong("parawisecommentsreceiveddate")));
		paraWiseComment.setHodProvidedDate(getLong(rs.getLong("hodprovideddate")));
		paraWiseComment.setResolutionDate(getLong(rs.getLong("resolutiondate")));
		paraWiseComment.setParaWiseComments(getString(rs.getString("parawisecomments")));
		paraWiseComment.setTenantId(getString(rs.getString("tenantid")));

		List<Document> documents = new ArrayList<Document>();
		TypeReference<List<Document>> documentReference = new TypeReference<List<Document>>() {
		};
		try {
			if (rs.getString("documents") != null)
				documents = objectMapper.readValue(rs.getString("documents"), documentReference);
		} catch (IOException ex) {
			throw new CustomException(propertiesManager.getParaWiseResponseErrorCode(),
					propertiesManager.getParaWiseCommentsResponseErrorMsg());
		}

		paraWiseComment.setDocuments(documents);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		paraWiseComment.setAuditDetails(auditDetails);

		return paraWiseComment;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}
	
	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}
