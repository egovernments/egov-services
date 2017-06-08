package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.mr.model.MarriageRegn;
import org.egov.mr.repository.querybuilder.MarriageRegnQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageRegnIdsRowMapper;
import org.egov.mr.repository.rowmapper.MarriageRegnRowMapper;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MarriageRegnRepository {

	@Autowired
	private MarriageRegnQueryBuilder marriageRegnQueryBuilder;

	@Autowired
	private MarriageRegnIdsRowMapper marriageRegnIdsRowMapper;

	@Autowired
	private MarriageRegnRowMapper marriageRegnRowMapper;

	@Autowired
	private WitnessRepository witnessRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static final String INSERT_JSON_QUERY = "INSERT INTO marriage_regn_json(applicationnumber, doc, tenantid)"
			+ " VALUES(?, to_json(?::json), ?)";

	public static final String INSERT_MARRIAGE_REGN_QUERY = "INSERT INTO egmr_marriage_regn("
			+ " regnunitid, marriagedate, venue, street, placeofmarriage, locality, city, marriagephoto, fee,"
			+ " bridegroomid, brideid, priestname, priestreligion, priestaddress, priestaadhaar, priestmobileno,"
			+ " priestemail, serialno, volumeno, applicationnumber,"
			+ " regnnumber, regndate, status, source, stateid, isactive, approvaldepartment, approvaldesignation, approvalassignee,"
			+ " approvalaction, approvalstatus, approvalcomments, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String UPDATE_MARRIAGE_REGN_QUERY = "UPDATE egmr_marriage_regn"
			+ " SET(regnunitid, marriagedate, venue, street, placeofmarriage, locality, city, marriagephoto, fee,"
			+ " bridegroomid, brideid, priestname, priestreligion, priestaddress, priestaadhaar, priestmobileno,"
			+ " priestemail, serialno, volumeno,"
			+ " regnnumber, regndate, status, source, stateid, isactive, approvaldepartment, approvaldesignation, approvalassignee,"
			+ " approvalaction, approvalstatus, approvalcomments, lastmodifiedby, lastmodifiedtime)"
			+ " = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			+ " WHERE applicationnumber = ? AND tenantid = ?";

	@SuppressWarnings("unchecked")
	public List<MarriageRegn> findForCriteria(MarriageRegnCriteria marriageRegnCriteria) {
		List<Object> preparedStatementValuesForListOfMarriageRegnIds = new ArrayList<Object>();
		String queryStrForListOfMarriageRegnIds = marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(
				marriageRegnCriteria, preparedStatementValuesForListOfMarriageRegnIds);

		List<String> listOfApplNos = jdbcTemplate.query(queryStrForListOfMarriageRegnIds,
				preparedStatementValuesForListOfMarriageRegnIds.toArray(), marriageRegnIdsRowMapper);

		System.out.println("queryStrForListOfMarriageRegnIds " + queryStrForListOfMarriageRegnIds);

		System.out.println("listOfApplNos " + listOfApplNos);
		if (listOfApplNos.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = marriageRegnQueryBuilder.getQuery(marriageRegnCriteria, preparedStatementValues,
				listOfApplNos);
		List<MarriageRegn> marriageRegn = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				marriageRegnRowMapper);
		return marriageRegn;
	}

	public void save(MarriageRegn marriageRegn) {
		Object[] obj = new Object[] { marriageRegn.getRegnUnit().getId(), marriageRegn.getMarriageDate(),
				marriageRegn.getVenue().toString(), marriageRegn.getStreet(), marriageRegn.getPlaceOfMarriage(),
				marriageRegn.getLocality(), marriageRegn.getCity(), marriageRegn.getMarriagePhoto(),
				marriageRegn.getFee(), marriageRegn.getBridegroom().getId(), marriageRegn.getBride().getId(),
				marriageRegn.getPriest().getName(), marriageRegn.getPriest().getReligion(),
				marriageRegn.getPriest().getAddress(), marriageRegn.getPriest().getAadhaar(),
				marriageRegn.getPriest().getMobileNo(), marriageRegn.getPriest().getEmail(), marriageRegn.getSerialNo(),
				marriageRegn.getVolumeNo(), marriageRegn.getApplicationNumber(), marriageRegn.getRegnNumber(),
				marriageRegn.getRegnDate(), marriageRegn.getStatus().toString(), marriageRegn.getSource().toString(),
				marriageRegn.getStateId(), marriageRegn.getIsActive(),
				marriageRegn.getApprovalDetails().getDepartment(), marriageRegn.getApprovalDetails().getDesignation(),
				marriageRegn.getApprovalDetails().getAssignee(), marriageRegn.getApprovalDetails().getAction(),
				marriageRegn.getApprovalDetails().getStatus(), marriageRegn.getApprovalDetails().getComments(),
				marriageRegn.getAuditDetails().getCreatedBy(), marriageRegn.getAuditDetails().getCreatedTime(),
				marriageRegn.getAuditDetails().getLastModifiedBy(),
				marriageRegn.getAuditDetails().getLastModifiedTime(), marriageRegn.getTenantId() };
		jdbcTemplate.update(INSERT_MARRIAGE_REGN_QUERY, obj);

		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.save(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}

	public String generateApplicationNumber() {
		return jdbcTemplate.queryForObject("SELECT nextval('seq_egmr_marriageregn_application_number')", String.class);
	}

	public void update(MarriageRegn marriageRegn) {
		System.out.println("maregn" + marriageRegn);
		Object[] obj = new Object[] { marriageRegn.getRegnUnit().getId(), marriageRegn.getMarriageDate(),
				marriageRegn.getVenue().toString(), marriageRegn.getStreet(), marriageRegn.getPlaceOfMarriage(),
				marriageRegn.getLocality(), marriageRegn.getCity(), marriageRegn.getMarriagePhoto(),
				marriageRegn.getFee(), marriageRegn.getBridegroom().getId(), marriageRegn.getBride().getId(),
				marriageRegn.getPriest().getName(), marriageRegn.getPriest().getReligion(),
				marriageRegn.getPriest().getAddress(), marriageRegn.getPriest().getAadhaar(),
				marriageRegn.getPriest().getMobileNo(), marriageRegn.getPriest().getEmail(), marriageRegn.getSerialNo(),
				marriageRegn.getVolumeNo(), marriageRegn.getRegnNumber(), marriageRegn.getRegnDate(),
				marriageRegn.getStatus().toString(), marriageRegn.getSource().toString(), marriageRegn.getStateId(),
				marriageRegn.getIsActive(), marriageRegn.getApprovalDetails().getDepartment(),
				marriageRegn.getApprovalDetails().getDesignation(), marriageRegn.getApprovalDetails().getAssignee(),
				marriageRegn.getApprovalDetails().getAction(), marriageRegn.getApprovalDetails().getStatus(),
				marriageRegn.getApprovalDetails().getComments(), marriageRegn.getAuditDetails().getLastModifiedBy(),
				marriageRegn.getAuditDetails().getLastModifiedTime(), marriageRegn.getApplicationNumber(),
				marriageRegn.getTenantId() };
		jdbcTemplate.update(UPDATE_MARRIAGE_REGN_QUERY, obj);

		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.update(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});

	}

	public void saveJson(String applicationNumber, String marriageRegnJson, String tenantId) {
		jdbcTemplate.update(INSERT_JSON_QUERY, applicationNumber, marriageRegnJson, tenantId);
	}
}
