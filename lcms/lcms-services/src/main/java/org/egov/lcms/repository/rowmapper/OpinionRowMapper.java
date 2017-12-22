package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.CaseDetails;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.Document;
import org.egov.lcms.models.Opinion;
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
* Veswanth		28th Oct 2017						Initial commit for OpinionRowMapper
* Veswanth		28th Oct 2017						Added null check for the searchResult parameters
* Shubham		31st Oct 2017						Changed to documentReference type to Document for OpinionSearch result
* Veswanth		03rd Nov 2017						Added department and advocateDetails in opinionSearch
* Veswanth		07th Nov 2017						Added additionalAdvoate for opinionSearch
* Veswanth		10th Nov 2017						Added summonReferenceNo for opinionSearch
*/
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

		opinion.setOpinionOn(getString(rs.getString("opinionon")));
		opinion.setOpinionDescription(getString(rs.getString("opiniondescription")));
		opinion.setInWardDate(getLong(rs.getLong("inwarddate")));
		opinion.setTenantId(getString(rs.getString("tenantid")));
		opinion.setStateId(getString(rs.getString("stateid")));
		opinion.setAdditionalAdvocate(getString(rs.getString("additionaladvocate")));

		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setSummonReferenceNo(getString(rs.getString("summonreferenceno")));

		opinion.setCaseDetails(caseDetails);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		opinion.setAuditDetails(auditDetails);

		TypeReference<List<Document>> documentReference = new TypeReference<List<Document>>() {
		};
		TypeReference<Advocate> advocateReference = new TypeReference<Advocate>() {
		};
		TypeReference<Department> departmentReference = new TypeReference<Department>() {
		};

		try {
			if (rs.getString("documents") != null)
				opinion.setDocuments(objectMapper.readValue(rs.getString("documents"), documentReference));
			if (rs.getString("opinionsby") != null)
				opinion.setOpinionsBy(objectMapper.readValue(rs.getString("opinionsby"), advocateReference));
			if (rs.getString("departmentname") != null)
				opinion.setDepartmentName(objectMapper.readValue(rs.getString("departmentname"), departmentReference));
		} catch (IOException ex) {
			throw new CustomException(propertiesManager.getOpinionSearchErrorCode(),
					propertiesManager.getOpinionSearchErrorMsg());
		}
		return opinion;
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